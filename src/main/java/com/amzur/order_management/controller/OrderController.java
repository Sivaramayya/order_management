package com.amzur.order_management.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amzur.order_management.dto.request.OrderRequest;
import com.amzur.order_management.dto.response.OrderResponse;
import com.amzur.order_management.service.OrderService;

import jakarta.validation.Valid;
@RestController
@RequestMapping("/orders")
public class OrderController {
	 @Autowired
	    private OrderService orderService;

	    // Create an order: POST http://localhost:8080/orders
	 @PostMapping
	    public CompletableFuture<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest) {
	        return orderService.createOrder(orderRequest);
	    }
	    
	    
	    @GetMapping("/{orderId}")
	    public CompletableFuture<OrderResponse> getOrderById(@PathVariable Long orderId) {
	    	
	        return orderService.getOrderById(orderId);
	    }
	    @GetMapping("/userId/{userId}")
	    public  CompletableFuture<List<OrderResponse>> getAllOrdersByUserId(@PathVariable Long userId) {
	    	return orderService.getAllOrdersByUserId(userId);
	    }
	   
	    
	    @PutMapping
	    public CompletableFuture<OrderResponse> updateOrder(@Valid @RequestBody OrderRequest orderRequest) {
	        return orderService.updateOrder(orderRequest);
	    }
}
