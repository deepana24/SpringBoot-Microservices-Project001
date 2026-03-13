package com.deepana.orderservice.service.impl;

import com.deepana.orderservice.dto.CustomerResponse;
import com.deepana.orderservice.dto.OrderRequest;
import com.deepana.orderservice.dto.OrderResponse;
import com.deepana.orderservice.entity.Order;
import com.deepana.orderservice.external.CustomerFeignClient;
import com.deepana.orderservice.repository.OrderRepository;
import com.deepana.orderservice.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CustomerFeignClient customerFeignClient;

    public OrderServiceImpl(OrderRepository orderRepository, CustomerFeignClient customerFeignClient) {
        this.orderRepository = orderRepository;
        this.customerFeignClient = customerFeignClient;
    }

    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        CustomerResponse customer = customerFeignClient.getCustomerById(orderRequest.getCustomerId());

        Order order = new Order();
        order.setCustomerId(orderRequest.getCustomerId());
        order.setDescription(orderRequest.getDescription());
        order.setQuantity(orderRequest.getQuantity());
        order.setTotalAmount(orderRequest.getTotalAmount());
        order.setStatus(Order.OrderStatus.PENDING);

        Order savedOrder = orderRepository.save(order);
        return mapToResponse(savedOrder, customer);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found with id: " + id));
        CustomerResponse customer = customerFeignClient.getCustomerById(order.getCustomerId());
        return mapToResponse(order, customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        if (orders.isEmpty()) {
            return List.of();
        }
        List<Long> customerIds = orders.stream()
                .map(Order::getCustomerId)
                .distinct()
                .collect(Collectors.toList());
        Map<Long, CustomerResponse> customersById = customerFeignClient.getCustomersByIds(customerIds);
        return orders.stream()
                .map(order -> mapToResponse(order, customersById.get(order.getCustomerId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByCustomerId(Long customerId) {
        CustomerResponse customer = customerFeignClient.getCustomerById(customerId);
        return orderRepository.findByCustomerId(customerId)
                .stream()
                .map(order -> mapToResponse(order, customer))
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse updateOrderStatus(Long id, Order.OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Order not found with id: " + id));
        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());
        Order updatedOrder = orderRepository.save(order);
        CustomerResponse customer = customerFeignClient.getCustomerById(updatedOrder.getCustomerId());
        return mapToResponse(updatedOrder, customer);
    }

    @Override
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new NoSuchElementException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    private OrderResponse mapToResponse(Order order, CustomerResponse customer) {
        String customerName = customer != null
                ? customer.getFirstName() + " " + customer.getLastName()
                : "Unknown";
        return new OrderResponse(
                order.getId(),
                order.getCustomerId(),
                customerName,
                order.getDescription(),
                order.getQuantity(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }
}
