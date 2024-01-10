package com.farmfresh.marketplace.OrchardCart.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ShippingAddressRequest {
    private String district;
    private String city;
    private String additionalAddress;
    @Pattern(regexp = "^01\\d{9}$", message = "Phone number must start with '01' and have 11 digits")
    private String phoneNumber;

}
