package com.service.order.exception;

public class CustomerAlreadyExistsException extends RuntimeException {

    public CustomerAlreadyExistsException(String email){
        super("Customer with email " + email+ " Already exists");
    }

}
