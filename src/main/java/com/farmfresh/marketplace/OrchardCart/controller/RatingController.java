package com.farmfresh.marketplace.OrchardCart.controller;

import com.farmfresh.marketplace.OrchardCart.dto.request.RatingRequest;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.UserInfo;
import com.farmfresh.marketplace.OrchardCart.repository.UserInfoRepository;
import com.farmfresh.marketplace.OrchardCart.service.AuthenticationService;
import com.farmfresh.marketplace.OrchardCart.service.RatingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class RatingController {

    private final RatingService ratingService;
    private final AuthenticationService authenticationService;

    public RatingController(RatingService ratingService, AuthenticationService authenticationService) {
        this.ratingService = ratingService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/submit-rating")
    public String addRating(@RequestParam Integer productId,
                            @RequestParam double rating, HttpServletRequest request) throws ElementNotFoundException {
        UserInfo user = authenticationService.getAuthUser().orElseThrow(()->new ElementNotFoundException("User not signed in"));
        RatingRequest ratingRequest = new RatingRequest();
        ratingRequest.setProductId(productId);
        ratingRequest.setRating(rating);
        ratingService.createRating(ratingRequest, user);
        String referer = request.getHeader("Referer");
        return "redirect:"+referer;
    }
}

