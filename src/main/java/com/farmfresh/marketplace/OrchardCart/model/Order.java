package com.farmfresh.marketplace.OrchardCart.model;

import com.farmfresh.marketplace.OrchardCart.model.enumaration.OrderStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Integer id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserInfo user;
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;
    private LocalDateTime orderTime;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    private Double totalPrice;
    private Integer totalItem;
    private String district;
    private String city;
    private String additionalAddress;
    @Pattern(regexp = "^01\\d{9}$", message = "Phone number must start with '01' and have 11 digits")
    private String phoneNumber;
}
