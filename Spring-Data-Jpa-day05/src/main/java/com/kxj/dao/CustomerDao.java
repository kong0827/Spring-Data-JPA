package com.kxj.dao; /**
 * @author kxj
 * @date 2019/11/3 0:19
 * @Desc 操作数据库
 */

import com.kxj.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 符合springDataJpa的dao层接口规范
 *      JpaRepository(操作的实体类类型，实体类中主键属性类型)
 *          封装了基本的CRUD操作
 *       JpaSpecificationExecutor(操作的实体类类型)
 *          封装了复杂查询
 */
public interface CustomerDao extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
}
