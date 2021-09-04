import com.intellij.database.model.DasTable
import com.intellij.database.model.ObjectKind
import com.intellij.database.util.Case
import com.intellij.database.util.DasUtil

import java.text.SimpleDateFormat
import java.time.LocalDateTime

/*
 * Available context bindings:
 *   SELECTION   Iterable<DasObject>
 *   PROJECT     project
 *   FILES       files helper
 */
packageName = ""
typeMapping = [
        (~/bigint/)                       : "Long",
        (~/(?i)int/)                      : "Integer",
        (~/(?i)float|double|real/)       :  "Double",
        (~/decimal/)                        : "BigDecimal",
        (~/(?i)bool|boolean/)             : "Boolean",
        (~/datetime|timestamp/)           : "LocalDateTime",
        (~/date/)                         : "LocalDate",
        (~/(?i)time/)                     : "LocalDateTime",
        (~/(?i)char|text/)                : "String",
        (~/(?i)/)                         : "String"
]

FILES.chooseDirectoryAndSave("Choose directory", "Choose where to store generated files") { dir ->
    SELECTION.filter { it instanceof DasTable && it.getKind() == ObjectKind.TABLE }.each { generate(it, dir) }
}

def generate(table, dir) {
    def className = javaName(table.getName(), true)
    def fields = calcFields(table)
    packageName = getPackageName(dir)
    new File(dir, className + "Entity.java").withPrintWriter("UTF-8") { out -> generate(out, table, className, fields) }
}

def generate(out, table, className, fields) {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    def nowTime = format.format(new Date());
    def tableName = table.getName()
    out.println "package $packageName"
    out.println ""
    out.println "import lombok.Data;"
    out.println ""
    out.println "import javax.persistence.*;"
    out.println "import java.io.Serializable;"
    out.println "import java.time.LocalDateTime;"
    out.println "/**"
    out.println " * "
    out.println " * @author cg1"
    out.println " * @date $nowTime"
    out.println " */"
    out.println "@Data"
    out.println "@Entity"
    out.println "@Table(name = \"$tableName\")"
    out.println "public class $className"+"Entity implements Serializable {"
    out.println ""
    // 判断自增
    if ((tableName + "_id").equalsIgnoreCase(fields[0].colum) || "id".equalsIgnoreCase(fields[0].colum)) {
        out.println "\t@Id"
        out.println "\t@GeneratedValue(strategy=GenerationType.IDENTITY)"
    }
    fields.each() {
        if (it.annos != "") out.println "  ${it.annos}"
        // 输出注释
        if (isNotEmpty(it.comment)) {
            out.println "\t/**"
            out.println "\t * ${it.comment}"
            out.println "\t */"
        }
        out.println "\t@Column(name = \"${it.colum}\" ,columnDefinition= \"${it.sqlType}\" )"
        String colName = it.name
        if (colName.startsWith("is")) {
            it.name = colName.substring(2).toLowerCase()
        }
        out.println "\tprivate ${it.type} ${it.name};"
        out.println ""
    }
    out.println "}"
}

def calcFields(table) {
    DasUtil.getColumns(table).reduce([]) { fields, col ->
        def spec = Case.LOWER.apply(col.getDataType().getSpecification())
        def typeStr = typeMapping.find { p, t -> p.matcher(spec).find() }.value
        String sqlTypeStr = spec;
        if (sqlTypeStr!=null&&!sqlTypeStr.isEmpty()&&sqlTypeStr.contains("(")){
            sqlTypeStr = sqlTypeStr.substring(0, sqlTypeStr.indexOf("("));
        }
        fields += [[
                           name   : javaName(col.getName(), false),
                           colum  : col.getName(),
                           type   : typeStr,
                           sqlType   : sqlTypeStr,
                           comment: col.getComment(),
                           annos  : ""]]
    }
}

def javaName(str, capitalize) {
    def s = str.split(/(?<=[^\p{IsLetter}])/).collect { Case.LOWER.apply(it).capitalize() }
            .join("").replaceAll(/[^\p{javaJavaIdentifierPart}]/, "_").replaceAll(/_/, "")
    capitalize || s.length() == 1 ? s : Case.LOWER.apply(s[0]) + s[1..-1]
}
/**
 * 获取包名称
 * @param dir 实体类所在目录
 * @return
 */
def getPackageName(dir) {
    String absolutePath = dir.toString()
    def replace = absolutePath.substring(absolutePath.indexOf("java\\") + 5).replace("\\", ".");
    return replace + ";"
}

def isNotEmpty(content) {
    return content != null && content.toString().trim().length() > 0
}
