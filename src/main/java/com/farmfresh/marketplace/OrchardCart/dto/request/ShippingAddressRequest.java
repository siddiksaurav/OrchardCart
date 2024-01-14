package com.farmfresh.marketplace.OrchardCart.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.Data;


public class ShippingAddressRequest {
    private String district;
    private String city;
    private String additionalAddress;
    @Pattern(regexp = "^01\\d{9}$")
    private String phoneNumber;

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAdditionalAddress() {
        return additionalAddress;
    }

    public void setAdditionalAddress(String additionalAddress) {
        this.additionalAddress = additionalAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
