package com.justo.bike.rental.components.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.apache.commons.lang3.StringUtils;

import com.justo.bike.rental.components.account.Account;
import com.justo.bike.rental.components.account.AccountService;


public class OrderService {

	private final OrderRepository repository;
	private final AccountService accountService;
	
	public OrderService(OrderRepository repository, AccountService accountService) {
		this.repository = repository;
		this.accountService = accountService;
	}
	
	public Order order(String accountName, OrderType type) {
		if (StringUtils.isBlank(accountName) || type == null) {
			throw new IllegalArgumentException("Account name and type cant be nullable or empty");
		}
		
		Account currentAccount = accountService.find(accountName);
		Order newOrder = new Order();
		newOrder.setAccount(currentAccount);
		newOrder.setType(type);
		newOrder.setCreatedAt(Instant.now());
		
		return repository.insert(newOrder);
	}
	
	public Order deliver(String orderId) {
		Order currentOrder = get(orderId);
		if (currentOrder.getDeliveredAt() != null) {
			throw new IllegalStateException("This order have been already delivered. Please check the orderId.");
		}
		
		currentOrder.setDeliveredAt(Instant.now());
		return repository.update(currentOrder);
	}
	
	public Order change(String orderId, OrderType type) {
		Order currentOrder = get(orderId);
		currentOrder.setType(type);
		return repository.update(currentOrder);
	}
	
	public Order get(String orderId) {
		if (StringUtils.isBlank(orderId)) {
			throw new IllegalArgumentException("Order Id cant be blank or null");
		}
		
		Order currentOrder = repository.find(orderId);
		if (currentOrder == null) {
			throw new IllegalStateException("This order id " + orderId + " doesnt exist.");
		}
		
		return currentOrder;
	}
	
	public BigDecimal calculateOrder(Order order, BigDecimal percentDiscount) {
		LocalDateTime start = LocalDateTime.ofInstant(order.getCreatedAt(), ZoneId.systemDefault());
		LocalDateTime end = LocalDateTime.ofInstant(order.getDeliveredAt(), ZoneId.systemDefault());
		long duration = 0L;
		switch (order.getType()) {
		case HOURLY:
			duration = start.until(end, ChronoUnit.HOURS);
			break;
		case DAILY:
			duration = start.until(end, ChronoUnit.DAYS);
			break;
		case WEEKLY:
			duration = start.until(end, ChronoUnit.WEEKS);
			break;
		}

		BigDecimal total = order.getType().getCharging().multiply(new BigDecimal(duration));
		return total.subtract(total.multiply(percentDiscount));
	}
}
