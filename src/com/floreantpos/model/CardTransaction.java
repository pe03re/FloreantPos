package com.floreantpos.model;

import com.floreantpos.main.Application;
import com.floreantpos.model.base.BaseCardTransaction;

public class CardTransaction extends BaseCardTransaction {
	private static final long serialVersionUID = 1L;

	/* [CONSTRUCTOR MARKER BEGIN] */
	public CardTransaction() {
		super();
	}

	public CardTransaction(PosTransaction pt) {
		this.amount = pt.getAmount();
		this.paymentType = PaymentType.CARD.name();
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
	public CardTransaction(java.lang.Integer id) {
		super(id);
	}

	/**
	 * Constructor for required fields
	 */
	public CardTransaction(java.lang.Integer id, java.lang.String transactionType, java.lang.String paymentType) {

		super(id, transactionType, paymentType);
	}

}