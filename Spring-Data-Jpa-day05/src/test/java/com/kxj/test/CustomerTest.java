package com.kxj.test;

import com.kxj.dao.CustomerDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @ClassName CustomerTest
 * @Description TODO
 * @Author kongxiangjin
 * @Date 2019/11/11 13:32
 * @Version 1.0
 **/

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class CustomerTest {

    @Autowired
    private CustomerDao customerDao;

    // 根据id判断客户是否存在
    @Test
    public void existsByIdTest() {
        boolean isExit = customerDao.existsById(1l);
        System.out.println(isExit);
    }

    // 获得个数
    @Test
    public void countTest() {
        long count = customerDao.count();
        System.out.println(count);
    }


}
