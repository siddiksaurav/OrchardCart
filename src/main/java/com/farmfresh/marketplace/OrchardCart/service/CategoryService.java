package com.farmfresh.marketplace.OrchardCart.service;

import com.farmfresh.marketplace.OrchardCart.model.Category;
import com.farmfresh.marketplace.OrchardCart.model.Product;
import com.farmfresh.marketplace.OrchardCart.repository.CategoryRepository;
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


    public String addCategory(Category category) {
        categoryRepository.save(category);
        return "success";
    }

    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    public void deleteCategoryById(Long id) {
        if(categoryRepository.existsById(id)){
            categoryRepository.deleteById(id);
        }
    }

    public Category updateCategoryById(Long id, Category category) {
        Category existingCategory = categoryRepository.findById(id).orElseThrow(()-> new NoSuchElementException("Category not found"));
        existingCategory.setCategoryName(category.getCategoryName());
        categoryRepository.save(existingCategory);
        return existingCategory;
    }
}
