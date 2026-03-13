package com.service.order.service;

import com.service.order.entity.Orders;
import com.service.order.repo.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepo;
    public  OrderServiceImpl(OrderRepository orderRepo){
        this.orderRepo=orderRepo;
    }
    @Override
    public List<Orders> getAllOrders() {
        return orderRepo.findAll();
    }

    @Override
    public Orders createOrder(Orders orders) {
        return orderRepo.save(orders);
    }
}
