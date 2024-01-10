package com.farmfresh.marketplace.OrchardCart.dto.request;

import lombok.Data;

@Data
public class ReviewRequest {
    private Integer productId;
    private String Review;
}
