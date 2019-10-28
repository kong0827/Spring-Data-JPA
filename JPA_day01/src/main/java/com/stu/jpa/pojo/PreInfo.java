package com.stu.jpa.pojo;

import javax.persistence.*;

//关联数据表
@Table(name = "company")
//数据持久化
@Entity
public class PreInfo {
    private int id;
    private String companyName;
    private String companyNo;

    @Column(name = "id")
    @Id
    //生成主键的方式
    @GeneratedValue(strategy = GenerationType.AUTO)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyNo() {
        return companyNo;
    }

    public void setCompanyNo(String companyNo) {
        this.companyNo = companyNo;
    }

    public PreInfo(int id, String companyName, String companyNo) {
        this.id = id;
        this.companyName = companyName;
        this.companyNo = companyNo;
    }

    public PreInfo() {
    }

    @Override
    public String toString() {
        return "PreInfo{" +
                "id=" + id +
                ", companyName='" + companyName + '\'' +
                ", companyNo='" + companyNo + '\'' +
                '}';
    }
}
