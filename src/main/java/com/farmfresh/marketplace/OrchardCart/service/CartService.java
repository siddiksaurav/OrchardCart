package com.farmfresh.marketplace.OrchardCart.service;

import com.farmfresh.marketplace.OrchardCart.dto.request.CartItemRequest;
import com.farmfresh.marketplace.OrchardCart.dto.response.ProductResponse;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.Cart;
import com.farmfresh.marketplace.OrchardCart.model.CartItem;
import com.farmfresh.marketplace.OrchardCart.model.Product;
import com.farmfresh.marketplace.OrchardCart.model.UserInfo;
import com.farmfresh.marketplace.OrchardCart.repository.CartRepository;
import com.farmfresh.marketplace.OrchardCart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    private  final CartItemService cartItemService;

    public CartService(CartRepository cartRepository, ProductRepository productRepository, CartItemService cartItemService) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.cartItemService = cartItemService;
    }

    public Cart createCart(UserInfo user) {
        Cart cart = new Cart();
        cart.setUserInfo(user);
        return  cartRepository.save(cart);
    }

    public String addCartItem(Integer userId, CartItemRequest cartItemRequest) throws ElementNotFoundException {
        Cart cart = cartRepository.findCartByUserInfoId(userId);
        Product product = productRepository.findById(cartItemRequest.getProductId()).orElseThrow(()->new ElementNotFoundException("Product not found with Id:"+cartItemRequest.getProductId()));
        CartItem isExist = cartItemService.isCartItemExist(cart,product,userId);
        if (isExist == null) {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setPrice(cartItemRequest.getQuantity() * product.getPrice());
            cartItem.setUserId(userId);
            cartItemService.createCartItem(cartItem);
            cart.getCartItems().add(cartItem);
        }
        return "Added cart item successfully";
    }
    public Cart findUserCart(Integer userId) {
        Cart cart = cartRepository.findCartByUserInfoId(userId);
        double totalPrice = 0;
        int totalItem = 0;
        for (CartItem item : cart.getCartItems()) {
            totalPrice = totalPrice + item.getPrice();
            totalItem= totalItem + item.getQuantity();
        }
        cart.setTotalItem(totalItem);
        cart.setTotalPrice(totalPrice);
        return cartRepository.save(cart);
    }
}
