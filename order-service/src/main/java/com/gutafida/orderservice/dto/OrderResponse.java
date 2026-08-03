package com.gutafida.orderservice.dto;

import com.gutafida.orderservice.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        String orderNumber,
        Long customerId,
        BigDecimal totalAmount,
        OrderStatus orderStatus,
        LocalDateTime createdAt
) {
}
