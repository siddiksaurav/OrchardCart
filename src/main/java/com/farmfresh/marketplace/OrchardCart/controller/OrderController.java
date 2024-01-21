package com.farmfresh.marketplace.OrchardCart.controller;

import com.farmfresh.marketplace.OrchardCart.dto.request.ShippingAddressRequest;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.Cart;
import com.farmfresh.marketplace.OrchardCart.model.Orders;
import com.farmfresh.marketplace.OrchardCart.model.UserInfo;
import com.farmfresh.marketplace.OrchardCart.repository.UserInfoRepository;
import com.farmfresh.marketplace.OrchardCart.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/order/")
public class OrderController {

    private final OrderService orderService;
    private final AuthenticationService authenticationService;
    private final CartService cartService;

    public OrderController(OrderService orderService,AuthenticationService authenticationService,CartService cartService) {
        this.orderService = orderService;
        this.authenticationService = authenticationService;
        this.cartService = cartService;
    }
    @GetMapping("shipment-address")
    public String provideShipmentAddress(Model model) {
        model.addAttribute("shippingAddress", new ShippingAddressRequest());
        return "order/shipment-address-form";
    }

    @PostMapping("place")
    public String placeOrder(ShippingAddressRequest shippingAddress,
                             Model model){
        UserInfo user = authenticationService.getAuthUser().orElseThrow(()->new ElementNotFoundException("User not signed in"));
        Orders order = orderService.createOrder(user, shippingAddress);
        model.addAttribute("order", order);
        return "order/order-details-confirmation";
    }

    @GetMapping("{id}")
    public String getOrderDetails(@PathVariable Integer id, Model model){
        Orders order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "order/order-details";
    }

    @GetMapping("list")
    public String OrdersList(Model model) {
        List<Orders> orders = orderService.getAllOrders();
        model.addAttribute("orders", orders);
        return "order/order-history";
    }

    @GetMapping("confirm-order")
    public String confirmOrder(){
        UserInfo user = authenticationService.getAuthUser().orElseThrow(()->new ElementNotFoundException("User not signed in"));
        cartService.clearUserCart(user);
        return "order/order-success";
    }
    @GetMapping("order-history")
    public String userOrderHistory(Model model) {
        UserInfo user = authenticationService.getAuthUser().orElseThrow(()->new ElementNotFoundException("User not signed in"));
        List<Orders> orderHistory = orderService.getOrderHistory(user);
        model.addAttribute("orders", orderHistory);
        return "order/order-history";
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return "redirect:/order/list";
    }
}

