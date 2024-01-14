package com.farmfresh.marketplace.OrchardCart.service;

import com.farmfresh.marketplace.OrchardCart.dto.request.ProductRequest;
import com.farmfresh.marketplace.OrchardCart.dto.response.ProductResponse;
import com.farmfresh.marketplace.OrchardCart.dto.mapper.ProductMapper;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.Category;
import com.farmfresh.marketplace.OrchardCart.model.Product;
import com.farmfresh.marketplace.OrchardCart.model.Seller;
import com.farmfresh.marketplace.OrchardCart.repository.CategoryRepository;
import com.farmfresh.marketplace.OrchardCart.repository.ProductRepository;
import com.farmfresh.marketplace.OrchardCart.repository.SellerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    private final SellerRepository sellerRepository;

    private final CategoryRepository categoryRepository;

    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, SellerRepository sellerRepository, CategoryRepository categoryRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
    }
    //todo use relative path
    private static final String uploadPath = "/home/saurav/Downloads/Java-Spring/OrchardCart/src/main/resources/static/img/";
    private Logger log = LoggerFactory.getLogger(ProductService.class);
    public List<ProductResponse> getProductList() {
        List<Product> products = productRepository.findAll();
        for(Product product: products) {
            log.info(product.getImageURL());
        }
        return products.stream()
                .map(productMapper::mapToResponse) // Using ProductMapper to convert Product to ProductResponse
                .collect(Collectors.toList());
    }

    @Transactional
    public String addProduct(ProductRequest productRequest) throws ElementNotFoundException, IOException {
        Seller seller = sellerRepository.findByBusinessName(productRequest.getBusinessName())
                .orElseThrow(() -> new ElementNotFoundException("Seller not found with business name: " + productRequest.getBusinessName()));
        Category category = categoryRepository.findByCategoryName(productRequest.getCategoryName())
                .orElseThrow(() -> new ElementNotFoundException("Category not found with category name: " + productRequest.getCategoryName()));
        Product product = new Product();
        product.setImageURL("/img/default.png");
        if(productRequest.getImageFile()!=null && !productRequest.getImageFile().isEmpty()){
            MultipartFile imageFile = productRequest.getImageFile();
            File directory= new File(uploadPath);

            if (!directory.exists()) {
                directory.mkdirs();
            }
            String originalFileName = imageFile.getOriginalFilename();
            String imageUrl = uploadPath + originalFileName;

            File destinationFile = new File(imageUrl);
            imageFile.transferTo(destinationFile);

            product.setImageURL("/img/"+originalFileName);
        }
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());
        product.setCategory(category);
        product.setSeller(seller);
        category.getProducts().add(product);
        categoryRepository.save(category);
        productRepository.save(product);
        return "success";
    }

    public ProductResponse getProductById(Integer id) throws ElementNotFoundException {
        Product product =productRepository.findById(id).orElseThrow(() -> new ElementNotFoundException("Product not found with id:"+id));
        return productMapper.mapToResponse(product);
    }

    public void deleteProductById(Integer id){
        productRepository.deleteById(id);
    }


    @Transactional
    public String updateProductById(ProductResponse product) throws ElementNotFoundException, AccessDeniedException {
        Product existingProduct = productRepository.findById(product.getId()).orElseThrow(() -> new NoSuchElementException("Product not found with id:"+product.getId()));
        Seller seller = sellerRepository.findByBusinessName(product.getBusinessName())
                .orElseThrow(() -> new ElementNotFoundException("Seller not found with business name: " + product.getBusinessName()));
        Category category = categoryRepository.findByCategoryName(product.getCategoryName())
                .orElseThrow(() -> new ElementNotFoundException("Category not found with category name: " + product.getCategoryName()));

        if (!Objects.equals(existingProduct.getSeller(), seller)) {
            throw new AccessDeniedException("You are not authorized to update this product");
        }

        if (!Objects.equals(existingProduct.getCategory(), category)) {
            Category previousCategory = existingProduct.getCategory();
            previousCategory.getProducts().remove(existingProduct);
            existingProduct.setCategory(category);
            category.getProducts().add(existingProduct);
            categoryRepository.save(category);
            categoryRepository.save(previousCategory);
        }
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setSeller(seller);

        productRepository.save(existingProduct);

        return "Updated product Successfully";
    }

}
