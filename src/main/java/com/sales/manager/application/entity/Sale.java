package com.sales.manager.application.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String itemName;
    private Date date;
    private Double value;
    private String customer;

    @ManyToOne
    private Seller seller;

    public Sale(){
    }

    public Sale(Seller seller, String customer, String itemName, Date date, Double value){
        this.seller = seller;
        this.customer = customer;
        this.itemName = itemName;
        this.date = date;
        this.value = value;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }
}
