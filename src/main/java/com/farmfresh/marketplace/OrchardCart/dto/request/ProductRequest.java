package com.farmfresh.marketplace.OrchardCart.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name should be between 2 and 100 characters")
    private String name;
    private String description;
    @DecimalMin(value = "0.0", message = "Price should be a positive value")
    private double price;
    @Min(value = 0, message = "Quantity should be a positive value")
    private Integer quantity;
    @NotBlank(message = "Category name is required")
    private String categoryName;
    @NotBlank(message = "businessName is required")
    private String businessName;
}
