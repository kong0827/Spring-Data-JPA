package com.jpa.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "cst_order")
public class Order {

    public Order(Date orderTime, String orderAddress, int custId, List<Product> products) {
        this.orderTime = orderTime;
        this.orderAddress = orderAddress;
        this.custId = custId;
        this.products = products;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;

    @Column(name = "order_time")
    private Date orderTime;

    @Column(name = "order_address")
    private String orderAddress;

    @Column(name = "cust_id")
    private int custId;

    @OneToMany(targetEntity = Product.class,fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id",referencedColumnName = "order_id")
    private List<Product> products;

    public Order() {
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }



    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", orderTime=" + orderTime +
                ", orderAddress='" + orderAddress + '\'' +
                ", custId=" + custId +
                ", products=" + products +
                '}';
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }


}
