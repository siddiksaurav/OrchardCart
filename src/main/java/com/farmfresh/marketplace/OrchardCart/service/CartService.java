package com.farmfresh.marketplace.OrchardCart.service;

import com.farmfresh.marketplace.OrchardCart.dto.request.CartItemRequest;
import com.farmfresh.marketplace.OrchardCart.dto.request.CartItemUpdateRequest;
import com.farmfresh.marketplace.OrchardCart.dto.response.ProductResponse;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.Cart;
import com.farmfresh.marketplace.OrchardCart.model.CartItem;
import com.farmfresh.marketplace.OrchardCart.model.Product;
import com.farmfresh.marketplace.OrchardCart.model.UserInfo;
import com.farmfresh.marketplace.OrchardCart.repository.CartRepository;
import com.farmfresh.marketplace.OrchardCart.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    private  final CartItemService cartItemService;

    private Logger log = LoggerFactory.getLogger(CartService.class);
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

    public String addCartItem(UserInfo user, CartItemRequest cartItemRequest) throws ElementNotFoundException {
        Cart cart = cartRepository.findCartByUserInfoId(user.getId());
        if (cart==null){
            cart = createCart(user);
        }
        Product product = productRepository.findById(cartItemRequest.getProductId()).orElseThrow(()->new ElementNotFoundException("Product not found with Id:"+cartItemRequest.getProductId()));
        CartItem isExist = cartItemService.isCartItemExist(cart,product,user.getId());
        if (isExist == null) {
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(cartItemRequest.getQuantity());
            cartItem.setPrice(cartItemRequest.getQuantity() * product.getPrice());
            cartItem.setUserId(user.getId());
            cartItemService.saveCartItem(cartItem);
            cart.getCartItems().add(cartItem);
        }
        return "Added cart item successfully";
    }
    public Cart findUserCart(UserInfo user) {
        Cart cart = cartRepository.findCartByUserInfoId(user.getId());
        if (cart == null){
            return createCart(user);
        }
        return cart;
    }

    public Cart updateCartItem(UserInfo user, Cart cart) throws Exception {
        if (cart == null){
            throw new Exception("Cart can't be null after update");
        }
        Cart existingCart = cartRepository.findCartByUserInfoId(user.getId());
        if (existingCart != null) {
            List<CartItem> updatedCartItems = cart.getCartItems();
            cartItemService.updateCartItems(existingCart, updatedCartItems);
            double totalPrice = 0;
            int totalItem = 0;
            for (CartItem item : cart.getCartItems()) {
                totalPrice = totalPrice + item.getPrice()*item.getQuantity();
                totalItem= totalItem + item.getQuantity();
            }
            cart.setTotalItem(totalItem);
            cart.setTotalPrice(totalPrice);
            return cartRepository.save(cart);
        }
        else{
            throw new Exception("Existing cart can't be null");
        }
    }

}
