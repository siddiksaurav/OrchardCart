package com.farmfresh.marketplace.OrchardCart.service;

import com.farmfresh.marketplace.OrchardCart.model.Category;
import com.farmfresh.marketplace.OrchardCart.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    public List<Category> getCategoryList(){
        return categoryRepository.findAll();
    }
}
