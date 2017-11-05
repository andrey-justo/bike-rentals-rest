package com.justo.bike.rental.components.bill;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.data.annotation.Id;

public class Bill implements Serializable, Cloneable {

	private static final long serialVersionUID = 1082323104337488648L;

	@Id
	private String id;
	private final Instant createdAt;
	private final BigDecimal total;
	private final String accountOwner;
	private final String orderId;
	private final PromotionType promotionType;
	private BillStatus status;
	private String reason;

	public Bill(String orderId, String accountOwner, PromotionType promotionType, BigDecimal total) {
		this.createdAt = Instant.now();
		this.total = total;
		this.promotionType = promotionType;
		this.orderId = orderId;
		this.accountOwner = accountOwner;
		this.status = BillStatus.OPENED;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public PromotionType getPromotionType() {
		return promotionType;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public BillStatus getStatus() {
		return status;
	}

	public void setStatus(BillStatus status) {
		this.status = status;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getId() {
		return id;
	}

	public String getAccountOwner() {
		return accountOwner;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	protected Bill clone() throws CloneNotSupportedException {
		return (Bill) super.clone();
	}

	@Override
	public String toString() {
		return "Bill = {id=" + id + ", createdAt=" + createdAt + ", total=" + total + ", accountOwner=" + accountOwner
				+ ", orderId=" + orderId + ", promotionType=" + promotionType + ", status=" + status + ", reason="
				+ reason + " }";
	}

}
