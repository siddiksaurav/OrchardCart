package com.farmfresh.marketplace.OrchardCart.service;

import com.farmfresh.marketplace.OrchardCart.dto.ProductRequest;
import com.farmfresh.marketplace.OrchardCart.model.Category;
import com.farmfresh.marketplace.OrchardCart.model.Product;
import com.farmfresh.marketplace.OrchardCart.model.Seller;
import com.farmfresh.marketplace.OrchardCart.repository.CategoryRepository;
import com.farmfresh.marketplace.OrchardCart.repository.ProductRepository;
import com.farmfresh.marketplace.OrchardCart.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;
    @Autowired
    SellerRepository sellerRepository;
    @Autowired
    CategoryRepository categoryRepository;

    public List<Product> getProductList() {
        List<Product> lists = productRepository.findAll();
        return lists;
    }

    @Transactional
    public String addProduct(ProductRequest productRequest) {
        Seller seller = sellerRepository.findByBusinessName(productRequest.getBusinessName());
        Category category = categoryRepository.findByCategoryName(productRequest.getCategoryName());
        Product product = new Product();
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

    public Optional<Product> getProductById(Long id) {
        return  productRepository.findById(id);
    }

    public void deleteProductById(Long id) {
        if(productRepository.existsById(id)) {
            productRepository.deleteById(id);
        }
    }

    @Transactional
    public Product updateProductById(Long id, ProductRequest product) {
        Seller seller = sellerRepository.findByBusinessName(product.getBusinessName());
        Category category = categoryRepository.findByCategoryName(product.getCategoryName());
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));

        if (!Objects.equals(existingProduct.getSeller(), seller)) {
            try {
                throw new AccessDeniedException("You are not authorized to update this product");
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
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

        return existingProduct;
    }

    /*
    public Optional<Product> getProductByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }*/
}
