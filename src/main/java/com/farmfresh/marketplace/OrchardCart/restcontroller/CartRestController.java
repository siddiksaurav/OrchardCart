package com.farmfresh.marketplace.OrchardCart.restcontroller;

import com.farmfresh.marketplace.OrchardCart.dto.request.CartItemRequest;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.Cart;
import com.farmfresh.marketplace.OrchardCart.model.UserInfo;
import com.farmfresh.marketplace.OrchardCart.service.CartService;
import com.farmfresh.marketplace.OrchardCart.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartRestController {

    @Autowired
    private CartService cartService;
    @Autowired
    private JwtService jwtService;


    @GetMapping("/")
    public Cart createCart(@RequestHeader("Authorization") String jwt) throws ElementNotFoundException {
        UserInfo user = jwtService.getUserByToken(jwt);
        return cartService.createCart(user);
    }

    @PostMapping("/addItem")
    public String addCartItem(@RequestHeader("Authorization") String jwt,
                              @RequestBody CartItemRequest cartItemRequest) throws ElementNotFoundException {
        UserInfo user = jwtService.getUserByToken(jwt);
        return cartService.addCartItem(user, cartItemRequest);
    }

    @GetMapping("/findCart")
    public Cart getUserCart(@RequestHeader("Authorization") String jwt) throws ElementNotFoundException {
        UserInfo user = jwtService.getUserByToken(jwt);
        return cartService.findUserCart(user);
    }

}
