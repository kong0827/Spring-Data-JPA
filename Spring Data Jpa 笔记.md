## Spring Data Jpa 笔记

### 简介

Spring Data JPA 是 Spring 基于 ORM 框架、JPA 规范的基础上封装的一套JPA应用框架，可使开发者用极简的代码即可实现对数据库的访问和操作。它提供了包括增删改查等在内的常用功能，且易于扩展！学习并使用 Spring Data JPA 可以极大提高开发效率！

 

Spring Data JPA 让我们解脱了DAO层的操作，基本上所有CRUD都可以依赖于它来实现,在实际的工作工程中，推荐使用Spring Data JPA + ORM（如：hibernate）完成操作，这样在切换不同的ORM框架时提供了极大的方便，同时也使数据库层操作更加简单，方便解耦

```
springdatajpa
	day1:orm思想和hibernate以及jpa的概述和jpa的基本操作
	day2：springdatajpa的运行原理以及基本操作
	day3：多表操作，复杂查询

第一 orm思想
	主要目的：操作实体类就相当于操作数据库表
	建立两个映射关系：
		实体类和表的映射关系
		实体类中属性和表中字段的映射关系
	不再重点关注：sql语句
	
	实现了ORM思想的框架：mybatis，hibernate

第二 hibernate框架介绍
	Hibernate是一个开放源代码的对象关系映射框架，
		它对JDBC进行了非常轻量级的对象封装，
		它将POJO与数据库表建立映射关系，是一个全自动的orm框架

第三 JPA规范
	jpa规范，实现jpa规范，内部是由接口和抽象类组成

第四 jpa的基本操作
	案例：是客户的相关操作（增删改查）
		客户：就是一家公司
	客户表：
	
	jpa操作的操作步骤
		1.加载配置文件创建实体管理器工厂
			Persisitence：静态方法（根据持久化单元名称创建实体管理器工厂）
				createEntityMnagerFactory（持久化单元名称）
			作用：创建实体管理器工厂
			
		2.根据实体管理器工厂，创建实体管理器
			EntityManagerFactory ：获取EntityManager对象
			方法：createEntityManager
			* 内部维护的很多的内容
				内部维护了数据库信息，
				维护了缓存信息
				维护了所有的实体管理器对象
				再创建EntityManagerFactory的过程中会根据配置创建数据库表
			* EntityManagerFactory的创建过程比较浪费资源
			特点：线程安全的对象
				多个线程访问同一个EntityManagerFactory不会有线程安全问题
			* 如何解决EntityManagerFactory的创建过程浪费资源（耗时）的问题？
			思路：创建一个公共的EntityManagerFactory的对象
			* 静态代码块的形式创建EntityManagerFactory
			
		3.创建事务对象，开启事务
			EntityManager对象：实体类管理器
				beginTransaction : 创建事务对象
				presist ： 保存
				merge  ： 更新
				remove ： 删除
				find/getRefrence ： 根据id查询
				
			Transaction 对象 ： 事务
				begin：开启事务
				commit：提交事务
				rollback：回滚
		4.增删改查操作
		5.提交事务
		6.释放资源
	
	i.搭建环境的过程
		1.创建maven工程导入坐标
		2.需要配置jpa的核心配置文件
			*位置：配置到类路径下的一个叫做 META-INF 的文件夹下
			*命名：persistence.xml
		3.编写客户的实体类
		4.配置实体类和表，类中属性和表中字段的映射关系
		5.保存客户到数据库中
	ii.完成基本CRUD案例
		persist ： 保存
		merge ： 更新
		remove ： 删除
		find/getRefrence ： 根据id查询
		
	iii.jpql查询
		sql：查询的是表和表中的字段
		jpql：查询的是实体类和类中的属性
		* jpql和sql语句的语法相似
		
		1.查询全部
		2.分页查询
		3.统计查询
		4.条件查询
		5.排序
		
		
```





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

    // get set方法
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



### JPA的API介绍

- #### **Persistence对象**

Persistence对象主要作用是用于获取EntityManagerFactory对象的 。通过调用该类的createEntityManagerFactory静态方法，根据配置文件中持久化单元名称创建EntityManagerFactory。

```java
//1. 创建 EntitymanagerFactory
@Test
String unitName = "myJpa";
EntityManagerFactory factory= Persistence.createEntityManagerFactory(unitName);
```

- #### EntityManagerFactory

EntityManagerFactory 接口主要用来创建 EntityManager 实例 

```java
//创建实体管理类EntityManager em = factory.createEntityManager();
```

由于EntityManagerFactory 是一个线程安全的对象（即多个线程访问同一个EntityManagerFactory 对象不会有线程安全问题），并且EntityManagerFactory 的创建极其浪费资源，所以在使用JPA编程时，我们可以对EntityManagerFactory 的创建进行优化，只需要做到一个工程只存在一个EntityManagerFactory 即可

- **EntityManager**

在 JPA 规范中, EntityManager是完成持久化操作的核心对象。实体类作为普通 java对象，只有在调用 EntityManager将其持久化后才会变成持久化对象。EntityManager对象在一组实体类与底层数据源之间进行 O/R 映射的管理。它可以用来管理和更新 Entity Bean, 根椐主键查找 Entity Bean, 还可以通过JPQL语句查询实体。

我们可以通过调用EntityManager的方法完成获取事务，以及持久化数据库的操作

```java
getTransaction : 获取事务对象
persist ： 保存操作
merge ： 更新操作
remove ： 删除操作
find/getReference ： 根据id查询
```

- **EntityTransaction**

  在 JPA 规范中, EntityTransaction是完成事务操作的核心对象，对于EntityTransaction在我们的java代码中承接的功能比较简单

  ```java
  begin：开启事务
  commit：提交事务
  rollback：回滚事务
  ```



### ***JPA中的复杂查询***

JPQL全称**Java Persistence Query Language**

