package com.floreantpos.inventory.report;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.model.ExpenseTransaction;

/**
 * @author SOMYA
 * 
 */
public class ExpenseTransactionModel extends ListTableModel {
	SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yy HH:mm");

	DecimalFormat decimalFormat = new DecimalFormat("0.00");

	public ExpenseTransactionModel() {
		super(new String[] { "transactionDate", "expenseTransactionType", "expenseTransactionHead", "expenseVendor", "amount", "vatPaid", "creditCheck", "remark" });
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		ExpenseTransaction data = (ExpenseTransaction) rows.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return dateFormat2.format(data.getTransactionDate());
		case 1:
			return data.getExpenseTransactionType().getName();
		case 2:
			if (data.getExpenseHead() != null) {
				return data.getExpenseHead().getName();
			} else
				return "";
		case 3:
			if (data.getInventoryVendor() != null) {
				return data.getInventoryVendor().getName();
			} else {
				return "";
			}
		case 4:
			return decimalFormat.format(data.getAmount());
		case 5:
			return decimalFormat.format(data.getVatPaid().getRate());
		case 6:
			if (data.isCreditCheck()) {
				return "T";
			} else {
				return "F";
			}
		case 7:
			return data.getRemark();
		}
		return null;
	}
}
