package com.justo.bike.rental.components.bill;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.justo.bike.rental.mongo.AbstractRepository;

public class BillRepository extends AbstractRepository<Bill> {

	private static final String BILL_COLLECTION_NAME = "bill";
	
	public BillRepository(MongoTemplate template) {
		super(template, Bill.class);
	}
	
	public long countFamilyPromotions(String accountName) {
		Query query = new Query(Criteria.where("accountOwner").is(accountName).andOperator(Criteria.where("status").is(BillStatus.PAID)));
        return template.count(query, BILL_COLLECTION_NAME);
	}
	
	public boolean checkOpened(String orderId) {
		Query query = new Query(Criteria.where("orderId").is(orderId).andOperator(Criteria.where("status").is(BillStatus.OPENED)));
        return template.exists(query, BILL_COLLECTION_NAME);
	}

	@Override
	public String collectionName() {
		return BILL_COLLECTION_NAME;
	}

}
