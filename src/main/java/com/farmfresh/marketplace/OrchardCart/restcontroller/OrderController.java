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

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private  final OrderService orderService;

    @PostMapping("/place")
    public Order placeOrder(@RequestHeader("Authorization")String token, ShippingAddressRequest shippingAddress) throws ElementNotFoundException {
         return orderService.createOrder(token,shippingAddress);
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
        return orderService.getOrderHistory(token);
    }

}
