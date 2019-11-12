package com.jpa.dao;

import com.jpa.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @ClassName CustomerDao
 * @Description TODO
 * @Author kongxiangjin
 * @Date 2019/11/11 15:57
 * @Version 1.0
 **/
public interface CustomerDao extends JpaRepository<Customer,Integer>, JpaSpecificationExecutor<Customer> {
}
