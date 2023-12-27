package com.farmfresh.marketplace.OrchardCart.dto.mapper;
import com.farmfresh.marketplace.OrchardCart.dto.response.ProductResponse;
import com.farmfresh.marketplace.OrchardCart.model.Product;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    private final ModelMapper modelMapper;

    public ProductMapper() {
        this.modelMapper = new ModelMapper();
        configureMapper();
    }

    private void configureMapper() {
        modelMapper.createTypeMap(Product.class, ProductResponse.class)
                .addMapping(src -> src.getCategory().getCategoryName(), ProductResponse::setCategoryName)
                .addMapping(src -> src.getSeller().getBusinessName(), ProductResponse::setBusinessName);
    }
    public ProductResponse mapToResponse(Product product) {
        return modelMapper.map(product, ProductResponse.class);
    }
}

