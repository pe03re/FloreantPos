package com.floreantpos.inventory.report;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.floreantpos.bo.actions.InventoryTransactionReportAction;
import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.model.Company;
import com.floreantpos.model.InOutEnum;
import com.floreantpos.model.InventoryItem;
import com.floreantpos.model.InventoryTransaction;
import com.floreantpos.model.InventoryTransactionType;
import com.floreantpos.model.InventoryVendor;
import com.floreantpos.model.dao.CompanyDAO;
import com.floreantpos.model.dao.InventoryItemDAO;
import com.floreantpos.model.dao.InventoryTransactionDAO;
import com.floreantpos.model.dao.InventoryTransactionTypeDAO;
import com.floreantpos.model.dao.InventoryVendorDAO;
import com.intellij.uiDesigner.core.GridConstraints;

/**
 * @author SOMYA
 * 
 */
public class InventoryTransactionView extends BaseReportView {
	private final JComboBox cbItem;
	private final JComboBox cbCompany;
	private final JComboBox cbVendor;
	private final JComboBox cbTransType;
	SelectionManager<InventoryItem> items = new SelectionManager<InventoryItem>();
	SelectionManager<Company> companies = new SelectionManager<Company>();
	SelectionManager<InventoryVendor> vendors = new SelectionManager<InventoryVendor>();
	SelectionManager<InventoryTransactionType> transTypes = new SelectionManager<InventoryTransactionType>();

	public InventoryTransactionView() {
		super();
		cbItem = new JComboBox();
		cbCompany = new JComboBox();
		cbVendor = new JComboBox();
		cbTransType = new JComboBox();
		addExtraParams();
	}

	public void addExtraParams() {
		List<InventoryItem> itemList = InventoryItemDAO.getInstance().findAll();
		this.cbItem.setModel(new DefaultComboBoxModel(itemList.toArray(new InventoryItem[0])));
		this.cbItem.addActionListener(items);
		this.cbItem.setRenderer(new MultiRenderer(items));

		List<Company> companiesList = CompanyDAO.getInstance().findAll();
		this.cbCompany.setModel(new DefaultComboBoxModel(companiesList.toArray(new Company[0])));
		this.cbCompany.addActionListener(companies);
		this.cbCompany.setRenderer(new MultiRenderer(companies));

		List<InventoryVendor> vendorsList = InventoryVendorDAO.getInstance().findAllExpenseVendors(false);
		this.cbVendor.setModel(new DefaultComboBoxModel(vendorsList.toArray(new InventoryVendor[0])));
		this.cbVendor.addActionListener(vendors);
		this.cbVendor.setRenderer(new MultiRenderer(vendors));

		List<InventoryTransactionType> transactions = InventoryTransactionTypeDAO.getInstance().findAll();
		this.cbTransType.setModel(new DefaultComboBoxModel(transactions.toArray(new InventoryTransactionType[0])));
		this.cbTransType.addActionListener(transTypes);
		this.cbTransType.setRenderer(new MultiRenderer(transTypes));

		Component[] comps = $$$getRootComponent$$$().getComponents();
		for (Component c : comps) {
			if (c.getName() != null && c.getName().equals("panel1")) {
				JPanel c1 = (JPanel) c;
				c1.add(new JLabel("Item"), new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED,
						null, null, null, 0, false));
				c1.add(cbItem, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
						GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(147, 24), null, 0, false));

				c1.add(new JLabel("Company"), new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED,
						GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
				c1.add(cbCompany, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
						GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(147, 24), null, 0, false));

				c1.add(new JLabel("Distributor"), new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED,
						GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
				c1.add(cbVendor, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
						GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(147, 24), null, 0, false));

				c1.add(new JLabel("Transaction Type"), new GridConstraints(2, 6, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED,
						GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
				c1.add(cbTransType, new GridConstraints(2, 7, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
						GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(
								147, 24), null, 0, false));
			}
		}
	}

	@Override
	protected ListTableModel getData(Date toDate, Date fromDate, HashMap properties) {
		InventoryTransactionDAO dao = new InventoryTransactionDAO();
		List<InventoryTransaction> findTrans = dao.findTransactions(fromDate, toDate, items.selectedItems, companies.selectedItems, vendors.selectedItems, transTypes.selectedItems);
		if (findTrans != null && !findTrans.isEmpty()) {
			Double totalPaid = getTotalAmountPaid(findTrans);
			Double totalRefund = getTotalAmountRefund(findTrans);
			properties.put("totalAmountPaid", totalPaid.toString());
			properties.put("totalAmountRefund", totalRefund.toString());
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

	public double getTotalAmountPaid(List<InventoryTransaction> trans) {
		double total = 0.0d;
		for (InventoryTransaction it : trans) {
			InOutEnum inOutEnum = InOutEnum.fromInt(it.getInventoryTransactionType().getInOrOut().intValue());
			if (inOutEnum == InOutEnum.IN) {
				if (it.isCreditCheck()) {
					total = total - it.getTotalPrice();
				} else {
					total = total + it.getTotalPrice();
				}
			}
		}
		return total;
	}

	public double getTotalAmountRefund(List<InventoryTransaction> trans) {
		double total = 0.0d;
		for (InventoryTransaction it : trans) {
			InOutEnum inOutEnum = InOutEnum.fromInt(it.getInventoryTransactionType().getInOrOut().intValue());
			if (inOutEnum == InOutEnum.OUT) {
				if (it.isCreditCheck()) {
					total = total + it.getTotalPrice();
				} else {
					total = total - it.getTotalPrice();
				}
			}
		}
		return total;
	}

}
