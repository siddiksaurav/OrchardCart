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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public  static String DEFAULT_PRODUCT_URL="/img/default.png";
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final ImageService imageService;
    private final AuthenticationService authenticationService;
    public ProductService(ProductRepository productRepository, SellerRepository sellerRepository, CategoryService categoryService, CategoryRepository categoryRepository, ProductMapper productMapper, ImageService imageService, AuthenticationService authenticationService) {
        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
        this.productMapper = productMapper;
        this.imageService = imageService;
        this.authenticationService = authenticationService;
    }

    private Logger log = LoggerFactory.getLogger(ProductService.class);
    public List<ProductResponse> getProductList() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public String addProduct(ProductRequest productRequest) throws ElementNotFoundException, IOException {
        Seller seller = sellerRepository.findByBusinessName(productRequest.getBusinessName())
                .orElseThrow(() -> new ElementNotFoundException("Seller not found with business name: " + productRequest.getBusinessName()));
        Category category = categoryRepository.findByCategoryName(productRequest.getCategoryName())
                .orElseThrow(() -> new ElementNotFoundException("Category not found with category name: " + productRequest.getCategoryName()));
        Product product = new Product();
        product.setImageUrl(DEFAULT_PRODUCT_URL);
        if (productRequest.getImageFile() != null && !productRequest.getImageFile().isEmpty()) {
            MultipartFile imageFile = productRequest.getImageFile();
            String imageUrl = imageService.saveImage(imageFile, "products");
            product.setImageUrl(imageUrl);
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
        log.info("Product added successfully: {}", product.getId());
        return "success";
    }

    public ProductResponse getProduct(Integer id){
        Product product =productRepository.findById(id).orElseThrow(() -> new ElementNotFoundException("Product not found with id:"+id));
        return productMapper.mapToResponse(product);
    }

    public void deleteProduct(Integer id){
        productRepository.deleteById(id);
    }


    @Transactional
    public String updateProduct(ProductResponse product) throws AccessDeniedException {
        Product existingProduct = productRepository.findById(product.getId()).orElseThrow(() -> new NoSuchElementException("Product not found with id:"+product.getId()));
        Seller seller = sellerRepository.findByBusinessName(product.getBusinessName())
                .orElseThrow(() -> new ElementNotFoundException("Seller not found with business name: " + product.getBusinessName()));
        Category category = categoryRepository.findByCategoryName(product.getCategoryName())
                .orElseThrow(() -> new ElementNotFoundException("Category not found with category name: " + product.getCategoryName()));

        if (!Objects.equals(existingProduct.getSeller().getId(), authenticationService.getAuthUser().get().getId())) {
            throw new AccessDeniedException("You are not authorized to update this product");
        }

        if (!Objects.equals(existingProduct.getCategory().getCategoryName(), product.getCategoryName())) {
            categoryService.updateProductCategory(existingProduct,category);
        }
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setSeller(seller);
        productRepository.save(existingProduct);
        log.info("Product updated successfully: {}", existingProduct.getId());
        return "Updated product Successfully";
    }

    public List<ProductResponse> getProductsByCategory(String categoryName) {
        List<Product> products = productRepository.findProductsByCategory_CategoryName(categoryName);
        return products.stream()
                .map(productMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<ProductResponse> getProductsByName(String productName) {
        List<Product> products = productRepository.findProductsByName(productName);
        return products.stream()
                .map(productMapper::mapToResponse)
                .collect(Collectors.toList());
    }
}
