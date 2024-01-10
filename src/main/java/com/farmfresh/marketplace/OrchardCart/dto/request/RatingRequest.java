package com.farmfresh.marketplace.OrchardCart.dto.request;

import lombok.Data;

@Data
public class RatingRequest {
    private Integer productId;
    private Double rating;
}
