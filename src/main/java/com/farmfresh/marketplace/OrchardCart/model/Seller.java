package com.farmfresh.marketplace.OrchardCart.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Seller {
    @Id
    private Integer id;
    @OneToOne
    @MapsId
    private UserInfo userInfo;
    @NotBlank
    private String address;
    @Column(unique = true)
    private String businessName;
    private String description;
    public Seller(){}
    public Seller(UserInfo userInfo, String address, String businessName, String description) {
        this.userInfo = userInfo;
        this.address = address;
        this.businessName = businessName;
        this.description = description;
    }

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
