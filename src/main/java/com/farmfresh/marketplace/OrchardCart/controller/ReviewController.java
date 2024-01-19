package com.farmfresh.marketplace.OrchardCart.controller;

import com.farmfresh.marketplace.OrchardCart.dto.request.RatingRequest;
import com.farmfresh.marketplace.OrchardCart.dto.request.ReviewRequest;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.UserInfo;
import com.farmfresh.marketplace.OrchardCart.repository.UserInfoRepository;
import com.farmfresh.marketplace.OrchardCart.service.AuthenticationService;
import com.farmfresh.marketplace.OrchardCart.service.RatingService;
import com.farmfresh.marketplace.OrchardCart.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class ReviewController {

    private final ReviewService reviewService;
    private final AuthenticationService authenticationService;
    private final UserInfoRepository userInfoRepository;
    public ReviewController(ReviewService reviewService, AuthenticationService authenticationService, UserInfoRepository userInfoRepository) {
        this.reviewService = reviewService;
        this.authenticationService = authenticationService;
        this.userInfoRepository = userInfoRepository;
    }

    //IHow to send ratingRequest instead of parameters.
    @PostMapping("/submit-review")
    public String addRating(@RequestParam Integer productId,
                            @RequestParam String review) throws ElementNotFoundException {
        String userEmail = authenticationService.getAuthUser();
        UserInfo user = userInfoRepository.findByEmail(userEmail).orElseThrow(()->new ElementNotFoundException("User not found with email:"+userEmail));
        ReviewRequest reviewRequest = new ReviewRequest();
        reviewRequest.setProductId(productId);
        reviewRequest.setReview(review);
        reviewService.createReview(reviewRequest, user);
        return "redirect:/order-history";
    }
}