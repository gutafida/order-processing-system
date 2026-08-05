package com.gutafida.orderservice.client;

import com.gutafida.orderservice.client.dto.PaymentRequest;
import com.gutafida.orderservice.client.dto.PaymentResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "payment-service",
        url = "${payment.service.url}"
)
public interface PaymentClient {
    @PostMapping("/api/payments")
    PaymentResponse createPayment(
            @RequestBody
            PaymentRequest request
    );
}
