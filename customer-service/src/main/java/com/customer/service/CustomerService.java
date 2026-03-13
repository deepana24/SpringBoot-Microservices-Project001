package com.customer.service;

import com.customer.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<Customer> fetchAllCustomers();
    Customer getCustomerById(Long id);
    Customer createCustomer(Customer newCustomer);
    Customer updateCustomer(Customer newCustomer, Long id);
    String deleteCustomer(Long id);
//    Optional<Customer> getCustomerByEmail(String email);
}