基于首次在EJB2.0中引入的EJB查询语言(EJB QL),Java持久化查询语言(JPQL)是一种可移植的查询语言，旨在以面向对象表达式语言的表达式，将SQL语法和简单查询语义绑定在一起·使用这种语言编写的查询是可移植的，可以被编译成所有主流数据库服务器上的SQL。

其特征与原生SQL语句类似，并且完全面向对象，通过类名和属性访问，而不是表名和表的属性。

#### **查询所有**

```java
//查询所有客户
	@Test
	public void findAll() {
		EntityManager em = null;
		EntityTransaction tx = null;
		try {
			//获取实体管理对象
			em = JPAUtil.getEntityManager();
			//获取事务对象
			tx = em.getTransaction();
			tx.begin();
			// 创建query对象
			String jpql = "from Customer";
			Query query = em.createQuery(jpql);
			// 查询并得到返回结果
			List list = query.getResultList(); // 得到集合返回类型
			for (Object object : list) {
				System.out.println(object);
			}
			tx.commit();
		} catch (Exception e) {
			// 回滚事务
			tx.rollback();
			e.printStackTrace();
		} finally {
			// 释放资源
			em.close();
		}
	}
```

#### **分页查询( 基于JPA1.9 )**

```java
//分页查询客户
	@Test
	public void findPaged () {
		EntityManager em = null;
		EntityTransaction tx = null;
		try {
			//获取实体管理对象
			em = JPAUtil.getEntityManager();
			//获取事务对象
			tx = em.getTransaction();
			tx.begin();

			//创建query对象
			String jpql = "from Customer";
			Query query = em.createQuery(jpql);
			//起始索引
			query.setFirstResult(0);
			//每页显示条数
			query.setMaxResults(2);
			//查询并得到返回结果
			List list = query.getResultList(); //得到集合返回类型
			for (Object object : list) {
				System.out.println(object);
			}
			tx.commit();
		} catch (Exception e) {
			// 回滚事务
			tx.rollback();
			e.printStackTrace();
		} finally {
			// 释放资源
			em.close();
		}
	}
```



#### **条件查询**

```java
//条件查询
	@Test
	public void findCondition () {
		EntityManager em = null;
		EntityTransaction tx = null;
		try {
			//获取实体管理对象
			em = JPAUtil.getEntityManager();
			//获取事务对象
			tx = em.getTransaction();
			tx.begin();
			//创建query对象
			String jpql = "from Customer where custName like ? ";
			Query query = em.createQuery(jpql);
			//对占位符赋值，从1开始
			query.setParameter(1, "传智播客%");
			//查询并得到返回结果
			Object object = query.getSingleResult(); //得到唯一的结果集对象
			System.out.println(object);
			tx.commit();
		} catch (Exception e) {
			// 回滚事务
			tx.rollback();
			e.printStackTrace();
		} finally {
			// 释放资源
			em.close();
		}
	}
```



#### **倒叙查询**

```java
//根据客户id倒序查询所有客户
	//查询所有客户
	@Test
	public void testOrder() {
		EntityManager em = null;
		EntityTransaction tx = null;
		try {
			//获取实体管理对象
			em = JPAUtil.getEntityManager();
			//获取事务对象
			tx = em.getTransaction();
			tx.begin();
			// 创建query对象
			String jpql = "from Customer order by custId desc";
			Query query = em.createQuery(jpql);
			// 查询并得到返回结果
			List list = query.getResultList(); // 得到集合返回类型
			for (Object object : list) {
				System.out.println(object);
			}
			tx.commit();
		} catch (Exception e) {
			// 回滚事务
			tx.rollback();
			e.printStackTrace();
		} finally {
			// 释放资源
			em.close();
		}
	}
```

#### **统计查询**

```java
//统计查询
	@Test
	public void findCount() {
		EntityManager em = null;
		EntityTransaction tx = null;
		try {
			//获取实体管理对象
			em = JPAUtil.getEntityManager();
			//获取事务对象
			tx = em.getTransaction();
			tx.begin();
			// 查询全部客户
			// 1.创建query对象
			String jpql = "select count(custId) from Customer";
			Query query = em.createQuery(jpql);
			// 2.查询并得到返回结果
			Object count = query.getSingleResult(); // 得到集合返回类型
			System.out.println(count);
			tx.commit();
		} catch (Exception e) {
			// 回滚事务
			tx.rollback();
			e.printStackTrace();
		} finally {
			// 释放资源
			em.close();
		}
	}
```



## **第二天**

```
SpringDataJpa第二天
	orm思想，hibernate，JPA的相关操作
	
* SpringDataJpa

第一 springDataJpa的概述

第二 springDataJpa的入门操作
	案例：客户的基本CRUD
	i.搭建环境
		创建工程导入坐标
		配置spring的配置文件（配置spring Data jpa的整合）
		编写实体类（Customer），使用jpa注解配置映射关系
	ii.编写一个符合springDataJpa的dao层接口
		* 只需要编写dao层接口，不需要编写dao层接口的实现类
		* dao层接口规范
			1.需要继承两个接口（JpaRepository，JpaSpecificationExecutor）
			2.需要提供响应的泛型
	
	*   findOne（id） ：根据id查询
		save(customer):保存或者更新（依据：传递的实体类对象中，是否包含id属性）
		delete（id） ：根据id删除
		findAll() : 查询全部

第三 springDataJpa的运行过程和原理剖析
	1.通过JdkDynamicAopProxy的invoke方法创建了一个动态代理对象
	2.SimpleJpaRepository当中封装了JPA的操作（借助JPA的api完成数据库的CRUD）
	3.通过hibernate完成数据库操作（封装了jdbc）


第四 复杂查询
	i.借助接口中的定义好的方法完成查询
		findOne(id):根据id查询
	ii.jpql的查询方式
		jpql ： jpa query language  （jpq查询语言）
		特点：语法或关键字和sql语句类似
			查询的是类和类中的属性
			
		* 需要将JPQL语句配置到接口方法上
			1.特有的查询：需要在dao接口上配置方法
			2.在新添加的方法上，使用注解的形式配置jpql查询语句
			3.注解 ： @Query

	iii.sql语句的查询
			1.特有的查询：需要在dao接口上配置方法
			2.在新添加的方法上，使用注解的形式配置sql查询语句
			3.注解 ： @Query
				value ：jpql语句 | sql语句
				nativeQuery ：false（使用jpql查询） | true（使用本地查询：sql查询）
					是否使用本地查询
					
	iiii.方法名称规则查询
```



