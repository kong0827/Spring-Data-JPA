## Spring Data Jpa 笔记

#### 入门案例

	##### 1、新建maven工程

##### 2、导入约束

```xml
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
    /**
     * 操作步骤
     *  1、加载配置文件创建工厂(实体管理类工厂)
     *  2、通过实体管理类工厂获取实体管理器
     *  3、获取事务对象，开启事务
     *  4、完成保存操作
     *  5、提交事务
     *  6、释放资源
     */
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