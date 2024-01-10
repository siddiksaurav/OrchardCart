package com.farmfresh.marketplace.OrchardCart.restcontroller;

import com.farmfresh.marketplace.OrchardCart.dto.request.ProductRequest;
import com.farmfresh.marketplace.OrchardCart.dto.response.ProductResponse;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {
    @Autowired
    ProductService productService;
    @GetMapping("/all")
    public List<ProductResponse> getAllProducts(){
        return productService.getProductList();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    @PostMapping("/create")
    public String createProduct(@Valid @RequestBody ProductRequest productRequest) throws ElementNotFoundException {
        return productService.addProduct(productRequest);
    }
    @GetMapping("/{id}")
    public ProductResponse getProductById(@PathVariable Integer id) throws ElementNotFoundException {
        return productService.getProductById(id);
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    @DeleteMapping("/{id}")
    public String deleteProductById(@PathVariable Integer id){
        productService.deleteProductById(id);
        return "deleted";
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    @PutMapping("{id}/save")
    public String updateProductById(@PathVariable Long id, @Valid @RequestBody ProductResponse productResponse) throws ElementNotFoundException, AccessDeniedException {
        return productService.updateProductById(productResponse);
        }
}
