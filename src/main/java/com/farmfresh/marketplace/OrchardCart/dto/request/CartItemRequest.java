package com.farmfresh.marketplace.OrchardCart.dto.request;

import lombok.Data;

@Data
public class CartItemRequest {
    private Integer cartId;
    private Integer productId;
    private Integer quantity;
}
