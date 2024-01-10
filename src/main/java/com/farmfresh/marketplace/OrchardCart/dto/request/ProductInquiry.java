package com.farmfresh.marketplace.OrchardCart.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProductInquiry {
   @Email(message = "Invalid email address")
   private String userEmail;
   @NotBlank(message = "product name required")
   private String productName;
   private String message;
}
