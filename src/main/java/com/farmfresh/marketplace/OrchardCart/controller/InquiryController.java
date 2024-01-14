package com.farmfresh.marketplace.OrchardCart.controller;

import com.farmfresh.marketplace.OrchardCart.dto.request.ProductInquiry;
import com.farmfresh.marketplace.OrchardCart.service.QueryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/inquiry")
public class InquiryController {
    @Autowired
    private QueryService queryService;

    @GetMapping("/products/{id}")
    public String showInquiryForm(@PathVariable Long id, Model model) {
        model.addAttribute("productId", id);
        model.addAttribute("productInquiry", new ProductInquiry());
        return "inquiry/inquiry";
    }

    @PostMapping("/products/{id}/submit")
    public String submitInquiry(@PathVariable Integer id, @Valid @ModelAttribute ProductInquiry productInquiry, BindingResult result) {
        if (result.hasErrors()) {
            return "inquiry/inquiry";
        }
        queryService.sendQuery(id, productInquiry);
        return "inquiry-success";
    }
}