#### **Spring Data JPA 与 JPA和hibernate之间的关系**

JPA是一套规范，内部是有接口和抽象类组成的。hibernate是一套成熟的ORM框架，而且Hibernate实现了JPA规范，所以也可以称hibernate为JPA的一种实现方式，我们使用JPA的API编程，意味着站在更高的角度上看待问题（面向接口编程）

Spring Data JPA是Spring提供的一套对JPA操作更加高级的封装，是在JPA规范下的专门用来进行数据持久化的解决方案。



#### 使用JPA注解配置映射关系

```java
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 *      * 所有的注解都是使用JPA的规范提供的注解，
 *      * 所以在导入注解包的时候，一定要导入javax.persistence下的
 */
@Entity //声明实体类
@Table(name="cst_customer") //建立实体类和表的映射关系
public class Customer {
    
    @Id//声明当前私有属性为主键
    @GeneratedValue(strategy=GenerationType.IDENTITY) //配置主键的生成策略
    @Column(name="cust_id") //指定和表中cust_id字段的映射关系
    private Long custId;
    
    @Column(name="cust_name") //指定和表中cust_name字段的映射关系
    private String custName;
    
    @Column(name="cust_source")//指定和表中cust_source字段的映射关系
    private String custSource;
    
    @Column(name="cust_industry")//指定和表中cust_industry字段的映射关系
    private String custIndustry;
    
    @Column(name="cust_level")//指定和表中cust_level字段的映射关系
    private String custLevel;
    
    @Column(name="cust_address")//指定和表中cust_address字段的映射关系
    private String custAddress;
    
    @Column(name="cust_phone")//指定和表中cust_phone字段的映射关系
    private String custPhone;
    
	// get set方法
}

```

#### 编写符合Spring Data JPA规范的Dao层接口

Spring Data JPA是spring提供的一款对于数据访问层（Dao层）的框架，使用Spring Data JPA，只需要按照框架的规范提供dao接口，不需要实现类就可以完成数据库的增删改查、分页查询等方法的定义，极大的简化了我们的开发过程。

在Spring Data JPA中，对于定义符合规范的Dao层接口，我们只需要遵循以下几点就可以了：

***\*1.创建一个Dao层接口，并实现JpaRepository和JpaSpecificationExecutor\****

 **\*2.提供相应的泛型\****

```java
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.itcast.entity.Customer;

/**
 * JpaRepository<实体类类型，主键类型>：用来完成基本CRUD操作
 * JpaSpecificationExecutor<实体类类型>：用于复杂查询（分页等查询操作）
 */
public interface CustomerDao extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
}
```

