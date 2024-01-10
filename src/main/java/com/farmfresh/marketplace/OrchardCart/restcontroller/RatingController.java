package com.farmfresh.marketplace.OrchardCart.restcontroller;

import com.farmfresh.marketplace.OrchardCart.dto.request.RatingRequest;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.Rating;
import com.farmfresh.marketplace.OrchardCart.service.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rating")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;
    @PostMapping("/create")
    public Rating addRating(@RequestHeader("Authorization") String userToken, RatingRequest ratingRequest) throws ElementNotFoundException {
        return ratingService.createRating(ratingRequest,userToken);
    }
    @GetMapping("/product/{producid}")
    public List<Rating> getProductRatings(@PathVariable Integer productId){
        return ratingService.getProductRatings(productId);
    }
}
