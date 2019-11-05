package com.jpa.dao;

import com.jpa.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * CustomerDao继承的两个借口，第一个JpaReository实现的是简单的增删改查的操作，后面一个实现的是比较复杂的分页、多条件查询等
 */
public interface CustomerDao extends JpaRepository<Customer,Integer>, JpaSpecificationExecutor<Customer> {
}
