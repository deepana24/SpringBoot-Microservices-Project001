package com.deepana.customerservice.service;

import com.deepana.customerservice.dto.CustomerRequest;
import com.deepana.customerservice.dto.CustomerResponse;

import java.util.List;
import java.util.Map;

public interface CustomerService {

    CustomerResponse createCustomer(CustomerRequest customerRequest);

    CustomerResponse getCustomerById(Long id);

    List<CustomerResponse> getAllCustomers();

    Map<Long, CustomerResponse> getCustomersByIds(List<Long> ids);

    CustomerResponse updateCustomer(Long id, CustomerRequest customerRequest);

    void deleteCustomer(Long id);
}
