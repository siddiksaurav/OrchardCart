package com.farmfresh.marketplace.OrchardCart.controller;

import com.farmfresh.marketplace.OrchardCart.dto.AuthenticationResponse;
import com.farmfresh.marketplace.OrchardCart.dto.RegisterRequest;
import com.farmfresh.marketplace.OrchardCart.dto.AuthenticationRequest;
import com.farmfresh.marketplace.OrchardCart.dto.SellerRegisterRequest;
import com.farmfresh.marketplace.OrchardCart.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/user/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.customerRegister(request));
    }
    @PostMapping("/seller/register")
    public ResponseEntity<AuthenticationResponse> registerAsSeller(
            @RequestBody SellerRegisterRequest request
    ) {
        return ResponseEntity.ok(service.sellerRegister(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
