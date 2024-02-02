package com.farmfresh.marketplace.OrchardCart.dto.mapper;

import com.farmfresh.marketplace.OrchardCart.dto.response.CategoryResponse;
import com.farmfresh.marketplace.OrchardCart.model.Category;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    private final ModelMapper modelMapper;

    public CategoryMapper() {
        this.modelMapper = new ModelMapper();
    }

    public CategoryResponse mapToResponse(Category category) {
        return modelMapper.map(category, CategoryResponse.class);
    }
}

