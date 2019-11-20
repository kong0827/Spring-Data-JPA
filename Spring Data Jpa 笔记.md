## Spring Data Jpa 笔记

#### JPA入门案例

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
   







#### JPA简单的CURD操作

#### Spring Data Jpa 入门案例

##### 1、新建maven工程

##### 2、导入约束

**springDataJpa的2.0版本以上必须和Spring5.0以上版本整合，否则会出现错误**

```xml
<properties>    <spring.version>4.3.25.RELEASE</spring.version>    <hibernate.version>5.0.7.Final</hibernate.version>
        <slf4j.version>1.6.6</slf4j.version>
        <log4j.version>1.2.12</log4j.version>
        <c3p0.version>0.9.1.2</c3p0.version>
        <mysql.version>5.1.6</mysql.version>
    </properties>
<dependencies>
        <!-- junit单元测试 -->
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <version>4.9</version>
            <scope>test</scope>
        </dependency>

        <!-- spring beg -->
        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.6.8</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aop</artifactId>
            <version>${spring.version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>${spring.version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context-support</artifactId>
            <version>${spring.version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-orm</artifactId>
            <version>${spring.version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-beans</artifactId>
            <version>${spring.version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-core</artifactId>
            <version>${spring.version}</version>
        </dependency>

        <!-- spring end -->

        <!-- hibernate beg -->
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-core</artifactId>
            <version>${hibernate.version}</version>
        </dependency>
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-entitymanager</artifactId>
            <version>${hibernate.version}</version>
        </dependency>
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-validator</artifactId>
            <version>5.2.1.Final</version>
        </dependency>
        <!-- hibernate end -->

        <!-- c3p0 beg -->
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>1.0.9</version>
        </dependency>
        <!-- c3p0 end -->

        <!-- log end -->
        <dependency>
            <groupId>log4j</groupId>
            <artifactId>log4j</artifactId>
            <version>${log4j.version}</version>
        </dependency>

        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>${slf4j.version}</version>
        </dependency>

        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-log4j12</artifactId>
            <version>${slf4j.version}</version>
        </dependency>
        <!-- log end -->


        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>${mysql.version}</version>
        </dependency>

        <dependency>
            <groupId>org.springframework.data</groupId>
            <artifactId>spring-data-jpa</artifactId>
            <version>2.0.10.RELEASE</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-test</artifactId>
            <version>4.2.4.RELEASE</version>
        </dependency>

        <!-- el beg 使用spring data jpa 必须引入 -->
        <dependency>
            <groupId>javax.el</groupId>
            <artifactId>javax.el-api</artifactId>
            <version>2.2.4</version>
        </dependency>

        <dependency>
            <groupId>org.glassfish.web</groupId>
            <artifactId>javax.el</artifactId>
            <version>2.2.4</version>
        </dependency>
        <!-- el end -->
    </dependencies>
```

