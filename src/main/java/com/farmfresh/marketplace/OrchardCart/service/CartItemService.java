package com.farmfresh.marketplace.OrchardCart.service;

import com.farmfresh.marketplace.OrchardCart.model.Cart;
import com.farmfresh.marketplace.OrchardCart.model.CartItem;
import com.farmfresh.marketplace.OrchardCart.model.Product;
import com.farmfresh.marketplace.OrchardCart.repository.CartItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CartItemService {
    public static final Logger log = LoggerFactory.getLogger(CartItemService.class);
    private final CartItemRepository cartItemRepository;

    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public void saveCartItem(CartItem cartItem) {
        cartItemRepository.save(cartItem);
    }

    public void updateCartItems(Cart existingCart, List<CartItem> updatedCartItems) {
        Set<Integer> updatedCartItemIds = updatedCartItems.stream()
                .map(CartItem::getId)
                .collect(Collectors.toSet());

        Iterator<CartItem> iterator = existingCart.getCartItems().iterator();
        while (iterator.hasNext()) {
            CartItem existingCartItem = iterator.next();
            if (!updatedCartItemIds.contains(existingCartItem.getId())) {
                log.warn("No cart item should be removed at this time");
                removeCartItem(existingCartItem);
                iterator.remove();
            }
        }

        for (CartItem updatedCartItem : updatedCartItems) {
            CartItem cartItem = cartItemRepository.save(updatedCartItem);
        }

    }

    public CartItem isCartItemExist(Cart cart, Product product, Integer userId) {
        return cartItemRepository.isCartItemExist(cart, product, userId);
    }

    public void removeCartItem(CartItem cartItem) {
        cartItemRepository.deleteById(cartItem.getId());
    }

}
