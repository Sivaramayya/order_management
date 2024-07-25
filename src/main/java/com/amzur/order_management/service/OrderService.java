package com.amzur.order_management.service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.amzur.order_management.dto.request.OrderRequest;
import com.amzur.order_management.dto.response.OrderResponse;

import jakarta.validation.Valid;

public interface OrderService {
	
	public CompletableFuture<OrderResponse> createOrder(OrderRequest orderRequest);
   
   public  CompletableFuture<OrderResponse> getOrderById(Long orderId);
   
   public CompletableFuture<List<OrderResponse>> getAllOrdersByUserId(Long userId);
   
   public Long getUserWithMaxOrders(LocalDate date);

public CompletableFuture<OrderResponse> updateOrder(@Valid OrderRequest orderRequest);

}
