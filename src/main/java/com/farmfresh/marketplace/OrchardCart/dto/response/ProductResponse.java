package com.farmfresh.marketplace.OrchardCart.dto.response;

import lombok.Data;

@Data
public class ProductResponse {
    private String name;
    private String description;
    private double price;
    private Integer quantity;
    private String categoryName;
    private String businessName;
}
