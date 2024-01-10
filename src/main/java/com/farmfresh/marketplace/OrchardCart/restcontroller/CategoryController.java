package com.farmfresh.marketplace.OrchardCart.restcontroller;

import com.farmfresh.marketplace.OrchardCart.dto.response.CategoryResponse;
import com.farmfresh.marketplace.OrchardCart.exception.ElementAlreadyExistException;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@EnableMethodSecurity
@RequestMapping("/api/vi/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/all")
    public List<CategoryResponse> showAllCategories(){
        return categoryService.getCategoryList();
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public String addCategory(@RequestParam String categoryName) throws ElementAlreadyExistException {
        return categoryService.addCategory(categoryName);
    }
    @GetMapping("/")
    public CategoryResponse getCategoryByCategoryName(@RequestParam String categoryName) throws ElementNotFoundException {
        return categoryService.getCategory(categoryName);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public String deleteCategoryById(@PathVariable Integer id){
        categoryService.deleteCategoryById(id);
        return "Deleted category successfully";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public String updateCategoryByCategoryName(@PathVariable Integer id, @RequestParam String categoryName) throws ElementNotFoundException {
        return categoryService.updateCategoryByCategoryName(id,categoryName);
    }
}
