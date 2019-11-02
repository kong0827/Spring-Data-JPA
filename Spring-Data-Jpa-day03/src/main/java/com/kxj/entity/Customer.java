package com.kxj.entity;

import javax.persistence.*;

/**
 * @author kxj
 * @date 2019/10/29 22:39
 * @Desc 客户实体类
 */

/**
 * 配置实体类与数据库的映射关系
 * @Entity 声明实体类
 * @Table 建立实体类和表的映射关系
 * @Id 声明主键
 * @GeneratedValue 主键的生成策略
 * @Column 指定和表中字段的映射关系
 *
 */
@Entity
@Table(name="cst_customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cust_id")
    private Long custId;   // 客户Id 主键

    @Column(name = "cust_name")
    private String custName; // 客户姓名

    @Column(name = "cust_source")
    private String custSource; // 客户来源

    @Column(name = "cust_industry")
    private String custIndustry; // 客户级别

    @Column(name = "cust_level")
    private String custLevel; //客户级别

    @Column(name = "cust_address")
    private String custAddress; // 客户地址

    @Column(name = "cust_phone")
    private String custPhone; // 联系方式

    public Customer() {
    }

    public Long getCustId() {
        return custId;
    }

    public void setCustId(Long custId) {
        this.custId = custId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustSource() {
        return custSource;
    }

    public void setCustSource(String custSource) {
        this.custSource = custSource;
    }

    public String getCustIndustry() {
        return custIndustry;
    }

    public void setCustIndustry(String custIndustry) {
        this.custIndustry = custIndustry;
    }

    public String getCustLevel() {
        return custLevel;
    }

    public void setCustLevel(String custLevel) {
        this.custLevel = custLevel;
    }

    public String getCustAddress() {
        return custAddress;
    }

    public void setCustAddress(String custAddress) {
        this.custAddress = custAddress;
    }

    public String getCustPhone() {
        return custPhone;
    }

    public void setCustPhone(String custPhone) {
        this.custPhone = custPhone;
    }

    @Override
    public String toString() {
        return "com.kxj.entity.Customer{" +
                "custId=" + custId +
                ", custName='" + custName + '\'' +
                ", custSource='" + custSource + '\'' +
                ", custIndustry='" + custIndustry + '\'' +
                ", custLevel='" + custLevel + '\'' +
                ", custAddress='" + custAddress + '\'' +
                ", custPhone='" + custPhone + '\'' +
                '}';
    }
}
