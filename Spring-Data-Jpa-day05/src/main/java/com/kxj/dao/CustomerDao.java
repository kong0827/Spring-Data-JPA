package com.kxj.dao;
/**
 * @author kxj
 * @date 2019/11/3 0:19
 * @Desc 操作数据库
 */

import com.kxj.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 符合springDataJpa的dao层接口规范
 * JpaRepository(操作的实体类类型，实体类中主键属性类型)
 * 封装了基本的CRUD操作
 * JpaSpecificationExecutor(操作的实体类类型)
 * 封装了复杂查询
 */
public interface CustomerDao extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    /**
     * 案例：根据客户名称查询客户
     * 使用jpql的形式查询
     *
     * @Query : 代表的是进行查询
     * *      * 声明此方法是用来进行更新操作
     */
    @Query(value = "from Customer where custName = ?1")
    public Customer findCustomerByCustName(String custName);

    /**
     * 根据id和用户名查询
     */
    @Query(value = "from Customer where custName = ?1 and custId = ?2")
    public Customer findCustomerByCustNameAndCustId(String custName, Long id);

    /**
     * 使用jpql完成更新操作
     * 案例 ： 根据id更新，客户的名称
     * sql  ：update cst_customer set cust_name = ? where cust_id = ?
     * jpql : update Customer set custName = ? where custId = ?
     *
     * @Modifying * 当前执行的是一个更新操作 更新操作必须加，否则程序无法运行
     */
    @Query(value = "update Customer set custName = ?1 where custId = ?2")
    @Modifying
    public void updateCustomerByCusId(String name, Long id);


    /**
     * Declare a native query at the query method using @Query
     *
     * 查询用户
     * nativeQuery： 查询方式
     *       true ： sql查询
     *       false：jpql查询
     */
    @Query(value = "select * from cst_customer where cust_name = ?1", nativeQuery = true)
    public Customer findCustomerByCustNameBySQL(String custName);


    /**
     * 模糊查询
     * @param name
     * @return
     */
    public List<Customer> findCustomersByCustNameLike(String name);

    /**
     * 模糊查询
     * @param name
     * @return
     */
    @Query(value = "from Customer where custName like ?1")
    public List<Customer> findCustomers(String name);
}
