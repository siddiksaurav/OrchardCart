package com.farmfresh.marketplace.OrchardCart.controller;

import com.farmfresh.marketplace.OrchardCart.model.CartItem;
import com.farmfresh.marketplace.OrchardCart.service.CartItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart-item")
public class CartItemController {
    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping("/remove/{id}")
    public String removeCartItem(@PathVariable Integer id){
        cartItemService.removeCartItem(id);
        return "redirect:/cart/find";
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateCartItemQuantity(@PathVariable Integer id, @RequestParam Integer quantity) {
        CartItem cartItem = cartItemService.getCartItem(id);
        if (cartItem == null) {
            return ResponseEntity.notFound().build();
        }
        cartItem.setQuantity(quantity);
        cartItemService.updateCartItem(cartItem);
        return ResponseEntity.ok().build();
    }
}

