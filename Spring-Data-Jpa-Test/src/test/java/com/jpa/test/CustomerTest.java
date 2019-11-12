package com.jpa.test;

import com.jpa.dao.CustomerDao;
import com.jpa.entity.Customer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @ClassName CustomerTest
 * @Description TODO
 * @Author kongxiangjin
 * @Date 2019/11/11 15:54
 * @Version 1.0
 *
 *
 * spring data jpa 版本：2.1.12.RELEASE
 * spring          版本：5.2.0.RELEASE
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@Transactional
public class CustomerTest {


    Logger logger= LoggerFactory.getLogger(CustomerTest.class);

    @Autowired
    private CustomerDao customerDao;

    /**
     * 根据用户id 查找用户
     * 要求：
     *      1）findById
     *
     *      2）getOne
     */

    @Test
    public void findCustomerById() {
        //加上get()后就会返回T类型，不加get的话返回的就是Optional
        //Customer customer = customerDao.findById(428).get();
        Optional<Customer> customer = customerDao.findById(428);
        logger.info(customer.get().toString());
    }

    @Test
    public void getOneCustomer() {
        //getOne()方法总是报错，说是需要在配置文件中添加配置，但是网上的配置都是基于SpringBoot的，Spring的配置尝试失败了
        Customer customer = customerDao.getOne(440);
        logger.info(customer.toString());
    }
    /**
     * 查找所有用户
     * 要求：
     *      1、findAll
     *      2、按照年龄排序
     *      3、不可以自己先查询出所有结果集
     */

    @Test
    public void findAll() {

        List<Customer> customers = customerDao.findAll();

        for (Customer customer:customers
             ) {
            logger.info(customer.toString());
        }
    }


    @Test
    public void findAllOrderById() {

        Sort sort=new Sort(Sort.Direction.DESC,"custId");
        List<Customer> customers = customerDao.findAll(sort);

        for (Customer customer:customers
        ) {
            logger.info(customer.toString());
        }
    }

    /**
     * 根据用户名查询用户
     * 要求：
     * 1、JPQL语法
     * 2、使用以下三种方式
     *      1）Query creation from method names
     *      2）Declare query at the query method using @Query
     *      3）Declare a native query at the query method using @Query
     */
    public void jpqlTest() {


    }

    
    /**
     * 根据用户id更新用户姓名
     * 要求：
     *      1）发生异常时可以回滚
     */
    @Test
    @Transactional
    //关于配置事务，只是在测试类和测试方法上添加了事务配置，具体作用不明。
    public void updateNameByIdTest() {
        Customer customer=new Customer();
        customer.setCustId(440);
        customer.setCustName("没得感情");
        customerDao.save(customer);
    }

}
