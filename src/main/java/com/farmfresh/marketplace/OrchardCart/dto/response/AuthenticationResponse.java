package com.farmfresh.marketplace.OrchardCart.dto.response;

import com.farmfresh.marketplace.OrchardCart.model.Role;


public class AuthenticationResponse {
    private String token;
    private Role role;

    public AuthenticationResponse(String token, Role role) {
        this.token = token;
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
