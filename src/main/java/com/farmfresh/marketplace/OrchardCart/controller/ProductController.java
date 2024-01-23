package com.farmfresh.marketplace.OrchardCart.controller;
import com.farmfresh.marketplace.OrchardCart.dto.request.ProductRequest;
import com.farmfresh.marketplace.OrchardCart.dto.response.CategoryResponse;
import com.farmfresh.marketplace.OrchardCart.dto.response.ProductResponse;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.service.CategoryService;
import com.farmfresh.marketplace.OrchardCart.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {
    private static final Logger log = LoggerFactory.getLogger(ProductController.class);
    private static final String REDIRECT_PRODUCTS_LIST = "redirect:/products/list";
    private final ProductService productService;
    private final CategoryService categoryService;

    public ProductController(ProductService productService, CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }


    @GetMapping("/list")
    public String listProducts(Model model,@RequestParam(required = false) String productName) {
        List<ProductResponse> productList;
        if (productName != null && !productName.isEmpty()) {
            productList = productService.getProductsByName(productName);
        } else {
            productList = productService.getProductList();
        }
        if(productList==null){
            log.info("No product found");
        }
        model.addAttribute("products", productList);
        return "products/product-list";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    @GetMapping("/create")
    public String createProductForm(Model model) {
        List<CategoryResponse> categories = categoryService.getCategoryList();
        model.addAttribute("productRequest", new ProductRequest());
        model.addAttribute("categories", categories);
        return "products/product-create";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    @PostMapping("/create")
    public String createProduct(@Valid ProductRequest productRequest, BindingResult bindingResult) throws IOException {
        if(bindingResult.hasErrors()) {
            log.warn("Validation errors in product create form");
            return "/products/product-create";
        }

        productService.addProduct(productRequest);
        return REDIRECT_PRODUCTS_LIST;
    }

    @GetMapping("/{id}")
    public String getProduct(@PathVariable Integer id, Model model){
        ProductResponse product = productService.getProduct(id);
        model.addAttribute("product", product);
        return "products/product-details";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return REDIRECT_PRODUCTS_LIST;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Integer id, Model model){
        ProductResponse product = productService.getProduct(id);
        model.addAttribute("productResponse", product);
        return "products/product-edit";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    @PostMapping("/save")
    public String updateProduct(@Valid ProductResponse product) throws AccessDeniedException {
        productService.updateProduct(product);
        return REDIRECT_PRODUCTS_LIST;
    }

    @GetMapping("/list/{categoryName}")
    public String listProductsByCategory(@PathVariable String categoryName,Model model) {
        List<ProductResponse> productList = productService.getProductsByCategory(categoryName);
        if(productList==null){
            log.info("No product found");
        }
        model.addAttribute("products", productList);
        return "products/product-list";
    }
}


