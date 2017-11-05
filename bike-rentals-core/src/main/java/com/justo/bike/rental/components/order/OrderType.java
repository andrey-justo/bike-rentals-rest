package com.justo.bike.rental.components.order;

import java.math.BigDecimal;

public enum OrderType {
	
	HOURLY(5),
	DAILY(20),
	WEEKLY(60);

	private BigDecimal charging;
	
	private OrderType(int charging) {
		this.charging = new BigDecimal(charging);	
	}
	
	public BigDecimal getCharging() {
		return charging;
	}
	
}
