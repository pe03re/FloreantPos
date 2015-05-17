package com.floreantpos.inventory.report;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
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
	private final JCheckBox itemCheck;
	private final JCheckBox compCheck;
	private final JCheckBox vendCheck;
	private final JCheckBox transCheck;
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
		itemCheck = new JCheckBox();

		compCheck = new JCheckBox();
		vendCheck = new JCheckBox();
		transCheck = new JCheckBox();
		addExtraParams();
	}

	public void addExtraParams() {
		final List<InventoryItem> itemList = InventoryItemDAO.getInstance().findAll();
		this.cbItem.setModel(new DefaultComboBoxModel(itemList.toArray(new InventoryItem[0])));
		this.cbItem.addActionListener(items);
		this.cbItem.setRenderer(new MultiRenderer(items));
		itemCheck.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					items.setAllSelected(itemList);
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					items.setAllUnSelected();
				}
			}
		});

		final List<Company> companiesList = CompanyDAO.getInstance().findAll();
		this.cbCompany.setModel(new DefaultComboBoxModel(companiesList.toArray(new Company[0])));
		this.cbCompany.addActionListener(companies);
		this.cbCompany.setRenderer(new MultiRenderer(companies));
		compCheck.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					companies.setAllSelected(companiesList);
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					companies.setAllUnSelected();
				}
			}
		});

		final List<InventoryVendor> vendorsList = InventoryVendorDAO.getInstance().findAllExpenseVendors(false);
		this.cbVendor.setModel(new DefaultComboBoxModel(vendorsList.toArray(new InventoryVendor[0])));
		this.cbVendor.addActionListener(vendors);
		this.cbVendor.setRenderer(new MultiRenderer(vendors));
		vendCheck.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					vendors.setAllSelected(vendorsList);
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					vendors.setAllUnSelected();
				}
			}
		});

		final List<InventoryTransactionType> transactions = InventoryTransactionTypeDAO.getInstance().findAll();
		this.cbTransType.setModel(new DefaultComboBoxModel(transactions.toArray(new InventoryTransactionType[0])));
		this.cbTransType.addActionListener(transTypes);
		this.cbTransType.setRenderer(new MultiRenderer(transTypes));
		transCheck.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					transTypes.setAllSelected(transactions);
				} else if (e.getStateChange() == ItemEvent.DESELECTED) {
					transTypes.setAllUnSelected();
				}
			}
		});

		Component[] comps = $$$getRootComponent$$$().getComponents();

		for (Component c : comps) {
			if (c.getName() != null && c.getName().equals("panel1")) {
				JPanel c1 = (JPanel) c;
				c1.add(new JLabel("Item"), new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED,
						null, null, null, 0, false));
				c1.add(itemCheck, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null,
						null, null, 0, false));
				c1.add(cbItem, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
						GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(160, 24), null, 0, false));

				c1.add(new JLabel("Transaction Type"), new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED,
						GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
				c1.add(transCheck, new GridConstraints(1, 4, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null,
						null, null, 0, false));
				c1.add(cbTransType, new GridConstraints(1, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE,
						GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(
								160, 24), null, 0, false));

				c1.add(new JLabel("Company"), new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED,
						GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
				c1.add(compCheck, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null,
						null, null, 0, false));
				c1.add(cbCompany, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
						GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(160, 24), null, 0, false));

				c1.add(new JLabel("Distributor"), new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED,
						GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
				c1.add(vendCheck, new GridConstraints(2, 4, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null,
						null, null, 0, false));
				c1.add(cbVendor, new GridConstraints(2, 5, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
						GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(160, 24), null, 0, false));

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
