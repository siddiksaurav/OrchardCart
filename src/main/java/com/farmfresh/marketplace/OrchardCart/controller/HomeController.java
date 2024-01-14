package com.farmfresh.marketplace.OrchardCart.controller;

import com.farmfresh.marketplace.OrchardCart.dto.response.CategoryResponse;
import com.farmfresh.marketplace.OrchardCart.dto.response.ProductResponse;
import com.farmfresh.marketplace.OrchardCart.service.CategoryService;
import com.farmfresh.marketplace.OrchardCart.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {

    private final CategoryService categoryService;
    private final ProductService productService;



    @GetMapping
    public String home(Model model) {
        List<CategoryResponse> categories = categoryService.getCategoryList();
        List<ProductResponse> products = productService.getProductList();
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        return "homepage";
    }
}
