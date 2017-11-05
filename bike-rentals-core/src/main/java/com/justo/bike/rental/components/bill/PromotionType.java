package com.justo.bike.rental.components.bill;

import java.math.BigDecimal;

public enum PromotionType {

	DEFAULT(0),
	FAMILY(0.3D);
	
	private BigDecimal discount;
	
	private PromotionType(double discount) {
		this.discount = new BigDecimal(discount);
	}
	
	public BigDecimal getDiscount() {
		return discount;
	}
	
}
