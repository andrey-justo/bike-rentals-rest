package com.justo.bike.rental.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.justo.bike.rental.components.account.AccountRepository;
import com.justo.bike.rental.components.account.AccountService;

@Configuration
public class AccountConfiguration {

	@Bean
	@Autowired
	public AccountRepository accountRepository(MongoTemplate template) {
		return new AccountRepository(template);
	}
	
	@Bean
	@Autowired
	public AccountService accountService(AccountRepository repository) {
		return new AccountService(repository);
	}
	
}
