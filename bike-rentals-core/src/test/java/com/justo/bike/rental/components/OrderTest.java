package com.justo.bike.rental.components;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

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
import com.justo.bike.rental.components.account.Account;
import com.justo.bike.rental.components.account.AccountService;
import com.justo.bike.rental.components.order.Order;
import com.justo.bike.rental.components.order.OrderService;
import com.justo.bike.rental.components.order.OrderType;
import com.justo.bike.rental.configuration.AccountConfiguration;
import com.justo.bike.rental.configuration.MongoTestConfiguration;
import com.justo.bike.rental.configuration.OrderConfiguration;

@ActiveProfiles({ "test" })
@EnableAutoConfiguration
@FixMethodOrder(MethodSorters.DEFAULT)
@SpringBootTest(classes = { MongoTestConfiguration.class, AccountConfiguration.class, OrderConfiguration.class })
public class OrderTest extends MainTest {

	private final String TEST_ACCOUNT = "Andrey";

	@Autowired
	private AccountService accountService;

	@Autowired
	private OrderService orderService;
	
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
	public void createOrderTest() {
		Order order = orderService.order(TEST_ACCOUNT, OrderType.DAILY);
		Account account = accountService.find(TEST_ACCOUNT);
		Assert.assertNotNull(order);
		Assert.assertTrue(StringUtils.isNotBlank(order.getId()));
		Assert.assertEquals(OrderType.DAILY, order.getType());
		Assert.assertNotNull(order.getCreatedAt());
		Assert.assertNull(order.getDeliveredAt());
		Assert.assertEquals(account.getName(), order.getAccount().getName());
	}

	@Test
	public void getOrderTest() {
		Order oldOrder = orderService.order(TEST_ACCOUNT, OrderType.DAILY);
		Order order = orderService.get(oldOrder.getId());
		Assert.assertNotNull(order);
		Assert.assertEquals(oldOrder.getId(), order.getId());
	}

	@Test
	public void changeOrderTest() {
		Order order = orderService.order(TEST_ACCOUNT, OrderType.DAILY);
		Order modifiedOrder = orderService.change(order.getId(), OrderType.HOURLY);
		Assert.assertNotNull(modifiedOrder);
		Assert.assertEquals(OrderType.HOURLY, modifiedOrder.getType());
	}
	
	@Test
	public void deliverOrderTest() {
		Order order = orderService.order(TEST_ACCOUNT, OrderType.DAILY);
		Order modifiedOrder = orderService.deliver(order.getId());
		Assert.assertNotNull(modifiedOrder);
		Assert.assertNotNull(modifiedOrder.getDeliveredAt());
		Assert.assertTrue(modifiedOrder.getCreatedAt().isBefore(modifiedOrder.getDeliveredAt()));
	}
	
	@Test(expected = IllegalStateException.class)
	public void invalidDeliverOrderTest() {
		Order order = orderService.order(TEST_ACCOUNT, OrderType.DAILY);
		orderService.deliver(order.getId());
		orderService.deliver(order.getId());
	}
	
	@Test
	public void calculateOrderTest() {
		long diffHours = 2;
		BigDecimal discount = new BigDecimal(0.1D);
		Order order = new Order();
		order.setCreatedAt(Instant.now());
		order.setDeliveredAt(order.getCreatedAt().plus(diffHours, ChronoUnit.HOURS));
		order.setType(OrderType.HOURLY);
		BigDecimal total = orderService.calculateOrder(order, discount);
		Assert.assertNotNull(total);
		Assert.assertEquals(9D, total.doubleValue(), 0.0000000001D);
	}
	
	@Test
	public void calculateOrderTest2() {
		long diffDays = 2;
		BigDecimal discount = new BigDecimal(0.1D);
		Order order = new Order();
		order.setCreatedAt(Instant.now());
		order.setDeliveredAt(order.getCreatedAt().plus(diffDays, ChronoUnit.DAYS));
		order.setType(OrderType.DAILY);
		BigDecimal total = orderService.calculateOrder(order, discount);
		Assert.assertNotNull(total);
		Assert.assertEquals(36D, total.doubleValue(), 0.0000000001D);
	}
	
	@Test
	public void calculateOrderTest3() {
		long diffDays = 2 * 7;
		BigDecimal discount = new BigDecimal(0.1D);
		Order order = new Order();
		order.setCreatedAt(Instant.now());
		order.setDeliveredAt(order.getCreatedAt().plus(diffDays, ChronoUnit.DAYS));
		order.setType(OrderType.WEEKLY);
		BigDecimal total = orderService.calculateOrder(order, discount);
		Assert.assertNotNull(total);
		Assert.assertEquals(108D, total.doubleValue(), 0.0000000001D);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void deliverInvalidOrderTest() {
		orderService.deliver(null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void invalidOrderTest() {
		orderService.order(TEST_ACCOUNT, null);
	}
	
	@Test(expected = IllegalStateException.class)
	public void deliverInvalidOrderTest2() {
		orderService.deliver("Not found order");
	}
	
}
