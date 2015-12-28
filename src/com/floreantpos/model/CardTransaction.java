package com.floreantpos.model;

import com.floreantpos.model.base.BaseCardTransaction;

public class CardTransaction extends BaseCardTransaction {
	private static final long serialVersionUID = 1L;

	/* [CONSTRUCTOR MARKER BEGIN] */
	public CardTransaction() {
		super();
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