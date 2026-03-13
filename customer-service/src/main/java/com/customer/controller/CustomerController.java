package com.customer.controller;

import com.customer.entity.Customer;
import com.customer.service.CustomerService;
import com.customer.service.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CustomerController {
    @Autowired
    private CustomerServiceImpl custService;

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers(){
        return ResponseEntity.ok(custService.fetchAllCustomers());
    }
    @PostMapping("/customer")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer newCustomer){
        Customer saved = custService.createCustomer(newCustomer);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    @GetMapping("/customer/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id){
        return ResponseEntity.ok(custService.getCustomerById(id));
    }
    @PutMapping("/customer/{id}")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer updateCustomer,
                                                      @PathVariable Long id){
        return ResponseEntity.ok(custService.updateCustomer(updateCustomer, id));
    }
    @DeleteMapping("/customer/{id}")
    public ResponseEntity<String> deleteCustomerById(@PathVariable Long id){
        String msg = custService.deleteCustomer(id);
        return ResponseEntity.ok(msg);
    }
}
