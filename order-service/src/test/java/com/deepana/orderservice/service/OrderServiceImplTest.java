package com.deepana.orderservice.service;

import com.deepana.orderservice.dto.CustomerResponse;
import com.deepana.orderservice.dto.OrderRequest;
import com.deepana.orderservice.dto.OrderResponse;
import com.deepana.orderservice.entity.Order;
import com.deepana.orderservice.external.CustomerFeignClient;
import com.deepana.orderservice.repository.OrderRepository;
import com.deepana.orderservice.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CustomerFeignClient customerFeignClient;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order order;
    private OrderRequest orderRequest;
    private CustomerResponse customerResponse;

    @BeforeEach
    void setUp() {
        customerResponse = new CustomerResponse();
        customerResponse.setId(1L);
        customerResponse.setFirstName("John");
        customerResponse.setLastName("Doe");
        customerResponse.setEmail("john.doe@example.com");

        orderRequest = new OrderRequest(1L, "Laptop", 1, new BigDecimal("999.99"));

        order = new Order(1L, 1L, "Laptop", 1, new BigDecimal("999.99"), Order.OrderStatus.PENDING);
    }

    @Test
    void createOrder_Success() {
        when(customerFeignClient.getCustomerById(1L)).thenReturn(customerResponse);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponse response = orderService.createOrder(orderRequest);

        assertThat(response).isNotNull();
        assertThat(response.getDescription()).isEqualTo("Laptop");
        assertThat(response.getCustomerName()).isEqualTo("John Doe");
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void getOrderById_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(customerFeignClient.getCustomerById(1L)).thenReturn(customerResponse);

        OrderResponse response = orderService.getOrderById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    void getOrderById_NotFound_ThrowsException() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.getOrderById(999L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Order not found with id: 999");
    }

    @Test
    void getAllOrders_Success() {
        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(customerFeignClient.getCustomerById(1L)).thenReturn(customerResponse);

        List<OrderResponse> responses = orderService.getAllOrders();

        assertThat(responses).hasSize(1);
    }

    @Test
    void getOrdersByCustomerId_Success() {
        when(customerFeignClient.getCustomerById(1L)).thenReturn(customerResponse);
        when(orderRepository.findByCustomerId(1L)).thenReturn(List.of(order));

        List<OrderResponse> responses = orderService.getOrdersByCustomerId(1L);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getCustomerName()).isEqualTo("John Doe");
    }

    @Test
    void updateOrderStatus_Success() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(customerFeignClient.getCustomerById(1L)).thenReturn(customerResponse);

        OrderResponse response = orderService.updateOrderStatus(1L, Order.OrderStatus.CONFIRMED);

        assertThat(response).isNotNull();
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void deleteOrder_Success() {
        when(orderRepository.existsById(1L)).thenReturn(true);
        doNothing().when(orderRepository).deleteById(1L);

        orderService.deleteOrder(1L);

        verify(orderRepository).deleteById(1L);
    }

    @Test
    void deleteOrder_NotFound_ThrowsException() {
        when(orderRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> orderService.deleteOrder(999L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessageContaining("Order not found with id: 999");
    }
}
