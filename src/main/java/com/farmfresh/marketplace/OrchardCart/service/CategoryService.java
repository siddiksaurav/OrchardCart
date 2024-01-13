package com.farmfresh.marketplace.OrchardCart.service;

import com.farmfresh.marketplace.OrchardCart.dto.mapper.CategoryMapper;
import com.farmfresh.marketplace.OrchardCart.dto.request.CategoryRequest;
import com.farmfresh.marketplace.OrchardCart.dto.response.CategoryResponse;
import com.farmfresh.marketplace.OrchardCart.exception.ElementAlreadyExistException;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.Category;
import com.farmfresh.marketplace.OrchardCart.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;
    private static final String uploadPath = "/home/saurav/Downloads/Java-Spring/OrchardCart/src/main/resources/static/img/";
    public List<CategoryResponse> getCategoryList(){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::mapToResponse)
                .collect(Collectors.toList());
    }


    public String addCategory(CategoryRequest categoryRequest) throws ElementAlreadyExistException, IOException {
        Optional<Category> category = categoryRepository.findByCategoryName(categoryRequest.getCategoryName());
        if(category.isPresent()){
            throw new ElementAlreadyExistException("Category already exist with "+categoryRequest.getCategoryName());
        }
        Category newCategory = new Category();
        newCategory.setCategoryName(categoryRequest.getCategoryName());
        if(categoryRequest.getImageFile()!=null && !categoryRequest.getImageFile().isEmpty()){
            MultipartFile imageFile = categoryRequest.getImageFile();
            File directory= new File(uploadPath);

            if (!directory.exists()) {
                directory.mkdirs();
            }
            String originalFileName = imageFile.getOriginalFilename();
            String imageUrl = uploadPath + originalFileName;

            File destinationFile = new File(imageUrl);
            imageFile.transferTo(destinationFile);

            newCategory.setImageUrl("/img/"+originalFileName);
        }
        categoryRepository.save(newCategory);
        return "success";
    }

    public CategoryResponse getCategory(String categoryName) throws ElementNotFoundException {
        Category category = categoryRepository.findByCategoryName(categoryName).orElseThrow(()->new ElementNotFoundException("Category not found with name:"+categoryName));
        return categoryMapper.mapToResponse(category);
    }

    public CategoryResponse getCategoryById(Integer id) throws ElementNotFoundException {
        Category category = categoryRepository.findById(id).orElseThrow(()->new ElementNotFoundException("Category not found with id:"+id));
        return categoryMapper.mapToResponse(category);
    }

    public void deleteCategoryById(Integer id) {
        if(categoryRepository.existsById(id)){
            categoryRepository.deleteById(id);
        }
    }

    public String updateCategoryByCategoryName(Integer id,String categoryName) throws ElementNotFoundException {
        Category existingCategory = categoryRepository.findById(id).orElseThrow(()->new ElementNotFoundException("Category not exist with id:"+id));
        existingCategory.setCategoryName(categoryName);
        categoryRepository.save(existingCategory);
        return "Successfully updated";
    }
}
