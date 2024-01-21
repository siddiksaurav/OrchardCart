package com.farmfresh.marketplace.OrchardCart.service;

import com.farmfresh.marketplace.OrchardCart.dto.mapper.CategoryMapper;
import com.farmfresh.marketplace.OrchardCart.dto.request.CategoryRequest;
import com.farmfresh.marketplace.OrchardCart.dto.response.CategoryResponse;
import com.farmfresh.marketplace.OrchardCart.exception.ElementAlreadyExistException;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.Category;
import com.farmfresh.marketplace.OrchardCart.model.Product;
import com.farmfresh.marketplace.OrchardCart.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final ImageService imageService;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper, ImageService imageService) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
        this.imageService = imageService;
    }

    public List<CategoryResponse> getCategoryList(){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::mapToResponse)
                .collect(Collectors.toList());
    }


    public String addCategory(CategoryRequest categoryRequest) throws IOException {
        Optional<Category> category = categoryRepository.findByCategoryName(categoryRequest.getCategoryName());
        if(category.isPresent()){
            throw new ElementAlreadyExistException("Category already exist with "+categoryRequest.getCategoryName());
        }
        Category newCategory = new Category();
        newCategory.setCategoryName(categoryRequest.getCategoryName());
        if(categoryRequest.getImageFile()!=null && !categoryRequest.getImageFile().isEmpty()){
            MultipartFile imageFile = categoryRequest.getImageFile();
            String imageUrl = imageService.saveImage(imageFile, "categories");
            newCategory.setImageUrl(imageUrl);
        }
        categoryRepository.save(newCategory);
        return "success";
    }

    public CategoryResponse getCategory(String categoryName) {
        Category category = categoryRepository.findByCategoryName(categoryName).orElseThrow(()->new ElementNotFoundException("Category not found with name:"+categoryName));
        return categoryMapper.mapToResponse(category);
    }

    public CategoryResponse getCategoryById(Integer id) {
        Category category = categoryRepository.findById(id).orElseThrow(()->new ElementNotFoundException("Category not found with id:"+id));
        return categoryMapper.mapToResponse(category);
    }

    public void deleteCategoryById(Integer id) {
        if(categoryRepository.existsById(id)){
            categoryRepository.deleteById(id);
        }
    }

    public String updateCategory(Integer id, CategoryRequest categoryRequest){
        Category existingCategory = categoryRepository.findById(id).orElseThrow(()->new ElementNotFoundException("Category not exist with id:"+id));
        existingCategory.setCategoryName(categoryRequest.getCategoryName());
        categoryRepository.save(existingCategory);
        return "Successfully Updated";
    }
    @Transactional
    public void updateProductCategory(Product product, Category newCategory) {
        Category previousCategory = product.getCategory();
        previousCategory.getProducts().remove(product);
        product.setCategory(newCategory);
        newCategory.getProducts().add(product);
        categoryRepository.save(newCategory);
        categoryRepository.save(previousCategory);
    }
}
