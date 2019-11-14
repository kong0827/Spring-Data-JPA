package com.jpa.dao;

import com.jpa.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @ClassName CustomerDao
 * @Description TODO
 * @Author kongxiangjin
 * @Date 2019/11/11 15:57
 * @Version 1.0
 **/
public interface CustomerDao extends JpaRepository<Customer,Integer>, JpaSpecificationExecutor<Customer> {

    //@Query 使用jpql的方式查询。
    @Query(value="from Customer")
     List<Customer> findAllCustomer();

    //@Query 使用jpql的方式查询。?1代表参数的占位符，其中1对应方法中的参数索引
    @Query(value="from Customer c where custName = ?1")
     Customer findCustomer(String custName);

}
