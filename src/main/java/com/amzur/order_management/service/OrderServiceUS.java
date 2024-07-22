package com.amzur.order_management.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.amzur.order_management.constants.ApplicationConstants;
import com.amzur.order_management.dto.request.OrderRequest;
import com.amzur.order_management.dto.response.LineItemResponse;
import com.amzur.order_management.dto.response.OrderResponse;
import com.amzur.order_management.entities.LineItemEntity;
import com.amzur.order_management.entities.OrderEntity;
import com.amzur.order_management.handler.ResourceNotAvailable;
import com.amzur.order_management.repository.LineItemRepository;
import com.amzur.order_management.repository.OrderRepository;


@Profile("us")
@Service
public class OrderServiceUS implements OrderService{
	@Autowired
    private OrderRepository orderRepository;
    @Autowired
    private LineItemRepository lineItemRepository;
	
    
    
    
    @Override
	public CompletableFuture<OrderResponse> createOrder(OrderRequest orderRequest) {
    	OrderEntity orderEntity = new OrderEntity();
		orderRequest.setOrderDate(LocalDate.now());
	    BeanUtils.copyProperties(orderRequest, orderEntity);
		orderEntity = orderRepository.save(orderEntity);
		final Long orderId = orderEntity.getOrderId();
		 List<LineItemEntity> lineItems = orderRequest.getBookIds().stream().map(bookId -> {
	            LineItemEntity lineItem = new LineItemEntity();
	            lineItem.setOrderId(orderId);
	            lineItem.setBookId(bookId);
	            return lineItemRepository.save(lineItem);
	        }).collect(Collectors.toList());
		 OrderResponse orderResponse =toOrderResponse(orderEntity, lineItems);
		 return CompletableFuture.completedFuture(orderResponse);
	}
//		//orderEntity.setUserId(orderRequest.getUserId());			
//		final Long orderId = orderEntity.getOrderId();
//		orderRequest.getBookIds().stream()
//	    .map(bookId -> {
//	        LineItemEntity lineItem = new LineItemEntity();
//	        lineItem.setOrderId(orderId);
//	        lineItem.setBookId(bookId);
//	        return lineItem;
//	    }).forEach(lineItemRepository::save);
//		OrderResponse orderResponse = new OrderResponse();
//		//orderResponse.setOrderId(orderId);
//		BeanUtils.copyProperties(orderEntity, orderResponse);

		
    @Async("taskExecutor")
   	public CompletableFuture<OrderResponse> updateOrder(OrderRequest orderRequest) {
    	OrderEntity orderEntity = orderRepository.findById(orderRequest.getOrderId())
                .orElseThrow(() -> new ResourceNotAvailable(ApplicationConstants.RESOURCE_NOT_FOUND));
       	
   		orderRequest.setOrderDate(LocalDate.now());
   	    BeanUtils.copyProperties(orderRequest, orderEntity);
   		orderEntity = orderRepository.save(orderEntity);
   		lineItemRepository.deleteByOrderId(orderRequest.getOrderId());
   		final Long orderId = orderEntity.getOrderId();
   		 List<LineItemEntity> lineItems = orderRequest.getBookIds().stream().map(bookId -> {
   	            LineItemEntity lineItem = new LineItemEntity();
   	            lineItem.setOrderId(orderId);
   	            lineItem.setBookId(bookId);
   	            return lineItemRepository.save(lineItem);
   	        }).collect(Collectors.toList());
   		

   		OrderResponse orderResponse =toOrderResponse(orderEntity, lineItems);
   	
   		
   		return CompletableFuture.completedFuture(orderResponse);
   	}
	
	

	@Override
	  public CompletableFuture<OrderResponse> getOrderById(Long orderId) {
        OrderEntity orderEntity = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotAvailable(ApplicationConstants.RESOURCE_NOT_FOUND));
        
        List<LineItemEntity> lineItems = lineItemRepository.findByOrderId(orderId);
        OrderResponse orderResponse = toOrderResponse(orderEntity, lineItems);
        return CompletableFuture.completedFuture(orderResponse);
    }
		
	@Override
	@Async("taskExecutor")
	public CompletableFuture<List<OrderResponse>> getAllOrdersByUserId(Long userId) {

		return CompletableFuture.completedFuture(orderRepository.findByUserId(userId).stream().map(order -> {
            List<LineItemEntity> lineItems = lineItemRepository.findByOrderId(order.getOrderId());
            return toOrderResponse(order, lineItems);}).collect(Collectors.toList()));
				
	}

	
	 private OrderResponse toOrderResponse(OrderEntity orderEntity, List<LineItemEntity> lineItems) {
	        OrderResponse orderResponse = new OrderResponse();
	        BeanUtils.copyProperties(orderEntity, orderResponse);
	        
	        List<LineItemResponse> lineItemResponses = lineItems.stream().map(lineItem -> {
	           LineItemResponse lineItemResponse = new LineItemResponse();
	           lineItemResponse.setBookId(lineItem.getBookId());
	            return lineItemResponse;
	        }).collect(Collectors.toList());
	        orderResponse.setLineItems(lineItemResponses);
	        return orderResponse;
	    }
	

	@Async("taskExecutor")
	public Map<Long, Long> getOrderCountsByUser(LocalDate date) {
		List<OrderEntity>orders=orderRepository.findAllByOrderDate(date);
		return orders.stream().collect(Collectors.groupingBy(OrderEntity::getUserId,Collectors.counting())) ;
	}
	@Async("taskExecutor")
	public Long getUserWithMaxOrders(LocalDate date) {
        Map<Long, Long> orderCountsByUser = getOrderCountsByUser(date);
        return orderCountsByUser.entrySet()
                                .stream()
                                .max(Map.Entry.comparingByValue())
                                .map(Map.Entry::getKey)
                                .orElse(null);
    }

}
