package com.farmfresh.marketplace.OrchardCart.service;

import com.farmfresh.marketplace.OrchardCart.dto.request.CartItemRequest;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.Cart;
import com.farmfresh.marketplace.OrchardCart.model.CartItem;
import com.farmfresh.marketplace.OrchardCart.model.Product;
import com.farmfresh.marketplace.OrchardCart.model.UserInfo;
import com.farmfresh.marketplace.OrchardCart.repository.CartItemRepository;
import com.farmfresh.marketplace.OrchardCart.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;

    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public void createCartItem(CartItem cartItem){
        cartItemRepository.save(cartItem);
    }
    public CartItem updateCartItem(Integer userId,Integer cartItemId, CartItem cartItem) throws ElementNotFoundException {
        CartItem existingItem = cartItemRepository.findById(cartItemId).orElseThrow(()-> new ElementNotFoundException("CartItem nitfound with Id:"+cartItemId));
        if(userId.equals(existingItem.getUserId()))
        {
            existingItem.setQuantity(cartItem.getQuantity());
            existingItem.setPrice(cartItem.getProduct().getPrice()*cartItem.getQuantity());

        }
        return cartItemRepository.save(existingItem);
    }
    public CartItem isCartItemExist(Cart cart, Product product, Integer userId){
        CartItem cartItem = cartItemRepository.isCartItemExist(cart,product,userId);
        return  cartItem;
    }

    public void removeCartItem(Integer userId,Integer cartItemId) throws AccessDeniedException, ElementNotFoundException {
        CartItem cartItem = findCartItemById(cartItemId);
        if(userId.equals(cartItem.getUserId())){
            cartItemRepository.deleteById(cartItemId);
        }
        else {
            throw new AccessDeniedException("You are not Authorized to remove cartitem");
        }
    }
    public CartItem findCartItemById(Integer cartItemId) throws ElementNotFoundException {
        return cartItemRepository.findById(cartItemId).orElseThrow(()->new ElementNotFoundException("cart item not available with id :"+cartItemId));
    }

}
