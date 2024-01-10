package com.farmfresh.marketplace.OrchardCart.restcontroller;

import com.farmfresh.marketplace.OrchardCart.dto.request.RatingRequest;
import com.farmfresh.marketplace.OrchardCart.dto.request.ReviewRequest;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.Rating;
import com.farmfresh.marketplace.OrchardCart.model.Review;
import com.farmfresh.marketplace.OrchardCart.service.RatingService;
import com.farmfresh.marketplace.OrchardCart.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;
    @PostMapping("/create")
    public Review addRating(@RequestHeader("Authorization") String userToken, ReviewRequest reviewRequest) throws ElementNotFoundException {
        return reviewService.createReview(reviewRequest,userToken);
    }
    @GetMapping("/product/{producid}")
    public List<Review> getProductReviews(@PathVariable Integer productId){
        return reviewService.getProductReviews(productId);
    }
}
