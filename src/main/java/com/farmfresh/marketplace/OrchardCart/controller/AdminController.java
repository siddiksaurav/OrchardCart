package com.farmfresh.marketplace.OrchardCart.controller;

import com.farmfresh.marketplace.OrchardCart.dto.response.CategoryResponse;
import com.farmfresh.marketplace.OrchardCart.dto.response.ProductResponse;
import com.farmfresh.marketplace.OrchardCart.model.Orders;
import com.farmfresh.marketplace.OrchardCart.service.CategoryService;
import com.farmfresh.marketplace.OrchardCart.service.OrderService;
import com.farmfresh.marketplace.OrchardCart.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final ProductService productService;
    private final OrderService orderService;
    private final CategoryService categoryService;

    public AdminController(ProductService productService, OrderService orderService, CategoryService categoryService) {
        this.productService = productService;
        this.orderService = orderService;
        this.categoryService = categoryService;
    }

    @GetMapping("/dashboard")
    public String showAdminDashboard() {
        return "dashboard/admin-dashboard";
    }

    @GetMapping("/products")
    public String showProductList(Model model) {
        List<ProductResponse> products = productService.getProductList();
        model.addAttribute("products", products);
        return "admin/admin-products";
    }

    @GetMapping("/orders")
    public String showOrders(Model model) {
        List<Orders> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "admin/admin-orders";
    }

    @GetMapping("/categories")
    public String showCategories(Model model) {
        List<CategoryResponse> categories = categoryService.getCategoryList();
        model.addAttribute("categories", categories);
        return "admin/admin-categories";
    }
}
