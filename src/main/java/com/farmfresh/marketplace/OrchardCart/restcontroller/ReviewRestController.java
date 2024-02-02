package com.farmfresh.marketplace.OrchardCart.restcontroller;

import com.farmfresh.marketplace.OrchardCart.dto.request.ReviewRequest;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.Review;
import com.farmfresh.marketplace.OrchardCart.model.UserInfo;
import com.farmfresh.marketplace.OrchardCart.service.JwtService;
import com.farmfresh.marketplace.OrchardCart.service.ReviewService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/review")
public class ReviewRestController {

    private final ReviewService reviewService;
    private final JwtService jwtService;

    public ReviewRestController(ReviewService reviewService, JwtService jwtService) {
        this.reviewService = reviewService;
        this.jwtService = jwtService;
    }

    @PostMapping("/create")
    public Review addRating(@RequestHeader("Authorization") String userToken, ReviewRequest reviewRequest) throws ElementNotFoundException {
        UserInfo user = jwtService.getUserByToken(userToken);
        return reviewService.createReview(reviewRequest, user);
    }

    @GetMapping("/product/{producid}")
    public List<Review> getProductReviews(@PathVariable Integer productId) {
        return reviewService.getProductReviews(productId);
    }
}
