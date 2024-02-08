package com.farmfresh.marketplace.OrchardCart.service;

import com.farmfresh.marketplace.OrchardCart.dto.request.CartItemRequest;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.Cart;
import com.farmfresh.marketplace.OrchardCart.model.CartItem;
import com.farmfresh.marketplace.OrchardCart.model.Product;
import com.farmfresh.marketplace.OrchardCart.model.UserInfo;
import com.farmfresh.marketplace.OrchardCart.repository.CartRepository;
import com.farmfresh.marketplace.OrchardCart.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;


@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemService cartItemService;



    public CartService(CartRepository cartRepository, ProductRepository productRepository, CartItemService cartItemService) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemService = cartItemService;
    }

    public Cart createCart(UserInfo user) {
        Cart cart = new Cart();
        cart.setUserInfo(user);
        return cartRepository.save(cart);
    }

    public String addCartItem(UserInfo user, CartItemRequest cartItemRequest) {
        Cart cart = cartRepository.findCartByUserInfoId(user.getId());
        Product product = productRepository.findById(cartItemRequest.getProductId()).orElseThrow(() -> new ElementNotFoundException("Product not found with Id:" + cartItemRequest.getProductId()));
        CartItem isExist = cartItemService.isCartItemExist(cart, product, user.getId());
        if (isExist == null) {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setPrice(BigDecimal.valueOf(cartItemRequest.getQuantity()).multiply(product.getPrice()));
            cartItem.setUserId(user.getId());
            cartItemService.createCartItem(cartItem);
            cart.getCartItems().add(cartItem);
        }
        return "Added cart item successfully";
    }

    public Cart findUserCart(UserInfo user) {
        Cart cart = cartRepository.findCartByUserInfoId(user.getId());
        BigDecimal totalPrice = BigDecimal.ZERO;
        int totalItem = 0;
        for (CartItem cartItem : cart.getCartItems()) {
            totalPrice.add(cartItem.getPrice());
            totalItem += cartItem.getQuantity();
        }
        cart.setTotalItem(totalItem);
        cart.setTotalPrice(totalPrice);
        return cartRepository.save(cart);
    }


}
