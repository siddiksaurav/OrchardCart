package com.farmfresh.marketplace.OrchardCart.controller;

import com.farmfresh.marketplace.OrchardCart.model.Category;
import com.farmfresh.marketplace.OrchardCart.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/all")
    public List<Category> showAllCategories(){
        return categoryService.getCategoryList();
    }
}
