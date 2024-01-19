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
    private final UserInfoRepository userInfoRepository;
    public RatingController(RatingService ratingService, AuthenticationService authenticationService, UserInfoRepository userInfoRepository) {
        this.ratingService = ratingService;
        this.authenticationService = authenticationService;
        this.userInfoRepository = userInfoRepository;
    }

    @PostMapping("/submit-rating")
    public String addRating(@RequestParam Integer productId,
                            @RequestParam double rating, HttpServletRequest request) throws ElementNotFoundException {
        String userEmail = authenticationService.getAuthUser();
        UserInfo user = userInfoRepository.findByEmail(userEmail).orElseThrow(()->new ElementNotFoundException("User not found with email:"+userEmail));
        RatingRequest ratingRequest = new RatingRequest();
        ratingRequest.setProductId(productId);
        ratingRequest.setRating(rating);
        ratingService.createRating(ratingRequest, user);
        String referer = request.getHeader("Referer");
        return "redirect:"+referer;
    }
}

