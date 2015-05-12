package com.floreantpos.inventory.report;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.floreantpos.bo.actions.ExpenseTransactionReportAction;
import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.model.ExpenseTransaction;
import com.floreantpos.model.dao.ExpenseTransactionDAO;

/**
 * @author SOMYA
 * 
 */
public class ExpenseTransactionView extends BaseReportView {

	@Override
	protected ListTableModel getData(Date toDate, Date fromDate, HashMap properties) {
		ExpenseTransactionDAO dao = new ExpenseTransactionDAO();
		List<ExpenseTransaction> findTrans = dao.findTransactions(fromDate, toDate);
		if (findTrans != null && !findTrans.isEmpty()) {
			Double total = getTotalAmount(findTrans);
			properties.put("totalAmount", total.toString());
		}
		ExpenseTransactionModel reportModel = new ExpenseTransactionModel();
		reportModel.setRows(findTrans);
		reportModel.setPageSize(100000);
		return reportModel;
	}

	@Override
	protected String getReportName() {
		return ExpenseTransactionReportAction.EXP_TRANS_REPORT;
	}

	public double getTotalAmount(List<ExpenseTransaction> trans) {
		double total = 0.0d;
		for (ExpenseTransaction it : trans) {
			if (it.isCreditCheck()) {
				total = total + it.getAmount();
			} else {
				total = total - it.getAmount();
			}
		}
		return total;
	}

}
