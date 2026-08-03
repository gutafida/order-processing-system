package com.gutafida.orderservice.service;

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
import java.time.LocalDateTime;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    public OrderResponse createOrder(CreateOrderRequest request){
        Order order = new Order();
        order.setOrderNumber(generateOrderNumber());
        order.setCustomerId(request.customerId());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        //order.setUpdatedAt(LocalDateTime.now());

        for (OrderItemRequest itemRequest : request.items()){
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
        return mapToResponse(savedOrder);
    }


    public OrderResponse getOrderById(Long id){
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + id));
        return mapToResponse(order);
    }


    private BigDecimal calculateSubtotal(
            BigDecimal unitPrice, Integer quantity){
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    private BigDecimal calculateTotal(Order order){
        return order.getItems().stream()
                .map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    private String generateOrderNumber(){
        return "ORD-" + System.currentTimeMillis();
    }

    private OrderResponse mapToResponse(Order order){
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