####  **完成基本CRUD操作**

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class CustomerDaoTest {

    @Autowired
    private CustomerDao customerDao;
    
    /**
     * 保存客户：调用save(obj)方法
     */
    @Test
    public void testSave() {
        Customer c = new Customer();
        c.setCustName("传智播客");
        customerDao.save(c);
    }
    
    /**
     * 修改客户：调用save(obj)方法
     *      对于save方法的解释：如果执行此方法是对象中存在id属性，即为更新操作会先根据id查询，再更新    
     *                      如果执行此方法中对象中不存在id属性，即为保存操作
     *          
     */
    @Test
    public void testUpdate() {
        //根据id查询id为1的客户
        Customer customer = customerDao.findOne(1l);
        //修改客户名称
        customer.setCustName("传智播客顺义校区");
        //更新
        customerDao.save(customer);
    }
    
    /**
     * 根据id删除：调用delete(id)方法
     */
    @Test
    public void testDelete() {
        customerDao.delete(1l);
    }
    
    /**
     * 根据id查询：调用findOne(id)方法
     */
    @Test
    public void testFindById() {
        Customer customer = customerDao.findOne(2l);
        System.out.println(customer);
    }
    
    /**
     * getOne的用法
     *   需要在实体类上加@Proxy(lazy = false)注解
     *   或者 在测试类上加上@Transactional
     *
     *   因为getOne实际em.getReference() 延迟加载
     */
    @Test
    @Transactional
    public void findTest() {
        Customer one = customerDao.getOne(1L);
        System.out.println(one);
    }
}
```



#### Spring Data Jpa的查询方式

1. #### ***继承JpaRepository后的方法列表***

   - ***继承JpaSpecificationExecutor的方法列表***
   - ***继承JpaRepository后的方法列表***

2. ***使用JPQL查询***

   使用Spring Data JPA提供的查询方法已经可以解决大部分的应用场景，但是对于某些业务来说，我们还需要灵活的构造查询条件，这时就可以使用@Query注解，结合JPQL的语句方式完成查询

   **@Query 注解**的使用非常简单，只需在方法上面标注该注解，同时提供一个JPQL查询语句即可

    ```java
   public interface CustomerDao extends JpaRepository<Customer, Long>,JpaSpecificationExecutor<Customer> {    
       //@Query 使用jpql的方式查询。
       @Query(value="from Customer") Customer是表对用的类名
       public List<Customer> findAllCustomer();
       
       //@Query 使用jpql的方式查询。?1代表参数的占位符，其中1对应方法中的参数索引
       @Query(value="from Customer where custName = ?1")
       public Customer findCustomer(String custName);
   }
    ```

   此外，也可以通过**使用 @Query 来执行一个更新操作**，为此，我们需要在使用 @Query 的同时，用 **@Modifying** 来将该操作标识为修改查询，这样框架最终会生成一个更新的操作，而非查询

    ```java
   @Query(value="update Customer set custName = ?1 where custId = ?2")
   @Modifying
   public void updateCustomer(String custName,Long custId);
    ```

3、**使用SQL查询**

​	**需要加上nativeQuery**

```java
/**
 * nativeQuery : 使用本地sql的方式查询
*/
@Query(value="select * from cst_customer",nativeQuery=true)
public void findSql();
```

4、**方法命名规则查询**

方法命名规则查询就是根据方法的名字，就能创建查询。只需要按照Spring Data JPA提供的方法命名规则定义方法的名称，就可以完成查询工作。Spring Data JPA在程序执行的时候会根据方法名称进行解析，并自动生成查询语句进行查询 

**按照Spring Data JPA 定义的规则，查询方法以findBy开头，涉及条件查询时，条件的属性用条件关键字连接，要注意的是：条件属性首字母需大写。框架在进行方法名解析时，会先把方法名多余的前缀截取掉，然后对剩下部分进行解析。**

```java
//方法命名方式查询（根据客户名称查询客户）
public Customer findByCustName(String custName);
```

| ***\*Keyword\**** | ***\*Sample\****                        | ***\*JPQL\****                                               |      |      |
| ----------------- | --------------------------------------- | ------------------------------------------------------------ | ---- | ---- |
| And               | findByLastnameAndFirstname              | … where x.lastname = ?1 and x.firstname = ?2                 |      |      |
| Or                | findByLastnameOrFirstname               | … where x.lastname = ?1 or x.firstname = ?2                  |      |      |
| Is,Equals         | findByFirstnameIs,findByFirstnameEquals | … where x.firstname = ?1                                     |      |      |
| Between           | findByStartDateBetween                  | … where x.startDate between ?1 and ?2                        |      |      |
| LessThan          | findByAgeLessThan                       | … where x.age < ?1                                           |      |      |
| LessThanEqual     | findByAgeLessThanEqual                  | … where x.age ⇐ ?1                                           |      |      |
| GreaterThan       | findByAgeGreaterThan                    | … where x.age > ?1                                           |      |      |
| GreaterThanEqual  | findByAgeGreaterThanEqual               | … where x.age >= ?1                                          |      |      |
| After             | findByStartDateAfter                    | … where x.startDate > ?1                                     |      |      |
| Before            | findByStartDateBefore                   | … where x.startDate < ?1                                     |      |      |
| IsNull            | findByAgeIsNull                         | … where x.age is null                                        |      |      |
| IsNotNull,NotNull | findByAge(Is)NotNull                    | … where x.age not null                                       |      |      |
| Like              | findByFirstnameLike                     | … where x.firstname like ?1                                  |      |      |
| NotLike           | findByFirstnameNotLike                  | … where x.firstname not like ?1                              |      |      |
| StartingWith      | findByFirstnameStartingWith             | … where x.firstname like ?1 (parameter bound with appended %) |      |      |
| EndingWith        | findByFirstnameEndingWith               | … where x.firstname like ?1 (parameter bound with prepended %) |      |      |
| Containing        | findByFirstnameContaining               | … where x.firstname like ?1 (parameter bound wrapped in %)   |      |      |
| OrderBy           | findByAgeOrderByLastnameDesc            | … where x.age = ?1 order by x.lastname desc                  |      |      |
| Not               | findByLastnameNot                       | … where x.lastname <> ?1                                     |      |      |
| In                | findByAgeIn(Collection ages)            | … where x.age in ?1                                          |      |      |
| NotIn             | findByAgeNotIn(Collection age)          | … where x.age not in ?1                                      |      |      |
| TRUE              | findByActiveTrue()                      | … where x.active = true                                      |      |      |
| FALSE             | findByActiveFalse()                     | … where x.active = false                                     |      |      |
| IgnoreCase        | findByFirstnameIgnoreCase               | … where UPPER(x.firstame) = UPPER(?1)                        |      |      |

 

## 第三天

第一 Specifications动态查询

	JpaSpecificationExecutor 方法列表
	
		T findOne(Specification<T> spec);  //查询单个对象
		List<T> findAll(Specification<T> spec);  //查询列表
	
		//查询全部，分页
		//pageable：分页参数
		//返回值：分页pageBean（page：是springdatajpa提供的）
		Page<T> findAll(Specification<T> spec, Pageable pageable);
	
		//查询列表
		//Sort：排序参数
		List<T> findAll(Specification<T> spec, Sort sort);
	
		long count(Specification<T> spec);//统计查询
		
	* Specification ：查询条件
		自定义我们自己的Specification实现类
			实现
	        //root：查询的根对象（查询的任何属性都可以从根对象中获取）
	        //CriteriaQuery：顶层查询对象，自定义查询方式（了解：一般不用）
	        //CriteriaBuilder：查询的构造器，封装了很多的查询条件
	        Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb); //封装查询条件

第二 多表之间的关系和操作多表的操作步骤

	表关系
		一对一
		一对多：
			一的一方：主表
			多的一方：从表
			外键：需要再从表上新建一列作为外键，他的取值来源于主表的主键
		多对多：
			中间表：中间表中最少应该由两个字段组成，这两个字段做为外键指向两张表的主键，又组成了联合主键
	
	讲师对学员：一对多关系
			
	实体类中的关系
		包含关系：可以通过实体类中的包含关系描述表关系
		继承关系
	
	分析步骤
		1.明确表关系
		2.确定表关系（描述 外键|中间表）
		3.编写实体类，再实体类中描述表关系（包含关系）
		4.配置映射关系

第三 完成多表操作

	i.一对多操作
		案例：客户和联系人的案例（一对多关系）
			客户：一家公司
			联系人：这家公司的员工
		
			一个客户可以具有多个联系人
			一个联系人从属于一家公司
			
		分析步骤
			1.明确表关系
				一对多关系
			2.确定表关系（描述 外键|中间表）
				主表：客户表
				从表：联系人表
					* 再从表上添加外键
			3.编写实体类，再实体类中描述表关系（包含关系）
				客户：再客户的实体类中包含一个联系人的集合
				联系人：在联系人的实体类中包含一个客户的对象
			4.配置映射关系
				* 使用jpa注解配置一对多映射关系
	
		级联：
			操作一个对象的同时操作他的关联对象
			
			级联操作：
				1.需要区分操作主体
				2.需要在操作主体的实体类上，添加级联属性（需要添加到多表映射关系的注解上）
				3.cascade（配置级联）
			
			级联添加，
				案例：当我保存一个客户的同时保存联系人
			级联删除
				案例：当我删除一个客户的同时删除此客户的所有联系人
				
	ii.多对多操作
		案例：用户和角色（多对多关系）
			用户：
			角色：
	
		分析步骤
			1.明确表关系
				多对多关系
			2.确定表关系（描述 外键|中间表）
				中间间表
			3.编写实体类，再实体类中描述表关系（包含关系）
				用户：包含角色的集合
				角色：包含用户的集合
			4.配置映射关系
			
	iii.多表的查询
		1.对象导航查询
			查询一个对象的同时，通过此对象查询他的关联对象
			案例：客户和联系人
			从一方查询多方
				* 默认：使用延迟加载（****）
				
			从多方查询一方
				* 默认：使用立即加载



#### **Specifications 动态查询** 

有时我们在查询某个实体的时候，给定的条件是不固定的，这时就需要动态构建相应的查询语句，在Spring Data JPA中可以通过JpaSpecificationExecutor接口查询。相比JPQL,其优势是类型安全,更加的面向对象

```java
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

