package com.farmfresh.marketplace.OrchardCart.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @NotBlank(message = "Category name is mandatory")
    private String categoryName;
    @OneToMany(mappedBy = "category")
    private List<Product> products = new ArrayList<>();

}
