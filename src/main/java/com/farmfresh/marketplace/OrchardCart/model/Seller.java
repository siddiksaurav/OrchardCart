package com.farmfresh.marketplace.OrchardCart.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seller {
    @Id
    private Integer id;
    @OneToOne
    @MapsId
    @JoinColumn(name = "seller_id")
    private UserInfo userInfo;
    @NotBlank(message = "address is mandatory")
    private String address;
    @Column(unique = true)
    private String businessName;
    private String description;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<Product> products = new ArrayList<>();
}
