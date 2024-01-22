package com.farmfresh.marketplace.OrchardCart.dto.request;

import com.farmfresh.marketplace.OrchardCart.model.Role;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class SellerRegisterRequest {
    @NotBlank
    private String firstname;
    private String lastname;
    @Email
    private String email;
    @NotBlank
    @Size(min=8)
    private String password;
    private Role role;
    @NotBlank
    private String businessName;
    private String description;
    @NotBlank
    private String district;
    @NotBlank
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

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }


    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