/**
 *	JpaSpecificationExecutor中定义的方法
 **/
 public interface JpaSpecificationExecutor<T> {
   	//根据条件查询一个对象
 	T findOne(Specification<T> spec);	
   	//根据条件查询集合
 	List<T> findAll(Specification<T> spec);
   	//根据条件分页查询
 	Page<T> findAll(Specification<T> spec, Pageable pageable);
   	//排序查询查询
 	List<T> findAll(Specification<T> spec, Sort sort);
   	//统计查询
 	long count(Specification<T> spec);
}
```

**对于JpaSpecificationExecutor，这个接口基本是围绕着Specification接口来定义的。我们可以简单的理解为，Specification构造的就是查询条件。**

Specification接口中只定义了如下一个方法：

```java
//构造查询条件
    /**
    *	root	：Root接口，代表查询的根对象，可以通过root获取实体中的属性
    *	query	：代表一个顶层查询对象，用来自定义查询
    *	cb		：用来构建查询，此对象里有很多条件方法
    **/
 public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb);
```

#### **使用Specifications完成条件查询**

```java
//依赖注入customerDao
@Autowired
private CustomerDao customerDao;	
@Test
public void testSpecifications() {
    //使用匿名内部类的方式，创建一个Specification的实现类，并实现toPredicate方法
    Specification <Customer> spec = new Specification<Customer>() {
        public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            //cb:构建查询，添加查询方式   like：模糊匹配
            //root：从实体Customer对象中按照custName属性进行查询
            return cb.like(root.get("custName").as(String.class), "传智播客%");
        }
    };
    Customer customer = customerDao.findOne(spec);
    System.out.println(customer);
}
```

#### **基于Specifications的分页查询**

```java
    @Test
	public void testPage() {
		//构造查询条件
		Specification<Customer> spec = new Specification<Customer>() {
			public Predicate toPredicate(Root<Customer> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				return cb.like(root.get("custName").as(String.class), "传智%");
			}
		};
		
		/**
		 * 构造分页参数
		 * 		Pageable : 接口
		 * 			PageRequest实现了Pageable接口，调用构造方法的形式构造
		 * 				第一个参数：页码（从0开始）
		 * 				第二个参数：每页查询条数
		 */
		Pageable pageable = new PageRequest(0, 5);
		
		/**
		 * 分页查询，封装为Spring Data Jpa 内部的page bean
		 * 		此重载的findAll方法为分页方法需要两个参数
		 * 			第一个参数：查询条件Specification
		 * 			第二个参数：分页参数
		 */
		Page<Customer> page = customerDao.findAll(spec,pageable);
		
	}
```

对于Spring Data JPA中的分页查询，是其内部自动实现的封装过程，返回的是一个Spring Data JPA提供的pageBean对象。其中的方法说明如下：

```java
//获取总页数
int getTotalPages();
//获取总记录数	
long getTotalElements();
//获取列表数据
List<T> getContent();
```

**方法对应关系**

| 方法名称                    | Sql对应关系          |
| --------------------------- | -------------------- |
| equle                       | filed = value        |
| gt（greaterThan ）          | filed > value        |
| lt（lessThan ）             | filed < value        |
| ge（greaterThanOrEqualTo ） | filed >= value       |
| le（ lessThanOrEqualTo）    | filed <= value       |
| notEqule                    | filed != value       |
| like                        | filed like value     |
| notLike                     | filed not like value |
|                             |                      |

#### **表之间关系的划分**

数据库中多表之间存在着三种关系，如图所示。

 

![img](file:///C:\Users\KONGXI~1\AppData\Local\Temp\ksohtml71788\wps1.jpg)



#### JPA框架中表关系的分析步骤

在实际开发中，我们数据库的表难免会有相互的关联关系，在操作表的时候就有可能会涉及到多张表的操作。而在这种实现了ORM思想的框架中（如JPA），可以让我们通过操作实体类就实现对数据库表的操作。所以今天我们的学习重点是：掌握配置实体之间的关联关系。

***\*第一步：首先确定两张表之间的关系。\****

- 如果关系确定错了，后面做的所有操作就都不可能正确。 

***\*第二步：在数据库中实现两张表的关系\**** 

***\*第三步：在实体类中描述出两个实体的关系\****

***\*第四步：配置出实体类和数据库表的关系映射（重点）\****



#### **JPA中多一对多**

```
示例分析：
我们采用的示例为客户和联系人。
 客户：指的是一家公司，我们记为A。
 联系人：指的是A公司中的员工。
 在不考虑兼职的情况下，公司和员工的关系即为一对多。
