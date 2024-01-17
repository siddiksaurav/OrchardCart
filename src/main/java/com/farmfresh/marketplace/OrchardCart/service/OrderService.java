package com.farmfresh.marketplace.OrchardCart.service;

import com.farmfresh.marketplace.OrchardCart.dto.request.ShippingAddressRequest;
import com.farmfresh.marketplace.OrchardCart.exception.ElementNotFoundException;
import com.farmfresh.marketplace.OrchardCart.model.*;
import com.farmfresh.marketplace.OrchardCart.model.OrderStatus;
import com.farmfresh.marketplace.OrchardCart.repository.OrderItemRepository;
import com.farmfresh.marketplace.OrchardCart.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
    }

    private final Logger log = LoggerFactory.getLogger(OrderService.class);
    public Order createOrder(UserInfo user,ShippingAddressRequest shippingAddress) throws ElementNotFoundException {
        Order order = new Order();
        Cart cart = cartService.findUserCart(user);
        List<OrderItem> orderItems = new ArrayList<>();
        for (CartItem item : cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setItemPrice(item.getPrice());
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            OrderItem createdOrderItem = orderItemRepository.save(orderItem);
            orderItems.add(createdOrderItem);
        }
        Order createdOrder = new Order();
        createdOrder.setOrderStatus(OrderStatus.PENDING);
        createdOrder.setDistrict(shippingAddress.getDistrict());
        createdOrder.setCity(shippingAddress.getCity());
        createdOrder.setAdditionalAddress(shippingAddress.getAdditionalAddress());
        createdOrder.setPhoneNumber(shippingAddress.getPhoneNumber());
        createdOrder.setTotalPrice(cart.getTotalPrice());
        createdOrder.setOrderItems(orderItems);
        createdOrder.setTotalItem(cart.getTotalItem());
        createdOrder.setOrderTime(LocalDateTime.now());
        createdOrder.setUser(user);
        Order savedOrder = orderRepository.save(createdOrder);
        for(OrderItem item: orderItems){
            item.setOrder(savedOrder);
            orderItemRepository.save(item);
        }
        return  order;
    }
    public Order updateOrderStatus(Integer OrderId,String status){
        Order order = new Order();

        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            order.setOrderStatus(orderStatus);
        } catch (IllegalArgumentException e) {
            log.error("Invalid status string: " + status);
        }
        return orderRepository.save(order);
    }

    public Order getOrderById(Integer orderId) throws ElementNotFoundException {
        return orderRepository.findById(orderId).orElseThrow(()->new ElementNotFoundException("Order not found with id:"+orderId));
    }
    public List<Order> getAllOrders(){
        return orderRepository.findAll();
    }
    public  List<Order> getOrderHistory(UserInfo user) throws ElementNotFoundException {
        return orderRepository.findByUserId(user.getId());
    }


    public void deleteOrder(Integer orderId){
        orderRepository.deleteById(orderId);
    }

}
