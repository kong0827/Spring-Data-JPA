package com.jpa.entity;

import javax.persistence.*;

@Entity(name = "cst_product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pro_id")
    private int proId;

    @Column(name = "pro_price")
    private double proPrice;

    @Column(name = "pro_size")
    private String proSize;

    @Column(name = "order_id")
    private int orderId;

    public int getProId() {
        return proId;
    }

    public void setProId(int proId) {
        this.proId = proId;
    }

    public double getProPrice() {
        return proPrice;
    }

    public void setProPrice(double proPrice) {
        this.proPrice = proPrice;
    }

    public String getProSize() {
        return proSize;
    }

    public void setProSize(String proSize) {
        this.proSize = proSize;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }


    public Product(double proPrice, String proSize, int orderId, int orderId1) {
        this.proPrice = proPrice;
        this.proSize = proSize;
        this.orderId = orderId;
        this.orderId = orderId1;
    }

    public Product() {
    }

    @Override
    public String toString() {
        return "Product{" +
                "proId=" + proId +
                ", proPrice=" + proPrice +
                ", proSize=" + proSize +
                ", orderId=" + orderId +
                '}';
    }
}
