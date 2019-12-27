package com.kxj.test;

import com.kxj.entity.Customer;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * @author kxj
 * @date 2019/10/29 23:47
 * @Desc 测试JPA的保存, 保存一个客户到数据库中
 */
public class CustomerTest {
    /**
     * 操作步骤
     * 1、加载配置文件创建工厂(实体管理类工厂)
     * 2、通过实体管理类工厂获取实体管理器
     * 3、获取事务对象，开启事务
     * 4、完成保存操作
     * 5、提交事务
     * 6、释放资源
     */
    @Test
    public void saveCustomerTest() {
        // jpa是persistence.xml中persistence-unit标签的name值
        EntityManagerFactory entityManagerFactory =
                Persistence.createEntityManagerFactory("myJpa");
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Customer customer = new Customer();
        customer.setCustName("Spring Data JPA");

        entityManager.persist(customer);

        transaction.commit();

        entityManager.close();
        entityManagerFactory.close();
    }
}
