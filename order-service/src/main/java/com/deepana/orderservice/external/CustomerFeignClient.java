package com.deepana.orderservice.external;

import com.deepana.orderservice.dto.CustomerResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@FeignClient(name = "customer-service", path = "/api/customers")
public interface CustomerFeignClient {

    @GetMapping("/{id}")
    CustomerResponse getCustomerById(@PathVariable("id") Long id);

    @GetMapping("/batch")
    Map<Long, CustomerResponse> getCustomersByIds(@RequestParam("ids") List<Long> ids);
}
