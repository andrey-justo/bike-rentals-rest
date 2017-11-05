package com.justo.bike.rental.components;

import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.justo.bike.rental.MainTest;
import com.justo.bike.rental.components.account.AccountService;
import com.justo.bike.rental.components.bill.Bill;
import com.justo.bike.rental.components.bill.BillService;
import com.justo.bike.rental.components.bill.BillStatus;
import com.justo.bike.rental.components.bill.PromotionType;
import com.justo.bike.rental.components.bill.exceptions.BillGeneratorException;
import com.justo.bike.rental.components.order.Order;
import com.justo.bike.rental.components.order.OrderService;
import com.justo.bike.rental.components.order.OrderType;
import com.justo.bike.rental.configuration.AccountConfiguration;
import com.justo.bike.rental.configuration.BillConfiguration;
import com.justo.bike.rental.configuration.MongoTestConfiguration;
import com.justo.bike.rental.configuration.OrderConfiguration;

@ActiveProfiles({ "test" })
@EnableAutoConfiguration
@FixMethodOrder(MethodSorters.DEFAULT)
@SpringBootTest(classes = { MongoTestConfiguration.class, AccountConfiguration.class, OrderConfiguration.class, BillConfiguration.class })
public class BillTest extends MainTest {

	private final String TEST_ACCOUNT = "Andrey";
	private final String FAMILY_ACCOUNT = "Family account";

	@Autowired
	private AccountService accountService;

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private BillService billService;
	
	@Autowired
	private MongoTemplate template;
	
	@After
	public void after() {
		template.getDb().dropDatabase();
	}
	
	@Before
	public void before() {
		accountService.create(TEST_ACCOUNT, "Buenos Aires", false);
	}

	@Test
	public void openBillTest() throws BillGeneratorException {
		Order order = orderService.order(TEST_ACCOUNT, OrderType.DAILY);
		Bill bill = billService.open(order.getId());
		Assert.assertNotNull(bill);
		Assert.assertTrue(StringUtils.isNotBlank(bill.getId()));
		Assert.assertNotNull(bill.getCreatedAt());
		Assert.assertNotNull(bill.getTotal());
		Assert.assertEquals(order.getId(), bill.getOrderId());
		Assert.assertEquals(PromotionType.DEFAULT, bill.getPromotionType());
		Assert.assertEquals(BillStatus.OPENED, bill.getStatus());
		Assert.assertTrue(StringUtils.isNotBlank(bill.getAccountOwner()));
	}
	
	@Test
	public void openFamilyBillTest() throws BillGeneratorException {
		accountService.create(FAMILY_ACCOUNT, "Buenos Aires", true);
		Order order = orderService.order(FAMILY_ACCOUNT, OrderType.DAILY);
		Bill bill = billService.open(order.getId());
		Assert.assertNotNull(bill);
		Assert.assertTrue(StringUtils.isNotBlank(bill.getId()));
		Assert.assertNotNull(bill.getCreatedAt());
		Assert.assertNotNull(bill.getTotal());
		Assert.assertEquals(order.getId(), bill.getOrderId());
		Assert.assertEquals(PromotionType.FAMILY, bill.getPromotionType());
		Assert.assertEquals(BillStatus.OPENED, bill.getStatus());
		Assert.assertTrue(StringUtils.isNotBlank(bill.getAccountOwner()));
	}
	
	@Test(expected = BillGeneratorException.class)
	public void openTwiceBillTest() throws BillGeneratorException {
		Order order = orderService.order(TEST_ACCOUNT, OrderType.DAILY);
		billService.open(order.getId());
		billService.open(order.getId());
	}

	@Test
	public void cancelBillTest() throws BillGeneratorException {
		Order order = orderService.order(TEST_ACCOUNT, OrderType.DAILY);
		Bill bill = billService.open(order.getId());
		Bill canceledBill = billService.cancel(bill.getId(), "Canceled by test");
		Assert.assertNotNull(canceledBill);
		Assert.assertEquals(BillStatus.CANCELED, canceledBill.getStatus());
		Assert.assertTrue(StringUtils.isNotBlank(canceledBill.getReason()));
	}

	@Test
	public void paidBillTest() throws BillGeneratorException {
		Order order = orderService.order(TEST_ACCOUNT, OrderType.DAILY);
		Bill bill = billService.open(order.getId());
		Bill paidBill = billService.pay(bill.getId());
		Assert.assertNotNull(paidBill);
		Assert.assertEquals(BillStatus.PAID, paidBill.getStatus());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void paidInvalidBillTest() {
		billService.pay(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void cancelInvalidBillTest() {
		billService.cancel(null, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void cancelInvalidBillTest2() {
		billService.cancel("Test bill", null);
	}
	
}
