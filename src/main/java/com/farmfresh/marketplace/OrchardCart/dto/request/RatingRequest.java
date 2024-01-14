package com.farmfresh.marketplace.OrchardCart.dto.request;

import lombok.Data;

public class RatingRequest {
    private Integer productId;
    private Double rating;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
