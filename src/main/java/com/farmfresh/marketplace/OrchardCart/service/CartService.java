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

    public String addCartItem(UserInfo user, CartItemRequest cartItemRequest){
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
            cartItem.setPrice(BigDecimal.valueOf(cartItemRequest.getQuantity()).multiply(product.getPrice()));
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
            BigDecimal totalPrice = BigDecimal.ZERO;
            int totalItem = 0;
            for (CartItem item : cart.getCartItems()) {
                BigDecimal itemTotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                totalPrice = totalPrice.add(itemTotal);
                totalItem= totalItem + item.getQuantity();
            }
            cart.setTotalItem(totalItem);
            cart.setTotalPrice(totalPrice);
            return cartRepository.save(cart);
        }
        else{
            throw new Exception("Existing cart can't be null after update");
        }
    }

    public void clearUserCart(UserInfo user) {
        Cart cart =findUserCart(user);
        cartRepository.deleteById(cart.getId());
    }
}
