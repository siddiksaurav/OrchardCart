package com.farmfresh.marketplace.OrchardCart.restcontroller;

import com.farmfresh.marketplace.OrchardCart.dto.request.ShippingAddressRequest;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.*;
import com.farmfresh.marketplace.OrchardCart.service.JwtService;
import com.farmfresh.marketplace.OrchardCart.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderRestController {

    private  final OrderService orderService;
    private final JwtService jwtService;

    public OrderRestController(OrderService orderService, JwtService jwtService) {
        this.orderService = orderService;
        this.jwtService = jwtService;
    }

    @PostMapping("/place")
    public Orders placeOrder(@RequestHeader("Authorization")String token, ShippingAddressRequest shippingAddress) throws ElementNotFoundException {
        UserInfo user = jwtService.getUserByToken(token);
        return orderService.createOrder(user,shippingAddress);
    }

    @GetMapping("{id}/update-status")
    public Orders updateOrderStatus(@PathVariable Integer id, @RequestParam String status){
        return orderService.updateOrderStatus(id,status);
    }

    @GetMapping("/{id}")
    public Orders getOrderFromId(@PathVariable Integer id) throws ElementNotFoundException {
        return orderService.getOrderById(id);
    }
    @GetMapping("/all")
    public List<Orders> getAllOrders(){
        return orderService.getAllOrders();
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Integer id){
        orderService.deleteOrder(id);
        return "Deleted Order successfully";
    }

    @GetMapping("/order-history")
    public List<Orders> userOrderHistory(@RequestHeader("Authorization")String token) throws ElementNotFoundException {
        UserInfo user = jwtService.getUserByToken(token);
        return orderService.getOrderHistory(user);
    }

}
