package com.farmfresh.marketplace.OrchardCart.controller;
import com.farmfresh.marketplace.OrchardCart.dto.request.CategoryRequest;
import com.farmfresh.marketplace.OrchardCart.dto.response.CategoryResponse;
import com.farmfresh.marketplace.OrchardCart.exception.ElementAlreadyExistException;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/all")
    public String showAllCategories(Model model) {
        List<CategoryResponse> categories = categoryService.getCategoryList();
        model.addAttribute("categories", categories);
        return "category/category-list";
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/create")
    public String createCategoryForm(Model model) {
        model.addAttribute("categoryRequest",new CategoryRequest());
        return "category/category-create";
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public String addCategory(@Valid @ModelAttribute("categoryRequest") CategoryRequest categoryRequest) throws ElementAlreadyExistException, IOException {
        categoryService.addCategory(categoryRequest);
        return "redirect:/category/all";
    }

    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable Integer id, Model model) throws ElementNotFoundException {
        CategoryResponse category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "category/category-edit";
    }

    @PostMapping("/update/{id}")
    public String updateCategory(@PathVariable Integer id, @RequestBody CategoryRequest categoryRequest) throws ElementNotFoundException {
        categoryService.updateCategory(id,categoryRequest);
        return "redirect:/category/all";
    }

    @GetMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Integer id, Model model) throws ElementNotFoundException {
        CategoryResponse category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        return "category/category-delete";
    }

    @PostMapping("/delete/{id}")
    public String deleteCategoryConfirm(@PathVariable Integer id) {
        categoryService.deleteCategoryById(id);
        return "redirect:/category/all";
    }
}
