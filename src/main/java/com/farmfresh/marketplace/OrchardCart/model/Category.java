package com.farmfresh.marketplace.OrchardCart.model;

import jakarta.persistence.*;

import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String categoryName;
    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

}
