package com.amzur.order_management.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ScheduledTasks {
	@Autowired
	private OrderService orderService;
	@Autowired
	private EmailService emailService;
	@Autowired
	private RestTemplate restTemplate;
	@Scheduled(cron="0 47 18 * * ?")
	public void sendEmailToTopUSer() {
		LocalDate today=LocalDate.now();
		Long topUserId=orderService.getUserWithMaxOrders(today);
		if(topUserId !=null) {
			String email=getUserEmailById(topUserId);
			String subject="Congratulations...!";
			String text="you have the maximum orders for today...!";
			emailService.sendEmail(email,subject,text);
		}
	}
	
	private String getUserEmailById(Long topUserId) {
		LocalDate today=LocalDate.now();
		Long userId=orderService.getUserWithMaxOrders(today);
		String url="http://localhost:9191/user-management/users/"+userId;
		ResponseEntity<String>response=restTemplate.exchange(url,HttpMethod.GET,null,new ParameterizedTypeReference<String>() {});
		return response.getBody();
	}
}
