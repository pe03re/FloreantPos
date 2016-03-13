package com.floreantpos.model;

import com.floreantpos.main.Application;
import com.floreantpos.model.base.BaseCashTransaction;

public class CashTransaction extends BaseCashTransaction {
	private static final long serialVersionUID = 1L;

	/*[CONSTRUCTOR MARKER BEGIN]*/
	public CashTransaction () {
		super();
	}
	
	public CashTransaction(PosTransaction pt) {
		this.amount = pt.getAmount();
		this.paymentType = PaymentType.CASH.name();
		this.transactionTime = pt.getTransactionTime();
		this.tenderAmount = pt.getTenderAmount();
		this.transactionType = pt.getTransactionType();
		this.setTerminal(Application.getInstance().getTerminal());
		this.setCaptured(true);
		this.setTicket(pt.getTicket());
		this.setUser(Application.getCurrentUser());
	}

	/**
	 * Constructor for primary key
	 */
	public CashTransaction (java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CashTransaction (
		java.lang.Integer id,
		java.lang.String transactionType,
		java.lang.String paymentType) {

		super (
			id,
			transactionType,
			paymentType);
	}

	/*[CONSTRUCTOR MARKER END]*/
}