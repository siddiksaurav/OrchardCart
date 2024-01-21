package com.farmfresh.marketplace.OrchardCart.service;

import com.farmfresh.marketplace.OrchardCart.dto.request.RatingRequest;
import com.farmfresh.marketplace.OrchardCart.dto.request.ReviewRequest;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.Product;
import com.farmfresh.marketplace.OrchardCart.model.Rating;
import com.farmfresh.marketplace.OrchardCart.model.Review;
import com.farmfresh.marketplace.OrchardCart.model.UserInfo;
import com.farmfresh.marketplace.OrchardCart.repository.ProductRepository;
import com.farmfresh.marketplace.OrchardCart.repository.RatingRepository;
import com.farmfresh.marketplace.OrchardCart.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    public ReviewService(ReviewRepository reviewRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
    }

    public Review createReview(ReviewRequest reviewRequest, UserInfo user) throws ElementNotFoundException {
        Product product = productRepository.findById(reviewRequest.getProductId()).orElseThrow(()->new ElementNotFoundException("product not found with id:"+reviewRequest.getProductId()));
        Review review =new Review();
        review.setProduct(product);
        review.setReview(reviewRequest.getReview());
        review.setUser(user);
        review.setCreateTime(LocalDateTime.now());
        return  reviewRepository.save(review);
    }

    public List<Review> getProductReviews(Integer productId){
        return reviewRepository.findReviewsByProductId(productId);
    }
}
