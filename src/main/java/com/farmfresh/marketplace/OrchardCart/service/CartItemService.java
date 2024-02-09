package com.farmfresh.marketplace.OrchardCart.service;

import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.Cart;
import com.farmfresh.marketplace.OrchardCart.model.CartItem;
import com.farmfresh.marketplace.OrchardCart.model.Product;
import com.farmfresh.marketplace.OrchardCart.repository.CartItemRepository;
import com.farmfresh.marketplace.OrchardCart.repository.CartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CartItemService {
    public static final Logger log = LoggerFactory.getLogger(CartItemService.class);
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    public CartItemService(CartItemRepository cartItemRepository, CartRepository cartRepository) {
        this.cartItemRepository = cartItemRepository;
        this.cartRepository = cartRepository;
    }

    public void createCartItem(CartItem cartItem) {
        cartItem.setQuantity(1);
        cartItem.setPrice(cartItem.getProduct().getPrice());
        cartItemRepository.save(cartItem);
    }

    public void updateCartItem(CartItem cartItem) {
        //CartItem item = getCartItem(itemId);
        BigDecimal totalItemPrice = BigDecimal.valueOf(cartItem.getQuantity()).multiply(cartItem.getProduct().getPrice());
        cartItem.setPrice(totalItemPrice);
        cartItemRepository.save(cartItem);
    }

    public CartItem isCartItemExist(Cart cart, Product product, Integer userId) {
        return cartItemRepository.isCartItemExist(cart, product, userId);
    }

    public void removeCartItem(Integer itemId) {
        cartItemRepository.deleteById(itemId);
    }

    public CartItem getCartItem(Integer id) {
        return cartItemRepository.findById(id).orElseThrow(() -> new ElementNotFoundException("cart item not found with Id"+ id));
    }
}
