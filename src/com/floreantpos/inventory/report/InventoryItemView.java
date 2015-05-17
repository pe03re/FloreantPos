package com.floreantpos.inventory.report;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.floreantpos.bo.actions.InventoryItemReportAction;
import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.model.InventoryItem;
import com.floreantpos.model.dao.InventoryItemDAO;

/**
 * @author SOMYA
 * 
 */
public class InventoryItemView extends BaseReportView {

	public InventoryItemView() {
		super();
		showDateFields(false);
	}

	@Override
	protected ListTableModel getData(Date toDate, Date fromDate, HashMap properties) {
		List<InventoryItem> inventoryItems = InventoryItemDAO.getInstance().findAll();
		InventoryItemModel reportModel = new InventoryItemModel();
		reportModel.setRows(inventoryItems);
		reportModel.setPageSize(100000);
		return reportModel;
	}

	@Override
	protected String getReportName() {
		return InventoryItemReportAction.INV_ITEM;
	}

}
