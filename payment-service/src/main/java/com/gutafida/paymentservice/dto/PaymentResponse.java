package com.gutafida.paymentservice.dto;

import com.gutafida.paymentservice.entity.PaymentMethod;
import com.gutafida.paymentservice.entity.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        String paymentNumber,
        Long orderId,
        BigDecimal amount,
        PaymentMethod paymentMethod,
        PaymentStatus paymentStatus,
        LocalDateTime createdAt
) {
}
