package com.farmfresh.marketplace.OrchardCart.controller;

import com.farmfresh.marketplace.OrchardCart.dto.request.CartItemRequest;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.Cart;
import com.farmfresh.marketplace.OrchardCart.model.UserInfo;
import com.farmfresh.marketplace.OrchardCart.service.AuthenticationService;
import com.farmfresh.marketplace.OrchardCart.service.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final AuthenticationService authenticationService;

    public CartController(CartService cartService, AuthenticationService authenticationService) {
        this.cartService = cartService;
        this.authenticationService = authenticationService;

    }

    @PostMapping("/addItem")
    public String addCartItem(CartItemRequest cartItemRequest,
                              Model model) {
        UserInfo user = authenticationService.getAuthUser().orElseThrow(() -> new ElementNotFoundException("User not signed in"));
        String result = cartService.addCartItem(cartItemRequest);
        model.addAttribute("result", result);
        return "redirect:/products/list";
    }

    @GetMapping("/find")
    public String getUserCart(Model model) {
        UserInfo user = authenticationService.getAuthUser().orElseThrow(() -> new ElementNotFoundException("User not signed in"));
        Cart cart = cartService.showUserCart(user);
        model.addAttribute("cart", cart);
        return "cart/cart";
    }
}

