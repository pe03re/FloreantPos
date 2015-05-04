package com.floreantpos.bo.ui.explorer;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;

import com.floreantpos.bo.ui.ModelBrowser;
import com.floreantpos.model.MenuItem;
import com.floreantpos.model.dao.MenuItemDAO;
import com.floreantpos.ui.model.MenuItemForm;

public class MenuItemBrowser extends ModelBrowser<MenuItem> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3216688728242415755L;
	private static MenuItemForm mf = new MenuItemForm();

	public MenuItemBrowser() {
		super(mf);
		JPanel buttonPanel = new JPanel();
		this.browserPanel.add(buttonPanel, "South");
		init(new MenuItemTableModel(), new Dimension(300, 400), new Dimension(650, 400));
		browserTable.getColumn("NAME").setPreferredWidth(125);
		browserTable.getColumn("SELL PRICE").setPreferredWidth(25);
		browserTable.getColumn("BUY PRICE").setPreferredWidth(25);
		browserTable.getColumn("TAX").setPreferredWidth(15);
		browserTable.getColumn("GROUP").setPreferredWidth(100);
		browserTable.getColumn("VISIBLE").setPreferredWidth(10);
		hideDeleteBtn();
		refreshTable();
	}

	public void loadData() {
		List<MenuItem> itemList = MenuItemDAO.getInstance().findAll();
		MenuItemTableModel tableModel = (MenuItemTableModel) this.browserTable.getModel();
		tableModel.setRows(itemList);
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
		mf.setFieldsEnable(false);
	}

	static class MenuItemTableModel extends ListTableModel<MenuItem> {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8008682351957964208L;

		public MenuItemTableModel() {
			super(new String[] { "NAME", "SELL PRICE", "BUY PRICE", "TAX", "GROUP", "VISIBLE" });
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			MenuItem item = (MenuItem) rows.get(rowIndex);

			switch (columnIndex) {
			case 0:
				return item.getName();
			case 1:
				return "₹ " + formatDouble(item.getPrice());
			case 2:
				return "₹ " + formatDouble(item.getBuyPrice());
			case 3:
				if (item.getTax() != null) {
					return formatDouble(item.getTax().getRate()) + " %";
				}
				return "";
			case 4:
				if (item.getParent() != null) {
					return item.getParent().getName();
				}
				return "";
			case 5:
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
