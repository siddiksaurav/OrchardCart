package com.farmfresh.marketplace.OrchardCart.controller;

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
    @Autowired
    private ProductService productService;

    @GetMapping("/all")
    public List<Category> showAllCategories(){
        return categoryService.getCategoryList();
    }
    @PreAuthorize("hasAnyRole('ROLE_SELLER','ROLE_ADMIN')")
    @PostMapping("/create")
    public String addCategory(@RequestBody Category category){
        return categoryService.addCategory(category);
    }
    @GetMapping("/{id}")
    public Optional<Category> showCategoryById(@PathVariable Long id){
        return categoryService.getCategoryById(id);
    }

    @GetMapping("/{categoryId}/products")
    public Optional<Product> showProductByCategoryId(@PathVariable Long categoryId){
        return productService.getProductByCategoryId(categoryId);
    }
    @DeleteMapping("/{id}")
    public void deleteCategoryById(@PathVariable Long id){
        categoryService.deleteCategoryById(id);
    }
    @PutMapping("/{id}")
    public Category updateCategoryById(@PathVariable Long id, @Valid @RequestBody Category category){
        return categoryService.updateCategoryById(id,category);
    }
}
