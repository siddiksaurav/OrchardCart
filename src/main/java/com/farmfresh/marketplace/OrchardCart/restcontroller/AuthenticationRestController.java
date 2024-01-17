package com.farmfresh.marketplace.OrchardCart.restcontroller;

import com.farmfresh.marketplace.OrchardCart.dto.response.AuthenticationResponse;
import com.farmfresh.marketplace.OrchardCart.dto.request.RegisterRequest;
import com.farmfresh.marketplace.OrchardCart.dto.request.AuthenticationRequest;
import com.farmfresh.marketplace.OrchardCart.dto.request.SellerRegisterRequest;
import com.farmfresh.marketplace.OrchardCart.exception.ElementAlreadyExistException;
import com.farmfresh.marketplace.OrchardCart.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/user/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) throws ElementAlreadyExistException {
        return ResponseEntity.ok(service.customerRegister(request));
    }
    @PostMapping("/seller/register")
    public ResponseEntity<AuthenticationResponse> registerAsSeller(
            @RequestBody SellerRegisterRequest request
    ) throws ElementAlreadyExistException {
        return ResponseEntity.ok(service.sellerRegister(request));
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
