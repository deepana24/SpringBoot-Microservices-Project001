package com.customer.service;

import com.customer.entity.Customer;
import com.customer.exception.CustomerAlreadyExistsException;
import com.customer.exception.CustomerNotFoundException;
import com.customer.repo.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository custRepo;
    public CustomerServiceImpl(CustomerRepository custRepo) {
        this.custRepo = custRepo;
    }
    @Override
    public List<Customer> fetchAllCustomers() {
        return custRepo.findAll();
    }
    @Override
    public Customer getCustomerById(Long id) {
        return custRepo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }
    @Override
    public Customer createCustomer(Customer newCustomer) {
        custRepo.findByEmail(newCustomer.getEmail())
                .ifPresent(c -> {
                    throw new CustomerAlreadyExistsException(newCustomer.getEmail()) ;
                });
        return custRepo.save(newCustomer);
    }
    @Override
    public Customer updateCustomer(Customer newCustomer, Long id) {
        Customer existingCustomer = custRepo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        existingCustomer.setName(newCustomer.getName());
        existingCustomer.setEmail(newCustomer.getEmail());
        existingCustomer.setLocation(newCustomer.getLocation());
        return custRepo.save(existingCustomer);
    }
    @Override
    public String deleteCustomer(Long id) {
        Customer cust = custRepo.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        custRepo.deleteById(id);
        return "Customer deleted successfully for ID " + id;
    }

/*    @Override
    public Optional<Customer> getCustomerByEmail(String email) {
      return custRepo.getCustomerByEmail(email);
    }*/
}