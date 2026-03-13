package com.service.order.service;

import com.service.order.client.CustomerClient;
import com.service.order.model.Customer;
import com.service.order.model.CustomerResponse;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerClient cusClient;
    @CircuitBreaker(name = "customerServiceCB", fallbackMethod = "fallbackGetCustomer")
    public CustomerResponse getCustomer(Long id){
        CustomerResponse customerResponse = new CustomerResponse();
        try {
            Customer customer = cusClient.getCustomerById(id);
            if (customer != null) {
                customerResponse.setCustomer(customer);
                customerResponse.setIsError(false);
                customerResponse.setErrorMsg(null);
            }
        } catch(FeignException.NotFound e){
            customerResponse.setIsError(true);
            customerResponse.setErrorMsg("Customer Not Found with id: " + id);
            System.out.println("Customer not found: " + e.getMessage());
        } catch (FeignException.ServiceUnavailable e) {
        // This catches 503 SERVICE UNAVAILABLE
        customerResponse.setIsError(true);
        customerResponse.setErrorMsg("Customer Service Temporary Unavailable. Please Try Again Later");
    } catch (Exception e) {
        // Any other exception
        customerResponse.setIsError(true);
        customerResponse.setErrorMsg("Error calling customer service: " + e.getMessage());
    }
        return customerResponse;
}
     public CustomerResponse fallbackGetCustomer(Long id, Throwable t){
        CustomerResponse customerResponse=new CustomerResponse();
        customerResponse.setIsError(true);
         // Check the type of exception
         if (t instanceof FeignException.NotFound) {
             customerResponse.setErrorMsg("Customer Not Found with id: " + id);
         } else if (t instanceof FeignException.ServiceUnavailable ||
                 t instanceof java.net.SocketTimeoutException ||
                 t instanceof java.net.ConnectException) {
             customerResponse.setErrorMsg("Customer Service Temporary Unavailable. Please Try Again Later");
         } else {
             customerResponse.setErrorMsg("Customer Service Error: " + t.getMessage());
         }
         return customerResponse;
     }
}
