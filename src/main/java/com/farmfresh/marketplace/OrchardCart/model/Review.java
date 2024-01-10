package com.farmfresh.marketplace.OrchardCart.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private UserInfo user;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private String review;
    private LocalDateTime createTime;
}
