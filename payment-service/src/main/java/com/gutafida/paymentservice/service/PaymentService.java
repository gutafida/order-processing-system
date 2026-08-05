package com.gutafida.paymentservice.service;

import com.gutafida.paymentservice.dto.CreatePaymentRequest;
import com.gutafida.paymentservice.dto.PaymentResponse;
import com.gutafida.paymentservice.entity.Payment;
import com.gutafida.paymentservice.entity.PaymentStatus;
import com.gutafida.paymentservice.exception.ResourceNotFoundException;
import com.gutafida.paymentservice.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public PaymentResponse getPaymentById(Long id){
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment not found with id: " + id));
        return mapToResponse(payment);
    }

    public List<PaymentResponse> getPaymentsByOrderId(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .stream().map(this::mapToResponse)
                .toList();
    }

public PaymentResponse refundPayment(Long id){
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Payment not found with id" + id));
        if(payment.getStatus() == PaymentStatus.REFUNDED){
            return mapToResponse(payment);
        }

        if(payment.getStatus() != PaymentStatus.COMPLETED){
            throw new IllegalStateException("Only completed payments can be refunded!");
        }
        payment.setStatus(PaymentStatus.REFUNDED);
        Payment saved = paymentRepository.save(payment);
        return mapToResponse(saved);

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
                payment.getPaymentMethod(),
                payment.getStatus(),
                payment.getCreatedAt()
        );
    }


}
