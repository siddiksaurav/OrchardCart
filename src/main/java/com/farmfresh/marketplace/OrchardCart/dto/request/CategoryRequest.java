package com.farmfresh.marketplace.OrchardCart.dto.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CategoryRequest {
    private String categoryName;
    private String imageUrl;
    private MultipartFile imageFile;
}
