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
    private final AuthenticationService authenticationService;
    private final CartService cartService;

    public OrderController(OrderService orderService, AuthenticationService authenticationService, CartService cartService) {
        this.orderService = orderService;
        this.authenticationService = authenticationService;
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
        UserInfo user = authenticationService.getAuthUser().orElseThrow(() -> new ElementNotFoundException("User not signed in"));
        Orders order = orderService.createOrder(user, shippingAddress);
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
        UserInfo user = authenticationService.getAuthUser().orElseThrow(() -> new ElementNotFoundException("User not signed in"));
        cartService.clearUserCart(user);
        return "order/order-success";
    }

    @GetMapping("/order-history")
    public String userOrderHistory(Model model) {
        UserInfo user = authenticationService.getAuthUser().orElseThrow(() -> new ElementNotFoundException("User not signed in"));
        List<Orders> orderHistory = orderService.getOrderHistory(user);
        model.addAttribute("orders", orderHistory);
        return "order/order-history";
    }

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

