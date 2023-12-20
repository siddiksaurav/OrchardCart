package com.farmfresh.marketplace.OrchardCart.service;

import com.farmfresh.marketplace.OrchardCart.model.Category;
import com.farmfresh.marketplace.OrchardCart.model.Product;
import com.farmfresh.marketplace.OrchardCart.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    public List<Product> getProductList() {
        List<Product> lists = productRepository.findAll();
        return lists;
    }

    public String addProduct(Product product) {
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

    public Product updateProductById(Long id, Product product) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setCategory(product.getCategory());
        productRepository.save(existingProduct);
        return existingProduct;
    }

    public Optional<Product> getProductByCategoryId(Long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
}
