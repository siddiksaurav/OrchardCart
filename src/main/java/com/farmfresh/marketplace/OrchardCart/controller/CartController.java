package com.farmfresh.marketplace.OrchardCart.controller;

import com.farmfresh.marketplace.OrchardCart.dto.request.CartItemRequest;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.Cart;
import com.farmfresh.marketplace.OrchardCart.model.UserInfo;
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

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private JwtService jwtService;
    Logger logger = LoggerFactory.getLogger(CartController.class);

    @GetMapping("/new")
    public String createCart(Model model) throws ElementNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = null;
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails userDetails) {
                username = userDetails.getUsername();
            }
        }
        //UserInfo user = jwtService.getUserByToken(jwt);
        UserDetails userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        logger.info("in new cart: "+username);
        //Cart cart = cartService.createCart(user);
        model.addAttribute("cart", new Cart());
        return "/cart/cart";
    }

    @PostMapping("/addItem")
    public String addCartItem(@RequestHeader("Authorization") String jwt,
                              @RequestBody CartItemRequest cartItemRequest,
                              Model model) throws ElementNotFoundException {
        UserInfo user = jwtService.getUserByToken(jwt);
        String result = cartService.addCartItem(user.getId(), cartItemRequest);
        model.addAttribute("result", result);
        return "/cart/cart";
    }

    @GetMapping("/findCart")
    public String getUserCart(@RequestHeader("Authorization") String jwt, Model model) throws ElementNotFoundException {
        UserInfo user = jwtService.getUserByToken(jwt);
        Cart cart = cartService.findUserCart(user.getId());
        model.addAttribute("cart", cart);
        return "/cart/cart";
    }
}

