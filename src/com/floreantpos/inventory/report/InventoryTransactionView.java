package com.floreantpos.inventory.report;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.floreantpos.bo.actions.InventoryTransactionReportAction;
import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.model.InOutEnum;
import com.floreantpos.model.InventoryTransaction;
import com.floreantpos.model.dao.InventoryTransactionDAO;

/**
 * @author SOMYA
 * 
 */
public class InventoryTransactionView extends BaseReportView {

	@Override
	protected ListTableModel getData(Date toDate, Date fromDate, HashMap properties) {
		InventoryTransactionDAO dao = new InventoryTransactionDAO();
		List<InventoryTransaction> findTrans = dao.findTransactions(fromDate, toDate);
		if (findTrans != null && !findTrans.isEmpty()) {
			Double total = getTotalAmount(findTrans);
			properties.put("totalAmount", total.toString());
		}
		InventoryTransactionModel reportModel = new InventoryTransactionModel();
		reportModel.setRows(findTrans);
		reportModel.setPageSize(100000);
		return reportModel;
	}

	@Override
	protected String getReportName() {
		return InventoryTransactionReportAction.INV_TRANS_REPORT;
	}

	public double getTotalAmount(List<InventoryTransaction> trans) {
		double total = 0.0d;
		for (InventoryTransaction it : trans) {
			InOutEnum inOutEnum = InOutEnum.fromInt(it.getInventoryTransactionType().getInOrOut().intValue());
			if (inOutEnum == InOutEnum.IN) {
				if (it.isCreditCheck()) {
					total = total + it.getTotalPrice();
				} else {
					total = total - it.getTotalPrice();
				}
			} else if (inOutEnum == InOutEnum.OUT) {
				if (it.isCreditCheck()) {
					total = total - it.getTotalPrice();
				} else {
					total = total + it.getTotalPrice();
				}
			}
		}
		return total;
	}

}
