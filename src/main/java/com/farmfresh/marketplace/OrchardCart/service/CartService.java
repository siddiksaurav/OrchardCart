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
    private final AuthenticationService authenticationService;



    public CartService(CartRepository cartRepository, ProductRepository productRepository, CartItemService cartItemService, AuthenticationService authenticationService) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemService = cartItemService;
        this.authenticationService = authenticationService;
    }

    public Cart getCart() {
        UserInfo user = authenticationService.getAuthUser().orElseThrow(() -> new ElementNotFoundException("User not signed in"));
        Cart cart = cartRepository.findCartByUserInfoId(user.getId());
        if (cart==null) {
            Cart newCart = new Cart();
            newCart.setUserInfo(user);
            return cartRepository.save(newCart);
        }
        return cart;
    }

    public String addCartItem(CartItemRequest cartItemRequest) {
        UserInfo user = authenticationService.getAuthUser().orElseThrow(() -> new ElementNotFoundException("User not signed in"));
        Cart cart = getCart();
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

    public Cart showUserCart() {
        Cart cart = getCart();
        if(cart.getCartItems().isEmpty()){
            return cart;
        }

        BigDecimal totalPrice = BigDecimal.ZERO;
        int totalItem = 0;
        for (CartItem cartItem : cart.getCartItems()) {
            totalPrice = totalPrice.add(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
            totalItem += cartItem.getQuantity();
        }
        cart.setTotalItem(totalItem);
        cart.setTotalPrice(totalPrice);
        return cartRepository.save(cart);
    }

    public void removeCart(){
        Cart cart = getCart();
        cartRepository.delete(cart);
    }


}
