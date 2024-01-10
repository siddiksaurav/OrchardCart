package com.farmfresh.marketplace.OrchardCart.service;

import com.farmfresh.marketplace.OrchardCart.dto.request.RatingRequest;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.Product;
import com.farmfresh.marketplace.OrchardCart.model.Rating;
import com.farmfresh.marketplace.OrchardCart.model.UserInfo;
import com.farmfresh.marketplace.OrchardCart.repository.ProductRepository;
import com.farmfresh.marketplace.OrchardCart.repository.RatingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingService {
    private final RatingRepository ratingRepository;
    private final ProductRepository productRepository;
    private final JwtService jwtService;
    public Rating createRating(RatingRequest ratingRequest, String userToken) throws ElementNotFoundException {
        Product product = productRepository.findById(ratingRequest.getProductId()).orElseThrow(()->new ElementNotFoundException("product not found with id:"+ratingRequest.getProductId()));
        UserInfo user = jwtService.getUserByToken(userToken);
        Rating rating = new Rating();
        rating.setProduct(product);
        rating.setRating(ratingRequest.getRating());
        rating.setUser(user);
        rating.setCreateTime(LocalDateTime.now());
        return  ratingRepository.save(rating);
    }

    public List<Rating> getProductRatings(Integer productId){
        return ratingRepository.findRatingsByProductId(productId);
    }
}