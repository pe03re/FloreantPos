package com.floreantpos.model;

public enum ExpenseTypeEnum {
	FIXED(0), FIXED_RECURRING(1), VARIABLE(-1);

	private int type;

	private ExpenseTypeEnum(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public static ExpenseTypeEnum fromInt(int type) {
		ExpenseTypeEnum[] values = values();

		for (ExpenseTypeEnum expenseTypeEnum : values) {
			if (expenseTypeEnum.type == type) {
				return expenseTypeEnum;
			}
		}
		return FIXED;
	}

	@Override
	public String toString() {
		return name();
	}
}
