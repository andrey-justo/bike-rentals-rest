package com.justo.bike.rental.components;

import org.junit.After;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.justo.bike.rental.MainTest;
import com.justo.bike.rental.components.account.Account;
import com.justo.bike.rental.components.account.AccountService;
import com.justo.bike.rental.configuration.AccountConfiguration;
import com.justo.bike.rental.configuration.MongoTestConfiguration;

@ActiveProfiles({ "test" })
@EnableAutoConfiguration
@FixMethodOrder(MethodSorters.DEFAULT)
@SpringBootTest(classes = { MongoTestConfiguration.class, AccountConfiguration.class })
public class AccountTest extends MainTest {

	private final String TEST_ACCOUNT = "Andrey";

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private MongoTemplate template;
	
	@After
	public void after() {
		template.getDb().dropDatabase();
	}

	@Test
	public void createAccountTest() {
		Account account = accountService.create(TEST_ACCOUNT, "Buenos Aires", false);
		Assert.assertNotNull(account);
		Assert.assertEquals(false, account.isCanUsePromotions());
		Assert.assertEquals(TEST_ACCOUNT, account.getName());
		Assert.assertTrue(account.getFamilyPromotions() > 0);
		Assert.assertTrue(account.getFamilyPromotions() >= AccountService.MIN_PROMOTION_FAMILY);
		Assert.assertTrue(account.getFamilyPromotions() <= AccountService.MAX_PROMOTION_FAMILY);
	}

	@Test
	public void getAccountTest() {
		accountService.create(TEST_ACCOUNT, "Buenos Aires", false);
		Account account = accountService.find(TEST_ACCOUNT);
		Assert.assertNotNull(account);
		Assert.assertEquals(false, account.isCanUsePromotions());
	}

	@Test
	public void changeAccountTest() {
		accountService.create(TEST_ACCOUNT, "Buenos Aires", false);
		Account account = accountService.change(TEST_ACCOUNT, null, null);
		Assert.assertNotNull(account);
		Assert.assertEquals(false, account.isCanUsePromotions());
	}
	
	@Test
	public void changeAccountTest2() {
		accountService.create(TEST_ACCOUNT, "Buenos Aires", false);
		Account account = accountService.change(TEST_ACCOUNT, "Brasil", true);
		Assert.assertNotNull(account);
		Assert.assertEquals(true, account.isCanUsePromotions());
		Assert.assertEquals("Brasil", account.getContactAddress());
	}

	@Test(expected = IllegalArgumentException.class)
	public void changeInvalidAccountTest() {
		accountService.change("", "Brasil", null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void createInvalidAccountTest() {
		accountService.create("", "Brasil", false);
	}

	@Test(expected = IllegalStateException.class)
	public void changeNotFoundAccountTest() {
		accountService.change("Not Found", "Brasil", null);
	}
}
