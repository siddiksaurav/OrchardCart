package com.farmfresh.marketplace.OrchardCart.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


public class CategoryRequest {
    private String categoryName;
    private String imageUrl;
    private MultipartFile imageFile;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }
}
