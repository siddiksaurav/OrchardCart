package com.farmfresh.marketplace.OrchardCart.service;

import com.farmfresh.marketplace.OrchardCart.dto.request.AuthenticationRequest;
import com.farmfresh.marketplace.OrchardCart.dto.response.AuthenticationResponse;
import com.farmfresh.marketplace.OrchardCart.dto.request.RegisterRequest;
import com.farmfresh.marketplace.OrchardCart.exception.ElementAlreadyExistException;
import com.farmfresh.marketplace.OrchardCart.model.Role;
import com.farmfresh.marketplace.OrchardCart.model.Seller;
import com.farmfresh.marketplace.OrchardCart.model.UserInfo;
import com.farmfresh.marketplace.OrchardCart.repository.SellerRepository;
import com.farmfresh.marketplace.OrchardCart.repository.UserInfoRepository;
import com.farmfresh.marketplace.OrchardCart.dto.request.SellerRegisterRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserInfoRepository userInfoRepository;
    private final SellerRepository sellerRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationService(UserInfoRepository userInfoRepository, SellerRepository sellerRepository, JwtService jwtService, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userInfoRepository = userInfoRepository;
        this.sellerRepository = sellerRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    Logger log = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationResponse customerRegister(RegisterRequest request) throws ElementAlreadyExistException {
        Optional<UserInfo> user = userInfoRepository.findByEmail(request.getEmail());
        if (user.isPresent()) throw new ElementAlreadyExistException("User already exist with email:" + request.getEmail());
        UserInfo newUser = new UserInfo(request.getFirstname(),request.getLastname(),request.getEmail(),passwordEncoder.encode(request.getPassword()), Role.CUSTOMER);
        userInfoRepository.save(newUser);
        var jwtToken = jwtService.generateToken(newUser);
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse sellerRegister(SellerRegisterRequest request) throws ElementAlreadyExistException {
        Optional<UserInfo> user = userInfoRepository.findByEmail(request.getEmail());
        if (user.isPresent()) throw new ElementAlreadyExistException("User already exist with email:" + request.getEmail());
        UserInfo newUser = new UserInfo(request.getFirstname(),request.getLastname(),request.getEmail(),passwordEncoder.encode(request.getPassword()), Role.SELLER);
        Seller seller = new Seller(newUser,request.getAddress(),request.getBusinessName(),request.getDescription());

        sellerRepository.save(seller);
        var jwtToken = jwtService.generateToken(newUser);
        return new AuthenticationResponse(jwtToken);
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userInfoRepository.findByEmail(request.getEmail())
                .orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }

    public String getAuthUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = null;
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails userDetails) {
                userEmail = userDetails.getUsername();
            }
        }
        return userEmail;

    }

}
