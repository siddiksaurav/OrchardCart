package com.farmfresh.marketplace.OrchardCart.restcontroller;

import com.farmfresh.marketplace.OrchardCart.dto.request.ShippingAddressRequest;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.*;
import com.farmfresh.marketplace.OrchardCart.service.CartService;
import com.farmfresh.marketplace.OrchardCart.service.JwtService;
import com.farmfresh.marketplace.OrchardCart.service.OrderService;
import jakarta.persistence.Id;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private  final OrderService orderService;
    private final JwtService jwtService;

    public OrderController(OrderService orderService, JwtService jwtService) {
        this.orderService = orderService;
        this.jwtService = jwtService;
    }

    @PostMapping("/place")
    public Order placeOrder(@RequestHeader("Authorization")String token, ShippingAddressRequest shippingAddress) throws ElementNotFoundException {
        UserInfo user = jwtService.getUserByToken(token);
        return orderService.createOrder(user,shippingAddress);
    }

    @GetMapping("{id}/update-status")
    public Order updateOrderStatus(@PathVariable Integer id, @RequestParam String status){
        return orderService.updateOrderStatus(id,status);
    }

    @GetMapping("/{id}")
    public Order getOrderFromId(@PathVariable Integer id) throws ElementNotFoundException {
        return orderService.getOrderById(id);
    }
    @GetMapping("/all")
    public List<Order> getAllOrders(){
        return orderService.getAllOrders();
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Integer id){
        orderService.deleteOrder(id);
        return "Deleted Order successfully";
    }

    @GetMapping("/order-history")
    public List<Order> userOrderHistory(@RequestHeader("Authorization")String token) throws ElementNotFoundException {
        UserInfo user = jwtService.getUserByToken(token);
        return orderService.getOrderHistory(user);
    }

}
