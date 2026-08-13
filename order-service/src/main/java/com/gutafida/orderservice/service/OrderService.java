package com.gutafida.orderservice.service;

import com.gutafida.orderservice.client.PaymentClient;
import com.gutafida.orderservice.client.dto.PaymentRequest;
import com.gutafida.orderservice.client.dto.PaymentResponse;
import com.gutafida.orderservice.client.inventory.InventoryClient;
import com.gutafida.orderservice.client.inventory.dto.DeductInventoryRequest;
import com.gutafida.orderservice.client.inventory.dto.ReserveInventoryRequest;
import com.gutafida.orderservice.dto.CreateOrderRequest;
import com.gutafida.orderservice.dto.OrderItemRequest;
import com.gutafida.orderservice.dto.OrderResponse;
import com.gutafida.orderservice.entity.Order;
import com.gutafida.orderservice.entity.OrderItem;
import com.gutafida.orderservice.enums.OrderStatus;
import com.gutafida.orderservice.exception.ResourceNotFoundException;
import com.gutafida.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentClient paymentClient;
    private final InventoryClient inventoryClient;

    public OrderService(OrderRepository orderRepository, PaymentClient paymentClient, InventoryClient inventoryClient) {
        this.orderRepository = orderRepository;
        this.paymentClient = paymentClient;
        this.inventoryClient = inventoryClient;
    }

    public OrderResponse createOrder(CreateOrderRequest request) {
        Order order = new Order();

        order.setOrderNumber(generateOrderNumber());
        order.setCustomerId(request.customerId());
        order.setStatus(OrderStatus.PENDING);

        for (OrderItemRequest itemRequest : request.items()) {
            ReserveInventoryRequest inventoryRequest = new ReserveInventoryRequest(
                    itemRequest.productId(),
                    itemRequest.quantity()
            );
            inventoryClient.reserveInventory(inventoryRequest);

            BigDecimal subtotal = calculateSubtotal(
                    itemRequest.unitPrice(),
                    itemRequest.quantity()
            );

            OrderItem item = OrderItem.builder()
                    .productId(itemRequest.productId())
                    .productName(itemRequest.productName())
                    .quantity(itemRequest.quantity())
                    .unitPrice(itemRequest.unitPrice())
                    .subtotal(subtotal)
                    .build();

            order.addItem(item);
        }

        order.setTotalAmount(calculateTotal(order));

        Order savedOrder = orderRepository.save(order);

        savedOrder.setStatus(OrderStatus.PAYMENT_PROCESSING);
        savedOrder = orderRepository.save(savedOrder);

        PaymentRequest paymentRequest = new PaymentRequest(
                savedOrder.getId(),
                savedOrder.getTotalAmount(),
                "CREDIT_CARD"
        );

        PaymentResponse paymentResponse =
                paymentClient.createPayment(paymentRequest);

        if ("COMPLETED".equals(paymentResponse.paymentStatus())) {
            for(OrderItem item : savedOrder.getItems()){
                DeductInventoryRequest deductRequest = new DeductInventoryRequest(
                        item.getProductId(),
                        item.getQuantity()
                );
                inventoryClient.deductInventory(deductRequest);
            }
            savedOrder.setStatus(OrderStatus.PAID);
        } else {
            savedOrder.setStatus(OrderStatus.PAYMENT_FAILED);
        }

        Order finalOrder = orderRepository.save(savedOrder);

        return mapToResponse(finalOrder);

    }

    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id: " + id
                        )
                );

        return mapToResponse(order);
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public OrderResponse cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Order not found with id: " + id
                        )
                );

        if (order.getStatus() == OrderStatus.CANCELLED) {
            return mapToResponse(order);
        }

        order.setStatus(OrderStatus.CANCELLED);

        Order savedOrder = orderRepository.save(order);

        return mapToResponse(savedOrder);
    }

    private BigDecimal calculateSubtotal(
            BigDecimal unitPrice,
            Integer quantity
    ) {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    private BigDecimal calculateTotal(Order order) {
        return order.getItems()
                .stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String generateOrderNumber() {
        return "ORD-" + System.currentTimeMillis();
    }

    private OrderResponse mapToResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getCustomerId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}