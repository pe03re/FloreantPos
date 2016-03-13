package com.floreantpos.model;

import com.floreantpos.model.base.BaseExpenseTransaction;



public class ExpenseTransaction extends BaseExpenseTransaction {
	private static final long serialVersionUID = 1L;

/*[CONSTRUCTOR MARKER BEGIN]*/
	public ExpenseTransaction () {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ExpenseTransaction (java.lang.Integer id) {
		super(id);
	}

/*[CONSTRUCTOR MARKER END]*/


}