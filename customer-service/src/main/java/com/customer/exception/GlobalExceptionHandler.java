package com.customer.exception;

import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleCustomerNotFound(CustomerNotFoundException ex){
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        pd.setTitle("Customer Not Found");
        pd.setDetail(ex.getMessage());
        pd.setProperty("timestamp",Instant.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(pd);
    }
    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public ResponseEntity<ProblemDetail> handleCustomerAlreadyExists
            (CustomerAlreadyExistsException ex){
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        pd.setTitle("Customer Already Exists");
        pd.setDetail(ex.getMessage());
        pd.setProperty("timestamp",Instant.now());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(pd);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(Exception ex){
        ProblemDetail pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle("INTERNAL SERVER ERROR");
        pd.setDetail(ex.getMessage());
        pd.setProperty("timestamp",Instant.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(pd);

    }

}
