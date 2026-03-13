package com.deepana.orderservice.service;

import com.deepana.orderservice.dto.OrderRequest;
import com.deepana.orderservice.dto.OrderResponse;
import com.deepana.orderservice.entity.Order;

import java.util.List;

public interface OrderService {

    OrderResponse createOrder(OrderRequest orderRequest);

    OrderResponse getOrderById(Long id);

    List<OrderResponse> getAllOrders();

    List<OrderResponse> getOrdersByCustomerId(Long customerId);

    OrderResponse updateOrderStatus(Long id, Order.OrderStatus status);

    void deleteOrder(Long id);
}
