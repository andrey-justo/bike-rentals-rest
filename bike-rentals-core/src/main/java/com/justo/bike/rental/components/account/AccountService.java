package com.justo.bike.rental.components.account;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;

public class AccountService {

	public static final int MAX_PROMOTION_FAMILY = 5;
	public static final int MIN_PROMOTION_FAMILY = 3;
	
	private final AccountRepository repository;
	
	public AccountService(AccountRepository repository) {
		this.repository = repository;
	}
	
	public Account create(String name, String address, boolean isFamily) {
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("The name of account cant be null or empty");
		}
		
		Account newAccount = new Account();
		newAccount.setName(name);
		newAccount.setContactAddress(address);
		Random random = new Random();
		newAccount.setFamilyPromotions(MIN_PROMOTION_FAMILY + random.nextInt(MAX_PROMOTION_FAMILY - MIN_PROMOTION_FAMILY));
		newAccount.setCanUsePromotions(isFamily);

		return repository.insert(newAccount);
	}
	
	public Account change(String name, String address, Boolean isFamily) {
		Account account = find(name);
		if (account == null) {
			throw new IllegalStateException("The account " + name + " doesnt exist.");
		}
		
		if (StringUtils.isNotBlank(address)) {
			account.setContactAddress(address);
		}
		
		if (isFamily != null) {
			account.setCanUsePromotions(isFamily);
		}
		
		return repository.update(account);
	}
	
	public Account find(String name) {
		if (StringUtils.isBlank(name)) {
			throw new IllegalArgumentException("Can't find an account with blank or null name"); 
		}
		
		return repository.find(name);
	}
	
}
