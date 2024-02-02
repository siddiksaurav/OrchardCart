package com.farmfresh.marketplace.OrchardCart.controller;

import com.farmfresh.marketplace.OrchardCart.dto.request.ProductInquiry;
import com.farmfresh.marketplace.OrchardCart.service.QueryService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/inquiry")
public class InquiryController {

    private final QueryService queryService;

    public InquiryController(QueryService queryService) {
        this.queryService = queryService;
    }

    @GetMapping("/products/{id}")
    public String showInquiryForm(@PathVariable Integer id, Model model) {
        model.addAttribute("productId", id);
        model.addAttribute("productInquiry", new ProductInquiry());
        return "inquiry/inquiry";
    }

    @PostMapping("/products/{id}/submit")
    public String submitInquiry(@PathVariable Integer id, @Valid ProductInquiry productInquiry, BindingResult result) {
        if (result.hasErrors()) {
            return "inquiry/inquiry";
        }
        queryService.sendQuery(id, productInquiry);
        return "inquiry-success";
    }
}

