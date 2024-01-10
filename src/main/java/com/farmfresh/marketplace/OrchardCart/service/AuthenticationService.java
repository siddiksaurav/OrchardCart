package com.farmfresh.marketplace.OrchardCart.service;

import com.farmfresh.marketplace.OrchardCart.dto.request.AuthenticationRequest;
import com.farmfresh.marketplace.OrchardCart.dto.response.AuthenticationResponse;
import com.farmfresh.marketplace.OrchardCart.dto.request.RegisterRequest;
import com.farmfresh.marketplace.OrchardCart.exception.ElementAlreadyExistException;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserInfoRepository userInfoRepository;
    private final SellerRepository sellerRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    public AuthenticationResponse customerRegister(RegisterRequest request) throws ElementAlreadyExistException {
        Optional<UserInfo> user = userInfoRepository.findByEmail(request.getEmail());
        if (user.isPresent()) throw new ElementAlreadyExistException("User already exist with email:" + request.getEmail());
        UserInfo newUser = UserInfo.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        userInfoRepository.save(newUser);
        var jwtToken = jwtService.generateToken(newUser);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    public AuthenticationResponse sellerRegister(SellerRegisterRequest request) throws ElementAlreadyExistException {
        Optional<UserInfo> user = userInfoRepository.findByEmail(request.getEmail());
        if (user.isPresent()) throw new ElementAlreadyExistException("User already exist with email:" + request.getEmail());
        UserInfo newUser = UserInfo.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        Seller seller = Seller.builder()
                .userInfo(newUser)
                .address(request.getAddress())
                .businessName(request.getBusinessName())
                .description(request.getDescription())
                .build();

        sellerRepository.save(seller);
        var jwtToken = jwtService.generateToken(newUser);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();

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
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

}
