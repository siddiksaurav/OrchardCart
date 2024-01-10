package com.farmfresh.marketplace.OrchardCart.restcontroller;

import com.farmfresh.marketplace.OrchardCart.dto.request.ProductInquiry;
import com.farmfresh.marketplace.OrchardCart.service.QueryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1//inquiry")
public class InquiryController {
    @Autowired
    private QueryService queryService;
    @PostMapping ("/products/{id}")
    public String InquiryOverEmail(@PathVariable Integer id, @Valid @RequestBody ProductInquiry productInquiry){
        return queryService.sendQuery(id,productInquiry);
   }
}
