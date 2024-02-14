package com.farmfresh.marketplace.OrchardCart.service;

import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.Product;
import com.farmfresh.marketplace.OrchardCart.model.Seller;
import com.farmfresh.marketplace.OrchardCart.model.UserInfo;
import com.farmfresh.marketplace.OrchardCart.repository.SellerRepository;
import com.farmfresh.marketplace.OrchardCart.repository.UserInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final AuthenticationService authenticationService;
    private final UserInfoRepository userInfoRepository;
    private final SellerRepository sellerRepository;

    public UserService(AuthenticationService authenticationService, UserInfoRepository userInfoRepository, SellerRepository sellerRepository, SellerRepository sellerRepository1) {
        this.authenticationService = authenticationService;
        this.userInfoRepository = userInfoRepository;
        this.sellerRepository = sellerRepository1;
    }

    public UserInfo getUserDetails() {
        return authenticationService.getAuthUser().orElseThrow(() -> new ElementNotFoundException("User not logged in"));
    }

    public void updateUserInfo(UserInfo user) {
        userInfoRepository.save(user);
    }
    public Seller getSellerDetails() {
        return sellerRepository.findByUserInfo(getUserDetails());
    }

    public void updateSellerInfo(Seller seller){
        sellerRepository.save(seller);
    }

    public List<Product> getSellerProducts() {
        Seller seller = getSellerDetails();
        return seller.getProducts();
    }


}
