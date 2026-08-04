package com.gutafida.paymentservice.dto;

import com.gutafida.paymentservice.entity.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreatePaymentRequest(

        @NotNull
        Long orderId,

        @NotNull
        @DecimalMin("0.01")
        BigDecimal amount,

        @NotNull
        PaymentMethod paymentMethod
) {
}
