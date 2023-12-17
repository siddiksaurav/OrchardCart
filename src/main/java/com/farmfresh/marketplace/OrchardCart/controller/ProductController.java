package com.farmfresh.marketplace.OrchardCart.controller;

import com.farmfresh.marketplace.OrchardCart.model.Product;
import com.farmfresh.marketplace.OrchardCart.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("products")
public class ProductController {
    @Autowired
    ProductService productService;
    @GetMapping("/all")
    public List<Product> showAllProducts(){
        return productService.getProductList();
    }
    @PostMapping("/create")
    public String createProduct(@Valid @RequestBody Product product){
        return productService.addProduct(product);
    }
    @GetMapping("/{id}")
    public Optional<Product> showProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }
    @DeleteMapping("/{id}")
    public void deleteProductById(@PathVariable Long id){
        productService.deleteProductById(id);
    }

    @PutMapping("/{id}")
    public Product updateProductById(@PathVariable Long id, @Valid @RequestBody Product product){
        return productService.updateProductById(id,product);
    }
}