##### 3、spring整合springdatajpa配置

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:aop="http://www.springframework.org/schema/aop"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:jdbc="http://www.springframework.org/schema/jdbc" xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:jpa="http://www.springframework.org/schema/data/jpa" xmlns:task="http://www.springframework.org/schema/task"
       xsi:schemaLocation="
		http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
		http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop.xsd
		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd
		http://www.springframework.org/schema/jdbc http://www.springframework.org/schema/jdbc/spring-jdbc.xsd
		http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx.xsd
		http://www.springframework.org/schema/data/jpa
		http://www.springframework.org/schema/data/jpa/spring-jpa.xsd">

    <!--1、数据库连接池-->
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="driver" value="com.mysql.jdbc.Driver"/>
        <property name="url" value="jdbc:mysql://localhost:3306/jpa"/>
        <property name="username" value="root"/>
        <property name="password" value="root"/>
    </bean>

    <!--2、配置entityManagerFactory-->
    <bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <!--配置扫描的包(实体类)-->
        <property name="packagesToScan" value="com.kxj.entity"/>
        <!--jpa的实现厂家-->
        <property name="persistenceProvider">
            <bean class="org.hibernate.jpa.HibernatePersistenceProvider"></bean>
        </property>
        <!--jpa的供应商适配器-->
        <property name="jpaVendorAdapter">
            <bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter">
                <!--配置是否自动创建数据库 -->
                <property name="generateDdl" value="false"/>
                <!--指定数据库类型-->
                <property name="database" value="MYSQL"/>
                <!--数据库方言-->
                <property name="databasePlatform" value="org.hibernate.dialect.MySQLDialect"/>
                <!--是否显示SQL-->
                <property name="showSql" value="true"/>
            </bean>
        </property>

        <!-- jpa的方言-->
        <property name="jpaDialect">
            <bean class="org.springframework.orm.jpa.vendor.HibernateJpaDialect"/>
        </property>
    </bean>

    <!--3、事务管理器 JPA事务管理器-->
    <bean name="tranasctionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
        <property name="entityManagerFactory" ref="entityManagerFactory"/>
    </bean>

    <!--4、整合spring data jpa-->
    <jpa:repositories base-package="com.kxj.dao" transaction-manager-ref="tranasctionManager"
        entity-manager-factory-ref="entityManagerFactory"
    >
    </jpa:repositories>

    <!--5、声明式事务-->
    <tx:advice id="txAdvice" transaction-manager="tranasctionManager">
        <tx:attributes>
            <tx:method name="save*" propagation="REQUIRED"/>
            <tx:method name="insert*" propagation="REQUIRED"/>
            <tx:method name="update*" propagation="REQUIRED"/>
            <tx:method name="delete*" propagation="REQUIRED"/>
            <tx:method name="get*" read-only="true"/>
            <tx:method name="find*" read-only="true"/>
            <tx:method name="*" propagation="REQUIRED"/>
        </tx:attributes>
    </tx:advice>

    <!--6、配置包扫描-->
    <context:component-scan base-package="com.kxj"/>
</beans>
```



#### JPQL查询

spring data jpa 1.0 和2.0部分方法存在差异，因此在使用的时候需要注意，否则，1.0能够跑起来的程序，2.0版本是会报错的。这里的1.0版本和2.0版本分别值得是我测试的版本，分别为1.9。0，2.0版本为2.1.12.其他版本差异可以参考官方文档。

例如：

- **JPQL和SQL语句存在差异**

  不可混用，否则报错

  ```
  sql  : update cst_customer set cust_name = ? where cust_id = ?
  jpql : update Customer set custName = ? where custId = ?
  ```

- **方法名的差异：**

​	springdatajpa：1.0版本 boolean exists(ID primaryKey)

​	springdatajpa：2.0版本 boolean existsById(ID primaryKey)

- **JPQL创建查询时：**

​	springdatajpa：1.0版本  可以指定展位符的位置，也可以不指定

```java
@Query(value="from Customer where custName = ?")
// 或@Query(value="from Customer where custName = ?")
public Customer findJpql(String custName);
```

​	2.0版本中必须指定位置，否则程序运行会报错

```java
@Query(value="from Customer where custName = ?")
public Customer findJpql(String custName);
```

​	同时2.0版本中  **Using named parameters**   也不再支持

```java
public interface UserRepository extends JpaRepository<User, Long> {

  @Query("select u from User u where u.firstname = :firstname or u.lastname = :lastname")
  User findByLastnameOrFirstname(@Param("lastname") String lastname,
                                 @Param("firstname") String firstname);
}
```









##### 提升

1. Spring Data Jpa @Query注解解析对象(使用SPEL)

   ```java
   @Query(value = "from ProduceRecord where VIN like concat('%',?#{#produceRecord.VIN}, '%') " +
               "and (?#{#produceRecord.equipmentType} IS NULL or equipmentType = ?#{#produceRecord.equipmentType})"+
               "and (:startDate IS NULL or writeTime >= :startDate)"+
               "and (:endDate IS NULL or writeTime <= :endDate)"+
               "and (?#{#produceRecord.workshop} IS NULL or workshop = ?#{#produceRecord.workshop})"+
               "and (?#{#produceRecord.equipmentIP} IS NULL or equipmentIP = ?#{#produceRecord.equipmentIP})"
       )
       public List<ProduceRecord> findProduceRecordList(@Param("produceRecord") ProduceRecord produceRecord,@Param("startDate") Date startDate,@Param("endDate") Date endDate,@Param("pagebale") Pageable pagebale);
   
   ```

   - **传入Pageable对象会自动解析**
   - **and（字段值 is null or 字段属性=字段值）**根据字段是否存在添加and 条件
   - **?#{#Object.property}**  获得对象参数

   

