package com.farmfresh.marketplace.OrchardCart.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDetails {
    private String message;
    private String details;
    private LocalDateTime timeStamp;
}
