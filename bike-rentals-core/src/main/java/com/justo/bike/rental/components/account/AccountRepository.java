package com.justo.bike.rental.components.account;

import org.springframework.data.mongodb.core.MongoTemplate;

import com.justo.bike.rental.mongo.AbstractRepository;

public class AccountRepository extends AbstractRepository<Account> {

	public static final String ACCOUNT_COLLECTION_NAME = "account";

	public AccountRepository(MongoTemplate template) {
		super(template, Account.class);
	}

	@Override
	public String collectionName() {
		return ACCOUNT_COLLECTION_NAME;
	}


}
