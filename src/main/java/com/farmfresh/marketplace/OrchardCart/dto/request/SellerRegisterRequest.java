package com.farmfresh.marketplace.OrchardCart.dto.request;

import com.farmfresh.marketplace.OrchardCart.model.enumaration.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SellerRegisterRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Role role;
    private String address;
    private String businessName;
    private String description;
}

