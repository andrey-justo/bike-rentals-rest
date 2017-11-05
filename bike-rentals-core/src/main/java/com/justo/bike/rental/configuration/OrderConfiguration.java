package com.justo.bike.rental.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.justo.bike.rental.components.account.AccountService;
import com.justo.bike.rental.components.order.OrderRepository;
import com.justo.bike.rental.components.order.OrderService;

@Configuration
@Import({AccountConfiguration.class})
public class OrderConfiguration {

	@Bean
	@Autowired
	public OrderRepository orderRepository(MongoTemplate template) {
		return new OrderRepository(template);
	}
	
	@Bean
	@Autowired
	public OrderService orderService(OrderRepository repository, AccountService accountService) {
		return new OrderService(repository, accountService);
	}
	
}
