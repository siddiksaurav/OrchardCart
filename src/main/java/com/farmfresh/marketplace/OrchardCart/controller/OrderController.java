package com.farmfresh.marketplace.OrchardCart.controller;

import com.farmfresh.marketplace.OrchardCart.dto.request.AddressRequest;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.Orders;
import com.farmfresh.marketplace.OrchardCart.model.UserInfo;
import com.farmfresh.marketplace.OrchardCart.service.AuthenticationService;
import com.farmfresh.marketplace.OrchardCart.service.CartService;
import com.farmfresh.marketplace.OrchardCart.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;
    private final CartService cartService;

    public OrderController(OrderService orderService, CartService cartService) {
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping("/shipment-address")
    public String provideShipmentAddress(Model model) {
        model.addAttribute("shippingAddress", new AddressRequest());
        return "order/shipment-address-form";
    }

    @PostMapping("/place")
    public String placeOrder(@Valid AddressRequest shippingAddress,
                             Model model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "order/shipment-address-form";
        }
        Orders order = orderService.createOrder(shippingAddress);
        model.addAttribute("order", order);
        return "order/order-details-confirmation";
    }

    @GetMapping("/{id}")
    public String getOrderDetails(@PathVariable Integer id, Model model) {
        Orders order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "order/order-details";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/list")
    public String OrdersList(Model model) {
        List<Orders> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order/order-history";
    }

    @GetMapping("/confirm-order")
    public String confirmOrder() {
        cartService.removeCart();
        return "order/order-success";
    }

    @GetMapping("/order-history")
    public String userOrderHistory(Model model) {
        List<Orders> orderHistory = orderService.getOrderHistory();
        model.addAttribute("orders", orderHistory);
        return "order/order-history";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("update-status/{id}")
    public String updateOrderStatus(@PathVariable Integer id, @RequestParam String status, HttpServletRequest request) {
        orderService.updateOrderStatus(id, status);
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return "redirect:/order/list";
    }
}

