## Spring Data Jpa 笔记

#### 入门案例

##### 1、新建maven工程

##### 2、导入约束

```xml
<properties>      
    <project.build.sourceEncoding>
        UTF8
    </project.build.sourceEncoding>
 <project.hibernate.version>
     5.0.7.Final
    </project.hibernate.version>
</properties>

<dependencies>
        <!-- junit -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.12</version>
            <scope>test</scope>
        </dependency>

        <!-- hibernate对jpa的支持包 -->
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-entitymanager</artifactId>
            <version>${project.hibernate.version}</version>
        </dependency>

        <!-- c3p0 -->
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-c3p0</artifactId>
            <version>${project.hibernate.version}</version>
        </dependency>

        <!-- log日志 -->
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>1.2.17</version>
        </dependency>

        <!-- Mysql and MariaDB -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>5.1.6</version>
        </dependency>
    </dependencies>
```

##### 3、创建客户表

```sql
    CREATE TABLE cst_customer (
      cust_id bigint(32) NOT NULL AUTO_INCREMENT COMMENT '客户编号(主键)',
      cust_name varchar(32) NOT NULL COMMENT '客户名称(公司名称)',
      cust_source varchar(32) DEFAULT NULL COMMENT '客户信息来源',
      cust_industry varchar(32) DEFAULT NULL COMMENT '客户所属行业',
      cust_level varchar(32) DEFAULT NULL COMMENT '客户级别',
      cust_address varchar(128) DEFAULT NULL COMMENT '客户联系地址',
      cust_phone varchar(64) DEFAULT NULL COMMENT '客户联系电话',
      PRIMARY KEY (`cust_id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
```

##### 4、创建实体类并配置映射关系

```java
/**
 * 配置实体类与数据库的映射关系
 * @Entity 声明实体类
 * @Table 建立实体类和表的映射关系
 * @Id 声明主键
 * @GeneratedValue 主键的生成策略
 * @Column 指定和表中字段的映射关系
 *
 */
@Entity
@Table(name="cst_customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cust_id")
    private Long custId;   // 客户Id 主键

    @Column(name = "cust_name")
    private String custName; // 客户姓名

    @Column(name = "cust_source")
    private String custSource; // 客户来源

    @Column(name = "cust_industry")
    private String custIndustry; // 客户级别

    @Column(name = "cust_level")
    private String custLevel; //客户级别

    @Column(name = "cust_address")
    private String custAddress; // 客户地址

    @Column(name = "cust_phone")
    private String custPhone; // 联系方式

    public Customer() {
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustSource() {
        return custSource;
    }

    public void setCustSource(String custSource) {
        this.custSource = custSource;
    }

    public String getCustIndustry() {
        return custIndustry;
    }

    public void setCustIndustry(String custIndustry) {
        this.custIndustry = custIndustry;
    }

    public String getCustLevel() {
        return custLevel;
    }

    public void setCustLevel(String custLevel) {
        this.custLevel = custLevel;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "custId=" + custId +
                ", custName='" + custName + '\'' +
                ", custSource='" + custSource + '\'' +
                ", custIndustry='" + custIndustry + '\'' +
                ", custLevel='" + custLevel + '\'' +
                ", custAddress='" + custAddress + '\'' +
                ", custPhone='" + custPhone + '\'' +
                '}';
    }
}

```

##### 5、创建JPA的核心配置类

**在resource目录下新建META-INF的文件夹，在META-INF目录下新建persistence.xml文件**

注：xml文件第一行不能添加注释

```xml
<!--Spring Data Jpa 的核心配置类-->
<?xml version="1.0" encoding="UTF-8"?>
<persistence xmlns="http://java.sun.com/xml/ns/persistence" version="2.0">

    <!--配置持久化单元 
		name：持久化单元名称 
		transaction-type：事务类型
		 	RESOURCE_LOCAL：本地事务管理 
		 	JTA：分布式事务管理 -->
    <persistence-unit name="jpa" transaction-type="RESOURCE_LOCAL">
        <!--jpa的实现方式-->
        <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>

        <!--数据库相关配置-->
        <properties>
            <property name="javax.persistence.jdbc.user" value="root"/>
            <property name="javax.persistence.jdbc.password" value="root"/>
            <property name="javax.persistence.jdbc.driver" value="com.mysql.jdbc.Driver"/>
            <property name="javax.persistence.jdbc.url" value="jdbc:mysql:///jpa"/>
        
        <!--配置jpa实现方(hibernate)的配置信息
        显示sql hibernate.show_sql： false|true 自动创建数据库表 ：
        hibernate.hbm2ddl.auto：
            create : 程序运行时创建数据库表（如果有表，先删除表再创建）
            update ：程序运行时创建表（如果有表，不会创建表）
        	none ：不会创建表 -->
            <property name="hibernate.show_sql" value="true"/>
            <property name="hibernate.hbm2ddl.auto" value="update"/>
        </properties>
    </persistence-unit>
</persistence>

```



##### 6、创建测试类

```java
public class CustomerTest {
  
    @Test
    public void saveCustomerTest() {
        // myJpa是persistence.xml中persistence-unit标签的name值
        // 1、加载配置文件创建工厂(实体管理类工厂)
        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("myJpa");
        // 2、通过实体管理类工厂获取实体管理器
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        // 3、获取事务对象，开启事务
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

		4、完成保存操作
        Customer customer = new Customer();
        customer.setCustName("Spring Data JPA");
        entityManager.persist(customer);
        
		// 5、提交事务
        transaction.commit();
		// 6、释放资源
        entityManager.close();
        entityManagerFactory.close();
    }
}
```

#### JPA的主键生成策略			

- GenerationType.IDENTITY       自增，mysql,底层数据库必须支持自动增长

- GenerationType.SEQUENCE    序列，oracle,底层数据库必须支持序列
- GenerationType.TABLE   jpa提供的一种机制，通过一张数据库表的形式帮助完成主键自增
- GenerationType.AUTO  程序自动帮我们选择主键生成策略



####  JPA操作小结

* 1、加载配置文件创建工厂(实体管理类工厂)

  * **Persistence**: 静态方法  根据持久化单元名称创建实体管理器工厂

    createEntityManagerFactory ( 持久化单元名称 )

  * 作用：创建实体管理器工厂

* 2、通过实体管理类工厂获取实体管理器

  * **EntityManagerFactory**: 获取EntityManager对象

    方法：createEntityManager

    - 内部维护了很多内容

    - 内部维护了数据库信息

    - 维护了缓存信息
    - 维护了所有的实体管理器
    - 在创建EntityManagerFactory的过程中会根据配置创建数据表

  - EntityManagerFactory的常见过程比较浪费资源

    特点：线程安全的类

    - 如何解决EntityManagerFactory创建浪费资源耗时问题

      创建一个公共的EntityManagerFactory的对象

      静态代码块的形式创建EntityManagerFactory

*   3、获取事务对象，开启事务

  * **EntityManager**对象，实体管理器。真正操作数据库的对象

    beginTransaction: 创建事务对象

    presist: 保存

    merge: 更新

    remove: 删除

    find/getRefrence: 根据id查询

  * **Trancastion** 对象: 事务

    begin： 开启

    commit：提交

    rollback: 回滚

* 4、完成保存操作

*   5、提交事务

*  6、释放资源
   