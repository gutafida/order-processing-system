package com.gutafida.orderservice.client.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        String orderNumber,
        Long orderId,
        BigDecimal amount,
        String paymentMethod,
        String status,
        LocalDateTime createdAt
) {
}
