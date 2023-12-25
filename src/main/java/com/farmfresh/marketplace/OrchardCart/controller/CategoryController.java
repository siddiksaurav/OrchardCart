package com.farmfresh.marketplace.OrchardCart.controller;

import com.farmfresh.marketplace.OrchardCart.dto.CategoryRequest;
import com.farmfresh.marketplace.OrchardCart.model.Category;
import com.farmfresh.marketplace.OrchardCart.model.Product;
import com.farmfresh.marketplace.OrchardCart.service.CategoryService;
import com.farmfresh.marketplace.OrchardCart.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@EnableMethodSecurity
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/all")
    public List<Category> showAllCategories(){
        return categoryService.getCategoryList();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public String addCategory(@RequestBody CategoryRequest categoryRequest){
        return categoryService.addCategory(categoryRequest);
    }
    @GetMapping("/{categoryName}")
    public Category showCategoryByCategoryName(@PathVariable String categoryName){
        return categoryService.getCategory(categoryName);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteCategoryById(@PathVariable Long id){
        categoryService.deleteCategoryById(id);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public String updateCategoryByCategoryName(@Valid @RequestBody CategoryRequest categoryRequest){
        return categoryService.updateCategoryByCategoryName(categoryRequest);
    }
}
