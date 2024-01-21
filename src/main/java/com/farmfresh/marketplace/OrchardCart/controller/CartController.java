package com.farmfresh.marketplace.OrchardCart.controller;

import com.farmfresh.marketplace.OrchardCart.dto.request.CartItemRequest;
import com.farmfresh.marketplace.OrchardCart.dto.request.CartItemUpdateRequest;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.Cart;
import com.farmfresh.marketplace.OrchardCart.model.CartItem;
import com.farmfresh.marketplace.OrchardCart.model.UserInfo;
import com.farmfresh.marketplace.OrchardCart.repository.UserInfoRepository;
import com.farmfresh.marketplace.OrchardCart.service.AuthenticationService;
import com.farmfresh.marketplace.OrchardCart.service.CartService;
import com.farmfresh.marketplace.OrchardCart.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final AuthenticationService authenticationService;

    public CartController(CartService cartService, AuthenticationService authenticationService) {
        this.cartService = cartService;
        this.authenticationService = authenticationService;

    }

    @GetMapping("/new")
    public String createCart(Model model){
        UserInfo user = authenticationService.getAuthUser().orElseThrow(()->new ElementNotFoundException("User not signed in"));
        Cart cart = cartService.createCart(user);
        return "cart/cart";
    }
    @PostMapping("/addItem")
    public String addCartItem(CartItemRequest cartItemRequest,
                              Model model) {
        UserInfo user = authenticationService.getAuthUser().orElseThrow(()->new ElementNotFoundException("User not signed in"));
        String result = cartService.addCartItem(user, cartItemRequest);
        model.addAttribute("result", result);
        return "redirect:/products/list"; // Redirect back to the product list
    }
    @PostMapping("/update")
    public String updateCart(Cart cart,Model model) throws Exception {
        UserInfo user = authenticationService.getAuthUser().orElseThrow(()->new ElementNotFoundException("User not signed in"));
        Cart updatedCart = cartService.updateCartItem(user,cart);
        model.addAttribute("cart", updatedCart);
        return "redirect:/home";
    }


    @GetMapping("/find")
    public String getUserCart(Model model) {
        UserInfo user = authenticationService.getAuthUser().orElseThrow(()->new ElementNotFoundException("User not signed in"));
        Cart cart = cartService.findUserCart(user);
        model.addAttribute("cart", cart);
        return "cart/cart";
    }
}

