package com.farmfresh.marketplace.OrchardCart.service;

import com.farmfresh.marketplace.OrchardCart.dto.request.AddressRequest;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.*;
import com.farmfresh.marketplace.OrchardCart.repository.AddressRepository;
import com.farmfresh.marketplace.OrchardCart.repository.OrderItemRepository;
import com.farmfresh.marketplace.OrchardCart.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final AddressRepository addressRepository;
    private final JmsTemplate jmsTemplate;
    private final AuthenticationService authenticationService;


    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartService cartService, AddressRepository addressRepository, JmsTemplate jmsTemplate, AuthenticationService authenticationService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.addressRepository = addressRepository;
        this.jmsTemplate = jmsTemplate;
        this.authenticationService = authenticationService;
    }

    @Transactional
    public Orders createOrder(AddressRequest shippingAddress) {
        UserInfo user = authenticationService.getAuthUser().orElseThrow(() -> new ElementNotFoundException("User not signed in"));
        Cart cart = cartService.showUserCart(user);
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem item : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setItemPrice(item.getPrice());
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            OrderItem createdOrderItem = orderItemRepository.save(orderItem);
            orderItems.add(createdOrderItem);
        }
        Address address = new Address(shippingAddress.getDistrict(), shippingAddress.getCity(), shippingAddress.getAdditionalAddress(), shippingAddress.getPhoneNumber());
        Orders createdOrder = new Orders();
        createdOrder.setOrderStatus(OrderStatus.PENDING);
        createdOrder.setShippingAddress(address);
        createdOrder.setTotalPrice(cart.getTotalPrice());
        createdOrder.setOrderItems(orderItems);
        createdOrder.setTotalItem(cart.getTotalItem());
        createdOrder.setOrderTime(LocalDateTime.now());
        createdOrder.setUser(user);
        Orders savedOrder = orderRepository.save(createdOrder);
        addressRepository.save(address);
        for (OrderItem item : orderItems) {
            item.setOrder(savedOrder);
            orderItemRepository.save(item);
        }
        jmsTemplate.convertAndSend("orderQueue", savedOrder.getUser().getEmail());
        return savedOrder;
    }

    public Orders updateOrderStatus(Integer orderId, String status) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() -> new ElementNotFoundException("Order not found with id:" + orderId));
        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            order.setOrderStatus(orderStatus);
        } catch (IllegalArgumentException e) {
            log.error("Invalid status string: " + status);
        }
        return orderRepository.save(order);
    }

    public Orders getOrderById(Integer orderId) {
        return orderRepository.findById(orderId).orElseThrow(() -> new ElementNotFoundException("Order not found with id:" + orderId));
    }

    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<Orders> getOrderHistory() throws ElementNotFoundException {
        UserInfo user = authenticationService.getAuthUser().orElseThrow(() -> new ElementNotFoundException("User not signed in"));
        return orderRepository.findByUserId(user.getId());
    }
    
    public void deleteOrder(Integer orderId) {
        orderRepository.deleteById(orderId);
    }

}
