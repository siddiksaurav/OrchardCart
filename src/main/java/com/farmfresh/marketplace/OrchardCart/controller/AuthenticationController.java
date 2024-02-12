package com.farmfresh.marketplace.OrchardCart.controller;

import com.farmfresh.marketplace.OrchardCart.dto.request.AuthenticationRequest;
import com.farmfresh.marketplace.OrchardCart.dto.request.SellerRegisterRequest;
import com.farmfresh.marketplace.OrchardCart.dto.request.UserRegisterRequest;
import com.farmfresh.marketplace.OrchardCart.dto.response.AuthenticationResponse;
import com.farmfresh.marketplace.OrchardCart.model.Role;
import com.farmfresh.marketplace.OrchardCart.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/auth")

public class AuthenticationController {
    private static final Logger log = LoggerFactory.getLogger(AuthenticationController.class);
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/registration-type")
    public String selectRegistrationTypePage() {
        return "auth/registration-option";
    }

    @GetMapping("/user/register")
    public String showUserRegistrationForm(Model model) {

        model.addAttribute("userRegisterRequest", new UserRegisterRequest());
        return "auth/register-user";
    }

    @PostMapping("/user/register")
    public String registerAsUser(@Valid UserRegisterRequest userRegisterRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors in user registration form");
            return "auth/register-user";
        }
        authenticationService.customerRegister(userRegisterRequest);
        log.info("User registered successfully");
        return "auth/register-successful";
    }

    @GetMapping("/seller/register")
    public String showSellerRegistrationForm(Model model) {
        model.addAttribute("sellerRegisterRequest", new SellerRegisterRequest());
        return "auth/register-seller";
    }

    @PostMapping("/seller/register")
    public String registerAsSeller(@Valid SellerRegisterRequest sellerRegisterRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors in seller registration form");
            return "auth/register-seller";
        }
        authenticationService.sellerRegister(sellerRegisterRequest);
        log.info("Seller registered successfully");
        return "auth/register-successful";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("authenticationRequest", new AuthenticationRequest());
        return "auth/login";
    }

    @PostMapping("/login")
    public String authenticateUser(AuthenticationRequest authenticationRequest, Model model, HttpServletResponse response) {
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);
        if (authenticationResponse.getToken() != null) {
            Cookie cookie = new Cookie("Bearer", authenticationResponse.getToken());
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
            if (authenticationResponse.getRole() == Role.ADMIN) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Invalid credentials. Please try again.");
            return "auth/login";
        }
    }
}

