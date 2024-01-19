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
@RequestMapping("/")
public class OrderController {

    private final OrderService orderService;
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserInfoRepository userInfoRepository;
    private final CartService cartService;

    public OrderController(OrderService orderService, JwtService jwtService, AuthenticationService authenticationService, UserInfoRepository userInfoRepository,CartService cartService) {
        this.orderService = orderService;
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userInfoRepository = userInfoRepository;
        this.cartService = cartService;
    }
    @GetMapping("shipment-address")
    public String provideShipmentAddress(Model model) {
        // You can add any additional data to the model if needed
        model.addAttribute("shippingAddress", new ShippingAddressRequest());

        // Return the Thymeleaf view name for providing shipment address
        return "order/shipment-address-form";
    }

    /*@PostMapping("/shipment-address")
    public String submitShipmentAddress(@ModelAttribute("shippingAddress") ShippingAddressRequest shippingAddress, Model model) throws ElementNotFoundException {
        // Return the Thymeleaf view name for order confirmation
        return "order/order-confirmation";
    }*/
    @PostMapping("place")
    public String placeOrder(ShippingAddressRequest shippingAddress,
                             Model model) throws ElementNotFoundException {
        String userEmail = authenticationService.getAuthUser();
        UserInfo user = userInfoRepository.findByEmail(userEmail).orElseThrow(()-> new ElementNotFoundException("No user with email"+userEmail));
        Orders order = orderService.createOrder(user, shippingAddress);
        model.addAttribute("order", order);
        // Return the Thymeleaf view name
        return "order/order-details-confirmation";
    }

    @GetMapping("order/{id}")
    public String getOrderDetails(@PathVariable Integer id, Model model) throws ElementNotFoundException {
        Orders order = orderService.getOrderById(id);

        // Add order details to the model for Thymeleaf rendering
        model.addAttribute("order", order);

        return "order/order-details";
    }

    @GetMapping("/all")
    public String getAllOrders(Model model) {
        List<Orders> orders = orderService.getAllOrders();

        // Add order list to the model for Thymeleaf rendering
        model.addAttribute("orders", orders);

        // Return the Thymeleaf view name
        return "order/order-history";
    }

    @GetMapping("/confirm-order")
    public String confirmOrder() throws ElementNotFoundException {
        String userEmail = authenticationService.getAuthUser();
        UserInfo user = userInfoRepository.findByEmail(userEmail).orElseThrow(()-> new ElementNotFoundException("No user with email"+userEmail));
        cartService.clearUserCart(user);
        return "order/order-success";
    }
    @GetMapping("/order-history")
    public String userOrderHistory(Model model) throws ElementNotFoundException {
        String userEmail = authenticationService.getAuthUser();
        UserInfo user = userInfoRepository.findByEmail(userEmail).orElseThrow(()-> new ElementNotFoundException("No user with email"+userEmail));
        List<Orders> orderHistory = orderService.getOrderHistory(user);

        // Add order history list to the model for Thymeleaf rendering
        model.addAttribute("orders", orderHistory);

        // Return the Thymeleaf view name
        return "order/order-history";
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return "redirect:/order/all";
    }
}

