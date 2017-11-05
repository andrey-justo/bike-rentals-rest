package com.justo.bike.rental.components.account;

import java.io.Serializable;

import org.springframework.data.annotation.Id;

public class Account implements Serializable, Cloneable {

	private static final long serialVersionUID = 3854064005518669987L;

	@Id
	private String name;
	private String contactAddress;
	private boolean canUsePromotions;
	private long familyPromotions;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactAddress() {
		return contactAddress;
	}

	public void setContactAddress(String contactAddress) {
		this.contactAddress = contactAddress;
	}

	public long getFamilyPromotions() {
		return familyPromotions;
	}

	public void setFamilyPromotions(long familyPromotions) {
		this.familyPromotions = familyPromotions;
	}

	public boolean isCanUsePromotions() {
		return canUsePromotions;
	}

	public void setCanUsePromotions(boolean canUsePromotions) {
		this.canUsePromotions = canUsePromotions;
	}

	@Override
	protected Account clone() throws CloneNotSupportedException {
		return (Account) super.clone();
	}

	@Override
	public String toString() {
		return "Account = {name=" + name + ", contactAddress=" + contactAddress + ", canUsePromotions="
				+ canUsePromotions + ", familyPromotions=" + familyPromotions + " }";
	}

}
