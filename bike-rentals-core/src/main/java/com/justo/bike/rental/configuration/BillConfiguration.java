package com.justo.bike.rental.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.justo.bike.rental.components.bill.BillRepository;
import com.justo.bike.rental.components.bill.BillService;
import com.justo.bike.rental.components.order.OrderService;

@Configuration
public class BillConfiguration {

	@Bean
	@Autowired
	public BillRepository billRepository(MongoTemplate template) {
		return new BillRepository(template);
	}
	
	@Bean
	@Autowired
	public BillService billService(BillRepository repository, OrderService orderService) {
		return new BillService(repository, orderService);
	}
	
}
