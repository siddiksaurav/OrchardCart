package com.farmfresh.marketplace.OrchardCart.dto.mapper;
import com.farmfresh.marketplace.OrchardCart.dto.request.ProductRequest;
import com.farmfresh.marketplace.OrchardCart.dto.response.ProductResponse;
import com.farmfresh.marketplace.OrchardCart.model.Product;
import com.farmfresh.marketplace.OrchardCart.model.Rating;
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
        modelMapper.createTypeMap(Product.class, ProductRequest.class)
                .addMapping(src -> src.getCategory().getCategoryName(), ProductRequest::setCategoryName)
                .addMapping(src -> src.getSeller().getBusinessName(), ProductRequest::setBusinessName);
    }
    public ProductResponse mapToResponse(Product product) {
        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
        double sum = product.getRatings().stream().mapToDouble(Rating::getRating).sum();
        double avgRating = sum / product.getRatings().size();
        productResponse.setRating(avgRating);
        return productResponse;
    }
    public ProductRequest mapToRequest(Product product) {
        return modelMapper.map(product, ProductRequest.class);
    }
}

