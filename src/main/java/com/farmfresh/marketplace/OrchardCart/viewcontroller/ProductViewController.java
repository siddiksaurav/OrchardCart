package com.farmfresh.marketplace.OrchardCart.viewcontroller;
import com.farmfresh.marketplace.OrchardCart.dto.request.ProductRequest;
import com.farmfresh.marketplace.OrchardCart.dto.response.CategoryResponse;
import com.farmfresh.marketplace.OrchardCart.dto.response.ProductResponse;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.Category;
import com.farmfresh.marketplace.OrchardCart.service.CategoryService;
import com.farmfresh.marketplace.OrchardCart.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductViewController {

    private final ProductService productService;
    private final CategoryService categoryService;


    @GetMapping("/all")
    public String getAllProducts(Model model) {
        List<ProductResponse> productList = productService.getProductList();
        model.addAttribute("products", productList);
        return "/products/product-list"; // Assuming "product-list.html" is the Thymeleaf template for listing products
    }

    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    @GetMapping("/create")
    public String showProductForm(Model model) {
        List<CategoryResponse> categories = categoryService.getCategoryList();
        model.addAttribute("productRequest", new ProductRequest());
        model.addAttribute("categories", categories);
        return "products/product-create"; // Assuming "create-product.html" is the Thymeleaf template for creating products
    }

    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    @PostMapping("/create")
    public String createProduct(@Valid @ModelAttribute("productRequest") ProductRequest productRequest) throws ElementNotFoundException, IOException {
        productService.addProduct(productRequest);
        return "redirect:/products/all"; // Redirect to the product list after creating a product
    }

    @GetMapping("/{id}")
    public String getProductById(@PathVariable Integer id, Model model) throws ElementNotFoundException {
        ProductResponse product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "/products/product-details"; // Assuming "product-details.html" is the Thymeleaf template for displaying product details
    }

    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    @GetMapping("/delete/{id}")
    public String deleteProductById(@PathVariable Integer id) {
        productService.deleteProductById(id);
        return "redirect:/products/all"; // Redirect to the product list after deleting a product
    }

    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable Integer id, Model model) throws ElementNotFoundException {
        ProductResponse product = productService.getProductById(id);
        model.addAttribute("productResponse", product);
        model.addAttribute("productId",id);
        return "/products/product-edit"; // Assuming "edit-product.html" is the Thymeleaf template for editing products
    }

    //@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SELLER')")
    @PostMapping("/save")
    public String updateProductById(@Valid @ModelAttribute("productResponse") ProductResponse product) throws ElementNotFoundException, AccessDeniedException {
        productService.updateProductById(product);
        return "redirect:/products/all"; // Redirect to the product list after updating a product
    }
}


