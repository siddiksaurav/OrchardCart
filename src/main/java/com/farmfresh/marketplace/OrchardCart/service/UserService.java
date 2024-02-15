package com.farmfresh.marketplace.OrchardCart.service;

import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.Address;
import com.farmfresh.marketplace.OrchardCart.model.Product;
import com.farmfresh.marketplace.OrchardCart.model.Seller;
import com.farmfresh.marketplace.OrchardCart.model.UserInfo;
import com.farmfresh.marketplace.OrchardCart.repository.SellerRepository;
import com.farmfresh.marketplace.OrchardCart.repository.UserInfoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Transactional
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
    @Transactional
    public void updateSellerInfo(Seller updatedSeller){
        Seller existingSeller = getSellerDetails();
        UserInfo user = existingSeller.getUserInfo();
        Address address = existingSeller.getAddress();
        user.setFirstname(updatedSeller.getUserInfo().getFirstname());
        user.setLastname(updatedSeller.getUserInfo().getLastname());
        user.setEmail(updatedSeller.getUserInfo().getEmail());
        address.setDistrict(updatedSeller.getAddress().getDistrict());
        address.setCity(updatedSeller.getAddress().getCity());
        address.setAdditionalAddress(updatedSeller.getAddress().getAdditionalAddress());
        address.setPhoneNumber(updatedSeller.getAddress().getPhoneNumber());
        existingSeller.setBusinessName(updatedSeller.getBusinessName());
        existingSeller.setDescription(updatedSeller.getDescription());
        existingSeller.setAddress(address);
        existingSeller.setUserInfo(user);
        sellerRepository.save(existingSeller);
    }

    public List<Product> getSellerProducts() {
        Seller seller = getSellerDetails();
        return seller.getProducts();
    }


}