```

**表关系建立**

在一对多关系中，我们习惯把一的一方称之为主表，把多的一方称之为从表。在数据库中建立一对多的关系，需要使用数据库的外键约束。

什么是外键？

指的是从表中有一列，取值参照主表的主键，这一列就是外键。

 

一对多数据库关系的建立，如下图所示 

 

![img](file:///C:\Users\KONGXI~1\AppData\Local\Temp\ksohtml71788\wps2.jpg) 

 **实体类关系建立以及映射配置**

在实体类中，由于客户是少的一方，它应该包含多个联系人，所以实体类要体现出客户中有多个联系人的信息，代码如下：

```java
/**
 * 客户的实体类
 * 明确使用的注解都是JPA规范的
 * 所以导包都要导入javax.persistence包下的
 */
@Entity//表示当前类是一个实体类
@Table(name="cst_customer")//建立当前实体类和表之间的对应关系
public class Customer implements Serializable {
	
	@Id//表明当前私有属性是主键
	@GeneratedValue(strategy=GenerationType.IDENTITY)//指定主键的生成策略
	@Column(name="cust_id")//指定和数据库表中的cust_id列对应
	private Long custId;
	@Column(name="cust_name")//指定和数据库表中的cust_name列对应
	private String custName;
	@Column(name="cust_source")//指定和数据库表中的cust_source列对应
	private String custSource;
	@Column(name="cust_industry")//指定和数据库表中的cust_industry列对应
	private String custIndustry;
	@Column(name="cust_level")//指定和数据库表中的cust_level列对应
	private String custLevel;
	@Column(name="cust_address")//指定和数据库表中的cust_address列对应
	private String custAddress;
	@Column(name="cust_phone")//指定和数据库表中的cust_phone列对应
	private String custPhone;
	
    //配置客户和联系人的一对多关系
  	@OneToMany(targetEntity=LinkMan.class)
	@JoinColumn(name="lkm_cust_id",referencedColumnName="cust_id")
	private Set<LinkMan> linkmans = new HashSet<LinkMan>(0);
}
```

由于联系人是多的一方，在实体类中要体现出，每个联系人只能对应一个客户，代码如下：

```java
/**
 * 联系人的实体类（数据模型）
 */
@Entity
@Table(name="cst_linkman")
public class LinkMan implements Serializable {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="lkm_id")
	private Long lkmId;
	@Column(name="lkm_name")
	private String lkmName;
	@Column(name="lkm_gender")
	private String lkmGender;
	@Column(name="lkm_phone")
	private String lkmPhone;
	@Column(name="lkm_mobile")
	private String lkmMobile;
	@Column(name="lkm_email")
	private String lkmEmail;
	@Column(name="lkm_position")
	private String lkmPosition;
	@Column(name="lkm_memo")
	private String lkmMemo;

	//多对一关系映射：多个联系人对应客户
	@ManyToOne(targetEntity=Customer.class)
	@JoinColumn(name="lkm_cust_id",referencedColumnName="cust_id")
	private Customer customer;//用它的主键，对应联系人表中的外键
}
```

**映射的注解说明**

```java
@OneToMany:
   	作用：建立一对多的关系映射
    属性：
    	targetEntityClass：指定多的多方的类的字节码
    	mappedBy：指定从表实体类中引用主表对象的名称。
    	cascade：指定要使用的级联操作
    	fetch：指定是否采用延迟加载
    	orphanRemoval：是否使用孤儿删除

@ManyToOne
    作用：建立多对一的关系
    属性：
    	targetEntityClass：指定一的一方实体类字节码
    	cascade：指定要使用的级联操作
    	fetch：指定是否采用延迟加载
    	optional：关联是否可选。如果设置为false，则必须始终存在非空关系。

@JoinColumn
     作用：用于定义主键字段和外键字段的对应关系。
     属性：
    	name：指定外键字段的名称
    	referencedColumnName：指定引用主表的主键字段名称
    	unique：是否唯一。默认值不唯一
    	nullable：是否允许为空。默认值允许。
    	insertable：是否允许插入。默认值允许。
    	updatable：是否允许更新。默认值允许。
    	columnDefinition：列的定义信息。
