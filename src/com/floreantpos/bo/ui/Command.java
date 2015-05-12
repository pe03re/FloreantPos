package com.floreantpos.bo.ui;

public enum Command {
	NEW, EDIT, DELETE, SAVE, CANCEL, NEW_TRANSACTION, UNKNOWN, NEW_EXPENSE, REFRESH_BUY_PRICE;

	public static Command fromString(String s) {
		Command[] values = values();
		for (Command command : values) {
			if (command.name().equalsIgnoreCase(s)) {
				return command;
			}
		}

		return UNKNOWN;
	}
}
