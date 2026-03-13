package com.service.order.service;

import com.service.order.entity.Orders;

import java.util.List;

public interface OrderService {
    List<Orders> getAllOrders();
    Orders createOrder(Orders orders);
}
