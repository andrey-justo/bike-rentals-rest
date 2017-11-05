package com.justo.bike.rental.components.order;

import java.io.Serializable;
import java.time.Instant;

import org.springframework.data.annotation.Id;

import com.justo.bike.rental.components.account.Account;

public class Order implements Serializable, Cloneable {

	private static final long serialVersionUID = 1238921510895612942L;

	@Id
	private String id;
	private Instant createdAt;
	private Instant deliveredAt;
	private OrderType type;
	private Account account;

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getDeliveredAt() {
		return deliveredAt;
	}

	public void setDeliveredAt(Instant deliveredAt) {
		this.deliveredAt = deliveredAt;
	}

	public OrderType getType() {
		return type;
	}

	public void setType(OrderType type) {
		this.type = type;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	protected Order clone() throws CloneNotSupportedException {
		return (Order) super.clone();
	}

	@Override
	public String toString() {
		return "Order = {id=" + id + ", createdAt=" + createdAt + ", deliveredAt=" + deliveredAt + ", type=" + type
				+ ", account=" + account + " }";
	}

}
