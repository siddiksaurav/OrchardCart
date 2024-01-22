package com.farmfresh.marketplace.OrchardCart.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;


public class ShippingAddressRequest {
    @NotBlank
    private String district;
    @NotBlank
    private String city;
    private String additionalAddress;
    @Pattern(regexp = "^01\\d{9}$")
    private String phoneNumber;

    public ShippingAddressRequest(String district, String city, String additionalAddress, String phoneNumber) {
        this.district = district;
        this.city = city;
        this.additionalAddress = additionalAddress;
        this.phoneNumber = phoneNumber;
    }

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
