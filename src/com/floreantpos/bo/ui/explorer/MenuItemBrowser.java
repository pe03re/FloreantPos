package com.floreantpos.bo.ui.explorer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

import com.floreantpos.bo.ui.Command;
import com.floreantpos.bo.ui.ModelBrowser;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.RecepieItem;
import com.floreantpos.model.TaxTreatment;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.ui.model.MenuItemForm;

/**
 * @author SOMYA
 * 
 */
public class MenuItemBrowser extends ModelBrowser<MenuItem> {

	private static final long serialVersionUID = -3216688728242415755L;
	private JButton btnRefreshPrice = new JButton("REFRESH PRICE");

	public MenuItemBrowser() {
		super(new MenuItemForm());
		JPanel buttonPanel = new JPanel();
		this.browserPanel.add(buttonPanel, BorderLayout.SOUTH);
		this.btnRefreshPrice.setActionCommand(Command.REFRESH_BUY_PRICE.name());
		this.btnRefreshPrice.setEnabled(true);
		init(new MenuItemTableModel(), new Dimension(500, 400), new Dimension(500, 400));
		browserTable.getColumn("NAME").setPreferredWidth(125);
		browserTable.getColumn("BASE PRICE").setPreferredWidth(25);
		browserTable.getColumn("TAX").setPreferredWidth(35);
		browserTable.getColumn("TOTAL").setPreferredWidth(15);
		browserTable.getColumn("BUY PRICE").setPreferredWidth(25);
		browserTable.getColumn("GROUP").setPreferredWidth(100);
		browserTable.getColumn("VISIBLE").setPreferredWidth(10);
		hideDeleteBtn();
		refreshTable();
		btnRefreshPrice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshBuyPrice();
			}
		});
		buttonPanel1.add(btnRefreshPrice);
	}

	public void loadData() {
		List<MenuItem> itemList = MenuItemDAO.getInstance().findAll();
		MenuItemTableModel tableModel = (MenuItemTableModel) this.browserTable.getModel();
		tableModel.setRows(itemList);
	}

	private void refreshBuyPrice() {
		MenuItemDAO menuItemDAO = new MenuItemDAO();
		List<MenuItem> itemList = MenuItemDAO.getInstance().findAll();
		for (MenuItem m : itemList) {
			m.setBuyPrice(getBuyPriceFromInventory(m));
			menuItemDAO.saveOrUpdate(m);
		}
		MenuItemTableModel tableModel = (MenuItemTableModel) this.browserTable.getModel();
		tableModel.setRows(itemList);
	}

	private static Double getBuyPriceFromInventory(MenuItem menuItem) {
		double buyPrice = 0.0d;
		if (menuItem != null && menuItem.getRecepie() != null) {
			List<RecepieItem> riList = menuItem.getRecepie().getRecepieItems();
			if (riList != null && !riList.isEmpty()) {
				for (RecepieItem ri : riList) {
					if (ri != null && ri.getInventoryItem() != null) {
						Double itemQty = ri.getPercentage();
						buyPrice += ri.getInventoryItem().getAverageRunitPrice() * itemQty;
					}
				}
			}
		}
		return buyPrice;
	}

	public void refreshTable() {
		loadData();
		super.refreshTable();
	}

	public void refreshUITable() {
		super.refreshTable();
	}

	public void valueChanged(ListSelectionEvent e) {
		super.valueChanged(e);
		beanEditor.setFieldsEnable(false);
	}

	static class MenuItemTableModel extends ListTableModel<MenuItem> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8008682351957964208L;

		public MenuItemTableModel() {
			super(new String[] { "NAME", "BASE PRICE", "TAX", "TOTAL", "BUY PRICE", "GROUP", "VISIBLE" });
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			MenuItem item = (MenuItem) getRowData(rowIndex);

			switch (columnIndex) {
			case 0:
				return item.getName();
			case 1:
				return "Rs " + formatDouble(item.getPrice());
			case 2:
				String taxList = "";
				if (item.getTaxList() != null && !item.getTaxList().isEmpty()) {
					for (TaxTreatment t : item.getTaxList()) {
						taxList = taxList + " + " + t.getTax().getRate() + "%";
					}
					return taxList.substring(2);
				} else {
					return taxList;
				}
			case 3:
				double taxRate = 0;
				if (item.getTaxList() != null && !item.getTaxList().isEmpty()) {
					for (TaxTreatment t : item.getTaxList()) {
						taxRate += t.getTax().getRate();
					}
				}
				if (item.getDiscountRate() < 0) {
					return "Rs " + formatDouble(item.getPrice() * (1 + taxRate / 100));
				} else {
					double discAmt = item.getPrice() * item.getDiscountRate() / 100;
					return "Rs " + formatDouble((item.getPrice() - discAmt) * (1 + taxRate / 100));
				}
			case 4:
				return "Rs " + formatDouble(item.getBuyPrice());
			case 5:
				if (item.getParent() != null) {
					return item.getParent().getName();
				}
				return "";
			case 6:
				if (item.isVisible()) {
					return "T";
				} else {
					return "F";
				}
			}
			return null;
		}
	}

}
