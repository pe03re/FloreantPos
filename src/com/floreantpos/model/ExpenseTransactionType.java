package com.floreantpos.model;

import com.floreantpos.model.base.BaseExpenseTransactionType;

public class ExpenseTransactionType extends BaseExpenseTransactionType {

	/* [CONSTRUCTOR MARKER BEGIN] */
	public ExpenseTransactionType() {
		super();
	}

	/**
	 * Constructor for primary key
	 */
	public ExpenseTransactionType(java.lang.Integer id) {
		super(id);
	}

	/* [CONSTRUCTOR MARKER END] */

	public ExpenseTypeEnum getExpenseTypeEnum() {
		return ExpenseTypeEnum.fromInt(getType());
	}

	public void setExpenseTypeEnum(ExpenseTypeEnum e) {
		super.setType(e.getType());
	}

	@Override
	public String toString() {
		return name;
	}
}