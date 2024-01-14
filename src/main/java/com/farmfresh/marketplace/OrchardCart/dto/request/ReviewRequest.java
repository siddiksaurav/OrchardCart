package com.farmfresh.marketplace.OrchardCart.dto.request;

import lombok.Data;

public class ReviewRequest {
    private Integer productId;
    private String Review;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getReview() {
        return Review;
    }

    public void setReview(String review) {
        Review = review;
    }
}