```

**一对多的操作**

- 添加

  ```
  @RunWith(SpringJUnit4ClassRunner.class)
  @ContextConfiguration(locations="classpath:applicationContext.xml")
  public class OneToManyTest {
  
  	@Autowired
  	private CustomerDao customerDao;
  	
  	@Autowired
  	private LinkManDao linkManDao;
  
  	/**
  	 * 保存操作
  	 * 需求:
  	 * 	保存一个客户和一个联系人
  	 * 要求：
  	 * 	创建一个客户对象和一个联系人对象
  	 *  建立客户和联系人之间关联关系（双向一对多的关联关系）
  	 *  先保存客户，再保存联系人
  	 * 问题：
  	 *		当我们建立了双向的关联关系之后，先保存主表，再保存从表时：
  	 *		会产生2条insert和1条update.
  	 * 		而实际开发中我们只需要2条insert。
  	 *  
  	 */
  	@Test
  	@Transactional  //开启事务
  	@Rollback(false)//设置为不回滚
  	public void testAdd() {
  		Customer c = new Customer();
  		c.setCustName("TBD云集中心");
  		c.setCustLevel("VIP客户");
  		c.setCustSource("网络");
  		c.setCustIndustry("商业办公");
  		c.setCustAddress("昌平区北七家镇");
  		c.setCustPhone("010-84389340");
  		
  		LinkMan l = new LinkMan();
  		l.setLkmName("TBD联系人");
  		l.setLkmGender("male");
  		l.setLkmMobile("13811111111");
  		l.setLkmPhone("010-34785348");
  		l.setLkmEmail("98354834@qq.com");
  		l.setLkmPosition("老师");
  		l.setLkmMemo("还行吧");
  
  		c.getLinkMans().add(l);
  		l.setCustomer(c);
  		customerDao.save(c);
  		linkManDao.save(l);
  	}
  }
  ```

  **通过保存的案例，我们可以发现在设置了双向关系之后，会发送两条insert语句，一条多余的update语句，那我们的解决是思路很简单，就是一的一方放弃维护权**

  ```java
  /**
  	 *放弃外键维护权的配置将如下配置改为
  	 */
  //@OneToMany(targetEntity=LinkMan.class)
  //@JoinColumn(name="lkm_cust_id",referencedColumnName="cust_id")	
  //设置为
  	@OneToMany(mappedBy="customer")
  ```

  **删除**

  ```java
  @Autowired
  private CustomerDao customerDao;
  
  @Test
  @Transactional
  @Rollback(false)//设置为不回滚
  public void testDelete() {
      customerDao.delete(1l);
  }
  ```

  **删除操作的说明如下：**

  **删除从表数据：可以随时任意删除。**

  

  **删除主表数据：** 

  有从表数据

  -  1、在默认情况下，它会把外键字段置为null，然后删除主表数据。如果在数据库的表         结构上，外键字段有非空约束，默认情况就会报错了。

  -  2、如果配置了放弃维护关联关系的权利，则不能删除（与外键字段是否允许为null，		没有关系）因为在删除时，它根本不会去更新从表的外键字段了。

  -  3、如果还想删除，使用级联删除引用

   **没有从表数据引用：随便删**

  **在实际开发中，级联删除请慎用！(在一对多的情况下)**

   

  **级联操作**

  - **级联操作：指操作一个对象同时操作它的关联对象**

  - **使用方法：只需要在操作主体的注解上配置cascade**

  ```java
  	/**
  	 * cascade:配置级联操作
  	 * 		CascadeType.MERGE	级联更新
  	 * 		CascadeType.PERSIST	级联保存：
  	 * 		CascadeType.REFRESH 级联刷新：
  	 * 		CascadeType.REMOVE	级联删除：
  	 * 		CascadeType.ALL		包含所有
  	 */
  	@OneToMany(mappedBy="customer",cascade=CascadeType.ALL)
  ```



#### **JPA的多对多**

```java
示例分析
 我们采用的示例为用户和角色。
 用户：指的是咱们班的每一个同学。
 角色：指的是咱们班同学的身份信息。

 比如A同学，它是我的学生，其中有个身份就是学生，还是家里的孩子，那么他还有个身份是子女。
 同时B同学，它也具有学生和子女的身份。
 那么任何一个同学都可能具有多个身份。同时学生这个身份可以被多个同学所具有。
 所以我们说，用户和角色之间的关系是多对多。
```

**表关系简历**

多对多的表关系建立靠的是中间表，其中用户表和中间表的关系是一对多，角色表和中间表的关系也是一对多，如下图所示：

![img](file:///C:\Users\KONGXI~1\AppData\Local\Temp\ksohtml71788\wps3.jpg)



**实体类关系建立以及映射配置**

一个用户可以具有多个角色，所以在用户实体类中应该包含多个角色的信息

```java
/**
 * 用户的数据模型
 */
@Entity
@Table(name="sys_user")
public class SysUser implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="user_id")
	private Long userId;
	@Column(name="user_code")
	private String userCode;
	@Column(name="user_name")
	private String userName;
	@Column(name="user_password")
	private String userPassword;
	@Column(name="user_state")
	private String userState;
	
	//多对多关系映射
	@ManyToMany(mappedBy="users")
	private Set<SysRole> roles = new HashSet<SysRole>(0);
	
}

```

一个角色可以赋予多个用户，所以在角色实体类中应该包含多个用户的信息

```java
/**
 * 角色的数据模型
 */
@Entity
@Table(name="sys_role")
public class SysRole implements Serializable {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="role_id")
	private Long roleId;
	@Column(name="role_name")
	private String roleName;
	@Column(name="role_memo")
	private String roleMemo;
	
	//多对多关系映射
	@ManyToMany
	@JoinTable(name="user_role_rel",//中间表的名称
			  //中间表user_role_rel字段关联sys_role表的主键字段role_id
			  joinColumns={@JoinColumn(name="role_id",referencedColumnName="role_id")},
			  //中间表user_role_rel的字段关联sys_user表的主键user_id
			  inverseJoinColumns={@JoinColumn(name="user_id",referencedColumnName="user_id")}
	)
	private Set<SysUser> users = new HashSet<SysUser>(0);
	
}
```



**映射的注解说明**

```java
@ManyToMany
	作用：用于映射多对多关系
	属性：
		cascade：配置级联操作。
		fetch：配置是否采用延迟加载。
    	targetEntity：配置目标的实体类。映射多对多的时候不用写。

@JoinTable
    作用：针对中间表的配置
    属性：
    	nam：配置中间表的名称
    	joinColumns：中间表的外键字段关联当前实体类所对应表的主键字段			  			
    	inverseJoinColumn：中间表的外键字段关联对方表的主键字段
    	
@JoinColumn
    作用：用于定义主键字段和外键字段的对应关系。
    属性：
    	name：指定外键字段的名称
    	referencedColumnName：指定引用主表的主键字段名称
    	unique：是否唯一。默认值不唯一
    	nullable：是否允许为空。默认值允许。
    	insertable：是否允许插入。默认值允许。
    	updatable：是否允许更新。默认值允许。
    	columnDefinition：列的定义信息。

