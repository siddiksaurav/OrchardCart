package com.farmfresh.marketplace.OrchardCart.controller;

import com.farmfresh.marketplace.OrchardCart.dto.ProductRequest;
import com.farmfresh.marketplace.OrchardCart.model.Category;
import com.farmfresh.marketplace.OrchardCart.model.Product;
import com.farmfresh.marketplace.OrchardCart.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
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

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    @PostMapping("/create")
    public String createProduct(@Valid @RequestBody ProductRequest productRequest){
        try{
            return productService.addProduct(productRequest);
        }
        catch (Exception e){
            return "Failed to create product!";
        }

    }
    @GetMapping("/{id}")
    public Optional<Product> showProductById(@PathVariable Long id){
        return productService.getProductById(id);
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    @DeleteMapping("/{id}")
    public String deleteProductById(@PathVariable Long id){
        try {
            productService.deleteProductById(id);
            return "Product deleted successfully";
        } catch (Exception e) {
            return "Failed to delete product";
        }
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    @PutMapping("/{id}")
    public String updateProductById(@PathVariable Long id, @Valid @RequestBody ProductRequest productRequest) {
        try {
            Product updatedProduct = productService.updateProductById(id, productRequest);
            if (updatedProduct != null) {
                return "Product updated successfully";
            } else {
                return "Failed to update product";
            }
        } catch (Exception e) {
            return "Failed to update product";
        }
    }

}
