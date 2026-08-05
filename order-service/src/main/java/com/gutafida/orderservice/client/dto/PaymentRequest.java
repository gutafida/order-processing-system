package com.gutafida.orderservice.client.dto;

import java.math.BigDecimal;

public record PaymentRequest(
        Long orderId,
        BigDecimal amount,
        String paymentMethod
) {
}
