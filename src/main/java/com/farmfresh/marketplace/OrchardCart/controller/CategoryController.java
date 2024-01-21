package com.farmfresh.marketplace.OrchardCart.controller;
import com.farmfresh.marketplace.OrchardCart.dto.request.CategoryRequest;
import com.farmfresh.marketplace.OrchardCart.dto.response.CategoryResponse;
import com.farmfresh.marketplace.OrchardCart.exception.ElementAlreadyExistException;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {
    private static final String REDIRECT_CATEGORY_LIST = "redirect:/products/list";
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/list")
    public String CategoryList(Model model) {
        List<CategoryResponse> categories = categoryService.getCategoryList();
        model.addAttribute("categories", categories);
        return REDIRECT_CATEGORY_LIST;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/create")
    public String createCategoryForm(Model model) {
        model.addAttribute("categoryRequest",new CategoryRequest());
        return "category/category-create";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public String addCategory(@Valid CategoryRequest categoryRequest) throws IOException {
        categoryService.addCategory(categoryRequest);
        return REDIRECT_CATEGORY_LIST;
    }

    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable Integer id, Model model){
        CategoryResponse category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "category/category-edit";
    }

    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable Integer id, @RequestBody CategoryRequest categoryRequest){
        categoryService.updateCategory(id,categoryRequest);
        return REDIRECT_CATEGORY_LIST;
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Integer id, Model model){
        CategoryResponse category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "category/category-delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategoryConfirm(@PathVariable Integer id) {
        categoryService.deleteCategoryById(id);
        return REDIRECT_CATEGORY_LIST;
    }
}
