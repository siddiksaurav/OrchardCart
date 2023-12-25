package com.farmfresh.marketplace.OrchardCart.service;

import com.farmfresh.marketplace.OrchardCart.dto.CategoryRequest;
import com.farmfresh.marketplace.OrchardCart.model.Category;
import com.farmfresh.marketplace.OrchardCart.model.Product;
import com.farmfresh.marketplace.OrchardCart.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    public List<Category> getCategoryList(){
        return categoryRepository.findAll();
    }


    public String addCategory(CategoryRequest categoryRequest) {
        Category category = new Category();
        category.setCategoryName(categoryRequest.getCategoryName());
        categoryRepository.save(category);
        return "success";
    }

    public Category getCategory(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName);
    }

    public void deleteCategoryById(Long id) {
        if(categoryRepository.existsById(id)){
            categoryRepository.deleteById(id);
        }
    }

    public String updateCategoryByCategoryName(CategoryRequest categoryRequest) {
        Category existingCategory = categoryRepository.findByCategoryName(categoryRequest.getCategoryName());
        if (existingCategory != null) {
            existingCategory.setCategoryName(categoryRequest.getCategoryName());
            categoryRepository.save(existingCategory);
            return "Successfully updated";
        } else {
            throw new NoSuchElementException("Category not found");
        }
    }
}
