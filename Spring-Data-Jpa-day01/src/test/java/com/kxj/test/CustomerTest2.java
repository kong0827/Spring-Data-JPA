package com.kxj.test;

import com.kxj.entity.Customer;
import com.kxj.utils.JpaUtils;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * @author kxj
 * @date 2019/10/30 21:38
 * @Desc 测试JPA的工具类
 */
public class CustomerTest2 {
    @Test
    public void saveCustomerTest() {
        EntityManager entityManager = JpaUtils.getEntityMangager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        Customer customer = new Customer();
        customer.setCustName("Spring Data JPA");

        entityManager.persist(customer);

        transaction.commit();

        entityManager.close();
    }
}
