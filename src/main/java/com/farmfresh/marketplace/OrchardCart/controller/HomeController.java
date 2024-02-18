package com.farmfresh.marketplace.OrchardCart.controller;

import com.farmfresh.marketplace.OrchardCart.dto.response.CategoryResponse;
import com.farmfresh.marketplace.OrchardCart.dto.response.ProductResponse;
import com.farmfresh.marketplace.OrchardCart.model.Category;
import com.farmfresh.marketplace.OrchardCart.service.CategoryService;
import com.farmfresh.marketplace.OrchardCart.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final CategoryService categoryService;
    private final ProductService productService;

    public HomeController(CategoryService categoryService, ProductService productService) {
        this.categoryService = categoryService;
        this.productService = productService;
    }

    @GetMapping
    public String home(Model model) throws FileNotFoundException {
        List<String> imagePaths = getImagePaths();
        model.addAttribute("imagePaths", imagePaths);
        List<Category> categories = categoryService.getFeaturedCategories();
        List<ProductResponse> products = productService.getFeaturedProducts();
        model.addAttribute("categories", categories);
        model.addAttribute("products", products);
        return "homepage";
    }
    public List<String> getImagePaths() {
        File directory = new File("src/main/resources/static/img/promo/");
        if (directory.exists() && directory.isDirectory()) {
            return Arrays.stream(Objects.requireNonNull(directory.listFiles()))
                    .filter(File::isFile)
                    .map(file -> "/img/promo/" + file.getName())
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
