package com.amzur.order_management.service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.amzur.order_management.dto.request.OrderRequest;
import com.amzur.order_management.dto.response.OrderResponse;

public interface OrderService {
	
	public CompletableFuture<OrderResponse> createOrder(OrderRequest orderRequest);
   
   public  CompletableFuture<List<OrderResponse>> getOrderById(Long orderId);
   
   public CompletableFuture<List<OrderResponse>> getAllOrdersByUserId(Long userId);
   
   public Long getUserWithMaxOrders(LocalDate date);

}
