package com.service.order.model;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CustomerResponse {

    private Customer customer;
    private Boolean isError;
    private String errorMsg;

}
