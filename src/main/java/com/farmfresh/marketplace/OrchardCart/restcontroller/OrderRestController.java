package com.farmfresh.marketplace.OrchardCart.restcontroller;

import com.farmfresh.marketplace.OrchardCart.dto.request.AddressRequest;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.Orders;
import com.farmfresh.marketplace.OrchardCart.model.UserInfo;
import com.farmfresh.marketplace.OrchardCart.service.JwtService;
import com.farmfresh.marketplace.OrchardCart.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderRestController {
    private final OrderService orderService;
    public OrderRestController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public Orders placeOrder(AddressRequest shippingAddress) throws ElementNotFoundException {
        return orderService.createOrder(shippingAddress);
    }

    @GetMapping("{id}/update-status")
    public Orders updateOrderStatus(@PathVariable Integer id, @RequestParam String status) {
        return orderService.updateOrderStatus(id, status);
    }

    @GetMapping("/{id}")
    public Orders getOrderFromId(@PathVariable Integer id) throws ElementNotFoundException {
        return orderService.getOrderById(id);
    }

    @GetMapping("/all")
    public List<Orders> getAllOrders() {
        return orderService.getAllOrders();
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return "Deleted Order successfully";
    }

    @GetMapping("/order-history")
    public List<Orders> userOrderHistory() throws ElementNotFoundException {
        return orderService.getOrderHistory();
    }

}
