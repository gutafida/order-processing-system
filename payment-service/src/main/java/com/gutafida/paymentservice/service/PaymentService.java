package com.gutafida.paymentservice.service;

import com.gutafida.paymentservice.dto.CreatePaymentRequest;
import com.gutafida.paymentservice.dto.PaymentResponse;
import com.gutafida.paymentservice.entity.Payment;
import com.gutafida.paymentservice.entity.PaymentMethod;
import com.gutafida.paymentservice.entity.PaymentStatus;
import com.gutafida.paymentservice.repository.PaymentRepository;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
    public PaymentResponse createPayment(CreatePaymentRequest request){
        Payment payment = Payment.builder()
                .paymentNumber(generatePaymentNumber())
                .orderId(request.orderId())
                .amount(request.amount())
                .paymentMethod(request.paymentMethod())
                .status(PaymentStatus.PENDING)
                .build();
        Payment saved = paymentRepository.save(payment);
        return mapToResponse(payment);
    }

    private String generatePaymentNumber() {
        return "PAY-" + System.currentTimeMillis();
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getPaymentNumber(),
                payment.getOrderId(),
                payment.getAmount(),
                payment.getPaymentmethod(),
                payment.getPaymentStatus(),
                payment.getCreatedAt()
        );
    }
}
