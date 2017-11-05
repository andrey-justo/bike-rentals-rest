package com.justo.bike.rental.components.order;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.justo.bike.rental.mongo.AbstractRepository;

public class OrderRepository extends AbstractRepository<Order> {

	private static final String ORDER_COLLECTION_NAME = "order";

	public OrderRepository(MongoTemplate template) {
		super(template, Order.class);
	}

	@Override
	public String collectionName() {
		return ORDER_COLLECTION_NAME;
	}

}
