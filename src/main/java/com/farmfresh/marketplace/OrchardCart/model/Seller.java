package com.farmfresh.marketplace.OrchardCart.model;

import com.farmfresh.marketplace.OrchardCart.dto.request.AddressRequest;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Seller {
    @Id
    private Integer id;
    @OneToOne
    @MapsId
    // named changed for removing joincolumn. It will give errors
    private UserInfo userInfo;
    @Column(unique = true)
    private String businessName;
    private String description;
    @OneToOne
    private Address address;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Seller(){}
    public Seller(UserInfo userInfo, String businessName, String description,Address address) {
        this.userInfo = userInfo;
        this.businessName = businessName;
        this.description = description;
        this.address= address;
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
