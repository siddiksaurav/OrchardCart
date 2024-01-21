package com.farmfresh.marketplace.OrchardCart.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class ProductInquiry {
   @Email
   private String userEmail;
   @NotBlank
   private String productName;
   @NotBlank
   private String message;

   public String getUserEmail() {
      return userEmail;
   }

   public void setUserEmail(String userEmail) {
      this.userEmail = userEmail;
   }

   public String getProductName() {
      return productName;
   }

   public void setProductName(String productName) {
      this.productName = productName;
   }

   public String getMessage() {
      return message;
   }

   public void setMessage(String message) {
      this.message = message;
   }
}
