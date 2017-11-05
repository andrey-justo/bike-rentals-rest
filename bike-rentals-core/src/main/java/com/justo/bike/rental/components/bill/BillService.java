package com.justo.bike.rental.components.bill;

import org.apache.commons.lang3.StringUtils;

import com.justo.bike.rental.components.bill.exceptions.BillGeneratorException;
import com.justo.bike.rental.components.order.Order;
import com.justo.bike.rental.components.order.OrderService;

public class BillService {

	private final BillRepository repository;
	private final OrderService orderService;

	public BillService(BillRepository repository, OrderService orderService) {
		this.repository = repository;
		this.orderService = orderService;
	}

	public Bill open(String orderId) throws BillGeneratorException {
		Order closableOrder = orderService.get(orderId);
		if (closableOrder.getDeliveredAt() == null) {
			closableOrder = orderService.deliver(orderId);
		}
		
		if (repository.checkOpened(orderId)) {
			throw new BillGeneratorException("There's a bill opened. You must close it to generate another for the same order.");
		}
		
		Bill bill = generate(closableOrder);
		return repository.insert(bill);
	}
	
	public Bill pay(String billId) {
		if (StringUtils.isBlank(billId)) {
			throw new IllegalArgumentException("BillId can't be blank");
		}
		
		Bill currentBill = repository.find(billId);
		currentBill.setStatus(BillStatus.PAID);
		return repository.update(currentBill);
	}
	
	public Bill cancel(String billId, String reason) {
		if (StringUtils.isBlank(billId)) {
			throw new IllegalArgumentException("BillId can't be blank");
		}
		
		if (StringUtils.isBlank(reason)) {
			throw new IllegalArgumentException("When you cancel a bill you have to give a reason. For example, the customer requested a change or a discount");
		}
		
		Bill currentBill = repository.find(billId);
		currentBill.setStatus(BillStatus.CANCELED);
		currentBill.setReason(reason);
		return repository.update(currentBill);
	}
	
	private Bill generate(Order order) {
		PromotionType type = PromotionType.DEFAULT;
		if (order.getAccount().isCanUsePromotions()) {
			long countedPromotions = repository.countFamilyPromotions(order.getAccount().getName());
			long leftPromotions = order.getAccount().getFamilyPromotions() - countedPromotions;
			type = leftPromotions > 0 ? PromotionType.FAMILY : PromotionType.DEFAULT;
		}
		
		return new Bill(order.getId(), order.getAccount().getName(), type, orderService.calculateOrder(order, type.getDiscount()));
	}
	
}