```



**多对多操作**

- 保存

  ```java
      @Autowired
  	private UserDao userDao;
  	
  	@Autowired
  	private RoleDao roleDao;
  	/**
  	 * 需求：
  	 * 	保存用户和角色
  	 * 要求：
  	 * 	创建2个用户和3个角色
  	 * 	让1号用户具有1号和2号角色(双向的)
  	 * 	让2号用户具有2号和3号角色(双向的)
  	 *  保存用户和角色
  	 * 问题：
  	 *  在保存时，会出现主键重复的错误，因为都是要往中间表中保存数据造成的。
  	 * 解决办法：
  	 * 	让任意一方放弃维护关联关系的权利
  	 */
  	@Test
  	@Transactional  //开启事务
  	@Rollback(false)//设置为不回滚
  	public void test1(){
  		//创建对象
  		SysUser u1 = new SysUser();
  		u1.setUserName("用户1");
  		SysRole r1 = new SysRole();
  		r1.setRoleName("角色1");
  		//建立关联关系
  		u1.getRoles().add(r1);
  		r1.getUsers().add(u1);
  		//保存
  		roleDao.save(r1);
  		userDao.save(u1);
  	}
  ```

  **在多对多（保存）中，如果双向都设置关系，意味着双方都维护中间表，都会往中间表插入数据，中间表的2个字段又作为联合主键，所以报错，主键重复，解决保存失败的问题：只需要在任意一方放弃对中间表的维护权即可，推荐在被动的一方放弃，配置如下：**

  ```java
  //放弃对中间表的维护权，解决保存中主键冲突的问题
  @ManyToMany(mappedBy="roles")
  private Set<SysUser> users = new HashSet<SysUser>(0);
  ```

- 删除

  ```java
  @Autowired
  private UserDao userDao;
  /**
  	 * 删除操作
  	 * 	在多对多的删除时，双向级联删除根本不能配置
  	 * 禁用
  	 *	如果配了的话，如果数据之间有相互引用关系，可能会清空所有数据
  	 */
  @Test
  @Transactional
  @Rollback(false)//设置为不回滚
  public void testDelete() {
      userDao.delete(1l);
  }
  ```

  

#### **Spring Data Jpa 的多表查询**

**对象导航查询**

对象图导航检索方式是根据已经加载的对象，导航到他的关联对象。它利用类与类之间的关系来检索对象。例如：我们通过ID查询方式查出一个客户，可以调用Customer类中的getLinkMans()方法来获取该客户的所有联系人。对象导航查询的使用要求是：两个对象之间必须存在关联关系。

 **查询一个客户，获取该客户下的所有联系人**

```java
@Autowired
private CustomerDao customerDao;

@Test
//由于是在java代码中测试，为了解决no session问题，将操作配置到同一个事务中
@Transactional 
public void testFind() {
    Customer customer = customerDao.findOne(5l);
    Set<LinkMan> linkMans = customer.getLinkMans();//对象导航查询
    for(LinkMan linkMan : linkMans) {
        System.out.println(linkMan);
    }
}

```



**查询一个联系人，获取该联系人的所有客户**

```java
@Autowired
private LinkManDao linkManDao;

@Test
public void testFind() {
    LinkMan linkMan = linkManDao.findOne(4l);
    Customer customer = linkMan.getCustomer(); //对象导航查询
    System.out.println(customer);
}
```



对象导航查询的问题分析

***\*问题1：我们查询客户时，要不要把联系人查询出来？\****

分析：如果我们不查的话，在用的时候还要自己写代码，调用方法去查询。如果我们查出来的，不使用时又会白白的浪费了服务器内存。

**解决：采用延迟加载的思想。通过配置的方式来设定当我们在需要使用时，发起真正的查询。**

```java
/**
	 * 在客户对象的@OneToMany注解中添加fetch属性
	 * 		FetchType.EAGER	：立即加载
	 * 		FetchType.LAZY	：延迟加载
	 */
@OneToMany(mappedBy="customer",fetch=FetchType.EAGER)
private Set<LinkMan> linkMans = new HashSet<>(0);
```



***\*问题2：我们查询联系人时，要不要把客户查询出来？\****

分析：例如：查询联系人详情时，肯定会看看该联系人的所属客户。如果我们不查的话，在用的时候还要自己写代码，调用方法去查询。如果我们查出来的话，一个对象不会消耗太多的内存。而且多数情况下我们都是要使用的。

**解决： 采用立即加载的思想。通过配置的方式来设定，只要查询从表实体，就把主表实体对象同时查出来**

```java
	/**
	 * 在联系人对象的@ManyToOne注解中添加fetch属性
	 * 		FetchType.EAGER	：立即加载
	 * 		FetchType.LAZY	：延迟加载
	 */
	@ManyToOne(targetEntity=Customer.class,fetch=FetchType.EAGER)
	@JoinColumn(name="cst_lkm_id",referencedColumnName="cust_id")
	private Customer customer;
```

**使用Specification查询**

```java
	/**
	 * Specification的多表查询
	 */
	@Test
	public void testFind() {
		Specification<LinkMan> spec = new Specification<LinkMan>() {
			public Predicate toPredicate(Root<LinkMan> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				//Join代表链接查询，通过root对象获取
				//创建的过程中，第一个参数为关联对象的属性名称，第二个参数为连接查询的方式（left，inner，right）
				//JoinType.LEFT : 左外连接,JoinType.INNER：内连接,JoinType.RIGHT：右外连接
				Join<LinkMan, Customer> join = root.join("customer",JoinType.INNER);
				return cb.like(join.get("custName").as(String.class),"传智播客1");
			}
		};
		List<LinkMan> list = linkManDao.findAll(spec);
		for (LinkMan linkMan : list) {
			System.out.println(linkMan);
		}
	}
```











#### 个别总结

spring data jpa 1.0 和2.0部分方法存在差异，因此在使用的时候需要注意，否则，1.0能够跑起来的程序，2.0版本可能是会报错的。这里的1.0版本和2.0版本分别值得是我测试的版本，分别为1.9。0，2.0版本为2.1.12.其他版本差异可以参考官方文档。

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



```java
@Column(name = "partName_en", nullable = false, columnDefinition = "varchar(30)")
nullable 未生效

@Column(name = "ecu_type", nullable = false, columnDefinition = "varchar(20)")
@NotNull
生效 成功映射到数据库约束
```





