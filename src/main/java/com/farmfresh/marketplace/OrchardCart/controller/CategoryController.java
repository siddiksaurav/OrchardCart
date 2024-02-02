package com.farmfresh.marketplace.OrchardCart.controller;

import com.farmfresh.marketplace.OrchardCart.dto.request.CategoryRequest;
import com.farmfresh.marketplace.OrchardCart.dto.response.CategoryResponse;
import com.farmfresh.marketplace.OrchardCart.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {
    private static final String REDIRECT_CATEGORY_LIST = "redirect:/category/list";
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public String CategoryList(Model model) {
        List<CategoryResponse> categories = categoryService.getCategoryList();
        model.addAttribute("categories", categories);
        return "category/category-list";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/create")
    public String createCategoryForm(Model model) {
        model.addAttribute("categoryRequest", new CategoryRequest());
        return "category/category-create";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public String addCategory(@Valid CategoryRequest categoryRequest, BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return "category/create";
        }
        categoryService.addCategory(categoryRequest);
        return REDIRECT_CATEGORY_LIST;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable Integer id, Model model) {
        CategoryResponse category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "category/category-edit";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable Integer id, @RequestBody CategoryRequest categoryRequest) {
        categoryService.updateCategory(id, categoryRequest);
        return REDIRECT_CATEGORY_LIST;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Integer id, Model model) {
        CategoryResponse category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "category/category-delete";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/delete/{id}")
    public String deleteCategoryConfirm(@PathVariable Integer id) {
        categoryService.deleteCategoryById(id);
        return REDIRECT_CATEGORY_LIST;
    }
}
