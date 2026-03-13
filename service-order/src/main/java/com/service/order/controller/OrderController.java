package com.service.order.controller;

import com.service.order.entity.Orders;
import com.service.order.model.CustomerResponse;
import com.service.order.service.CustomerService;
import com.service.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class OrderController {
    @Autowired
    private CustomerService customerService;
    @Autowired
    private OrderService orderService;

    @PostMapping("/orders/placeorder")
    public ResponseEntity<String> placeOrder(@RequestBody Orders newOrder){
        CustomerResponse customerResponse = customerService.getCustomer(newOrder.getCustomerId());
        if((customerResponse.getErrorMsg()!=null) && customerResponse.getErrorMsg().contains("Customer Not Found")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(customerResponse.getErrorMsg());}
        else if(customerResponse.getErrorMsg()!=null && customerResponse.getErrorMsg().contains("Customer Service Temporary Unavailable.Please Try Again Later")){
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Customer Service Temporary Unavailable.Please Try Again Later");}
        return ResponseEntity.ok("Order Placed for Customer: " +orderService.createOrder(newOrder));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<Orders>> getAllOrders(){
        return ResponseEntity.ok(orderService.getAllOrders());
    }
}
