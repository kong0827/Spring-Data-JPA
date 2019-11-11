package com.jpa.test;

import com.jpa.dao.CustomerDao;
import org.springframework.beans.factory.annotation.Autowired;

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
public class CustomerTest {

    @Autowired
    private CustomerDao customerDao;

    /**
     * 根据用户id 查找用户
     * 要求：
     *      1）findById
     *      2）getOne
     */
    public void findCustomerById() {
    }

    /**
     * 查找所有用户
     * 要求：
     *      1、findAll
     *      2、按照年龄排序
     *      3、不可以自己先查询出所有结果集
     */
    public void findAll() {

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
    public void updateNameByIdTest() {

    }

}
