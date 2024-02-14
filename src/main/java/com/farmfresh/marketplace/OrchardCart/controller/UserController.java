package com.farmfresh.marketplace.OrchardCart.controller;

import com.farmfresh.marketplace.OrchardCart.model.Product;
import com.farmfresh.marketplace.OrchardCart.model.Seller;
import com.farmfresh.marketplace.OrchardCart.model.UserInfo;
import com.farmfresh.marketplace.OrchardCart.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String showUserProfile(Model model){
        UserInfo user = userService.getUserDetails();
        model.addAttribute("user",user);
        return "user/profile";
    }

    @GetMapping("/seller-profile")
    public String showSellerProfile(Model model){
        Seller seller = userService.getSellerDetails();
        model.addAttribute("seller",seller);
        return "user/profile";
    }


    @GetMapping("/update/profile")
    public String updateUserProfileForm(Model model){
        UserInfo user = userService.getUserDetails();
        model.addAttribute("user",user);
        return "user/profile-edit";

    }
    @PostMapping("/update/profile")
    public String updateUserProfile(@Valid UserInfo user, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "user/profile-edit";
        }
        userService.updateUserInfo(user);
        return "redirect:/user/profile";
    }

    @GetMapping("/products")
    public String listSellerProducts(Model model) {
        List<Product> products = userService.getSellerProducts();
        model.addAttribute("products",products);
        return "products/product-list";
    }

    @GetMapping("/update/seller-profile")
    public String updateSellerProfileForm(Model model){
        Seller seller = userService.getSellerDetails();
        model.addAttribute("seller",seller);
        return "user/profile-edit";

    }
    @PostMapping("/update/seller-profile")
    public String updateSellerProfile(@Valid Seller seller, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            return "user/profile-edit";
        }
        userService.updateSellerInfo(seller);
        return "redirect:/user/profile";
    }


}
