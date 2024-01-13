package com.farmfresh.marketplace.OrchardCart.viewcontroller;

import com.farmfresh.marketplace.OrchardCart.dto.request.AuthenticationRequest;
import com.farmfresh.marketplace.OrchardCart.dto.request.RegisterRequest;
import com.farmfresh.marketplace.OrchardCart.dto.request.SellerRegisterRequest;
import com.farmfresh.marketplace.OrchardCart.dto.response.AuthenticationResponse;
import com.farmfresh.marketplace.OrchardCart.exception.ElementAlreadyExistException;
import com.farmfresh.marketplace.OrchardCart.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationViewController {
    private final AuthenticationService authenticationService;
    Logger logger = LoggerFactory.getLogger(AuthenticationViewController.class);
    @GetMapping("/registration-type")
    public String selectRegistrationTypePage() {
        return "auth/registration-option"; // Return the selection page
    }
    @GetMapping("/user/register")
    public String showUserRegistrationForm(Model model){

        model.addAttribute("user", new RegisterRequest());
        return "auth/register-user";
    }
    @GetMapping("/seller/register")
    public String showSellerRegistrationForm(Model model){
        model.addAttribute("seller", new SellerRegisterRequest());
        return "auth/register-seller";
    }
    @PostMapping("/user/register")
    public String registerAsUser(@Valid  @ModelAttribute("user") RegisterRequest request) throws ElementAlreadyExistException {
        authenticationService.customerRegister(request);
        return "auth/register-successful";
    }
    @PostMapping("/seller/register")
    public String registerAsSeller(@Valid  @ModelAttribute("seller") SellerRegisterRequest request) throws ElementAlreadyExistException {
        authenticationService.sellerRegister(request);
        return "auth/register-successful";
    }
    @GetMapping("/login")
    public String showLoginForm(Model model) {
        logger.info("In login get map");
        model.addAttribute("authenticationRequest",new AuthenticationRequest());
        return "auth/login";
    }

    @PostMapping("/login")
    public String authenticateUser(@ModelAttribute("authenticationRequest") AuthenticationRequest authenticationRequest, Model model) {
        logger.info("In login post map");
        logger.info(authenticationRequest.getEmail());
        AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);
        logger.info(response.getToken());
        System.out.println("Token:"+response.getToken());
        if (response != null && response.getToken() != null) {
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Invalid credentials. Please try again.");
            return "auth/login";
        }
    }
}

