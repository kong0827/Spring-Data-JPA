package com.kxj.test;

import com.kxj.dao.CustomerDao;
import com.kxj.entity.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName JpqlTest
 * @Description TODO
 * @Author kongxiangjin
 * @Date 2019/11/11 13:49
 * @Version 1.0
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class JpqlTest {

    @Autowired
    private CustomerDao customerDao;

    @Test
    public void findCustomerByCustName() {
        Customer customer = customerDao.findCustomerByCustName("杭州");
        System.out.println(customer);
    }

    @Test
    public void findCustomerByCustNameAndCustIdTest() {
        Customer customer = customerDao.findCustomerByCustNameAndCustId("杭州", 3l);
        System.out.println(customer);
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void updateCustomerByCusIdTest() {
        customerDao.updateCustomerByCusId("福州", 4l);
    }

    @Test
    public void findCustomerByCustNameBySQLTest() {
        Customer customer = customerDao.findCustomerByCustNameBySQL("杭州");
        System.out.println(customer);
    }


    // 模糊查询
    @Test
    public void findCustomersByCustNameLikeTest() {
        List<Customer> customers = customerDao.findCustomersByCustNameLike("福%");
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }

    // 模糊查询二
    @Test
    public void findCustomersTest() {
        List<Customer> customers = customerDao.findCustomers("福%");
        for (Customer customer : customers) {
            System.out.println(customer);
        }
    }
}
