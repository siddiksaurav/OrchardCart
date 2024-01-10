package com.farmfresh.marketplace.OrchardCart.service;

import com.farmfresh.marketplace.OrchardCart.dto.request.ProductInquiry;
import com.farmfresh.marketplace.OrchardCart.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueryService {
    private final EmailSendingService emailSendingService;
    private final ProductRepository productRepository;

    public String sendQuery(Integer id, ProductInquiry productQuery) {
        String toEmail = productRepository.findSellerEmailByProductId(id);
        String subject ="Query for product name:"+productQuery.getProductName();
        return emailSendingService.sendSimpleEmail(toEmail,productQuery.getUserEmail(),productQuery.getMessage(),subject);
    }
}
