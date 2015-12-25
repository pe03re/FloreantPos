//package com.floreantpos.bo.ui.explorer;
//
//import java.awt.BorderLayout;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.util.List;
//
//import javax.swing.JButton;
//import javax.swing.JScrollPane;
//
//import org.jdesktop.swingx.JXTable;
//
//import com.floreantpos.POSConstants;
//import com.floreantpos.bo.ui.BOMessageDialog;
//import com.floreantpos.bo.ui.BackOfficeWindow;
//import com.floreantpos.main.Application;
//import com.floreantpos.model.MenuItem;
//import com.floreantpos.model.dao.MenuItemDAO;
//import com.floreantpos.swing.TransparentPanel;
//import com.floreantpos.ui.PosTableRenderer;
//import com.floreantpos.ui.dialog.BeanEditorDialog;
//import com.floreantpos.ui.dialog.ConfirmDeleteDialog;
//import com.floreantpos.ui.model.MenuItemForm;
//
//public class MenuItemExplorer extends TransparentPanel {
//	private List<MenuItem> itemList;
//
//	private JXTable table;
//	private MenuItemExplorerTableModel tableModel;
//	private String currencySymbol;
//
//	public MenuItemExplorer() {
//		currencySymbol = Application.getCurrencySymbol();
//
//		MenuItemDAO dao = new MenuItemDAO();
//		itemList = dao.findAll();
//
//		tableModel = new MenuItemExplorerTableModel();
//		tableModel.setRows(itemList);
//		tableModel.setPageSize(1000);
//		table = new JXTable(tableModel);
//		table.setDefaultRenderer(Object.class, new PosTableRenderer());
//
//		setLayout(new BorderLayout(5, 5));
//		add(new JScrollPane(table));
//		ExplorerButtonPanel explorerButton = new ExplorerButtonPanel();
//		JButton editButton = explorerButton.getEditButton();
//		JButton addButton = explorerButton.getAddButton();
//		JButton deleteButton = explorerButton.getDeleteButton();
//
//		editButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				try {
//					int index = table.getSelectedRow();
//					if (index < 0)
//						return;
//
//					index = table.convertRowIndexToModel(index);
//
//					MenuItem menuItem = itemList.get(index);
//					menuItem = MenuItemDAO.getInstance().initialize(menuItem);
//					itemList.set(index, menuItem);
//
//					MenuItemForm editor = new MenuItemForm(menuItem);
//					BeanEditorDialog dialog = new BeanEditorDialog(editor, BackOfficeWindow.getInstance(), true);
//					dialog.open();
//					if (dialog.isCanceled())
//						return;
//
//					table.repaint();
//				} catch (Throwable x) {
//					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
//				}
//			}
//
//		});
//
//		addButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				try {
//					MenuItemForm editor = new MenuItemForm();
//					BeanEditorDialog dialog = new BeanEditorDialog(editor, BackOfficeWindow.getInstance(), true);
//					dialog.open();
//					if (dialog.isCanceled())
//						return;
//					MenuItem foodItem = (MenuItem) editor.getBean();
//					tableModel.addMenuItem(foodItem);
//				} catch (Throwable x) {
//					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
//				}
//			}
//
//		});
//
//		deleteButton.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				try {
//					int index = table.getSelectedRow();
//					if (index < 0)
//						return;
//
//					index = table.convertRowIndexToModel(index);
//
//					if (ConfirmDeleteDialog.showMessage(MenuItemExplorer.this, POSConstants.CONFIRM_DELETE, POSConstants.DELETE) != ConfirmDeleteDialog.NO) {
//						MenuItem category = itemList.get(index);
//						MenuItemDAO foodItemDAO = new MenuItemDAO();
//						foodItemDAO.delete(category);
//						tableModel.deleteMenuItem(category, index);
//					}
//				} catch (Throwable x) {
//					BOMessageDialog.showError(POSConstants.ERROR_MESSAGE, x);
//				}
//			}
//
//		});
//
//		TransparentPanel panel = new TransparentPanel();
//
//		panel.add(addButton);
//		panel.add(editButton);
//		panel.add(deleteButton);
//		add(panel, BorderLayout.SOUTH);
//	}
//
//	class MenuItemExplorerTableModel extends ListTableModel {
//		String[] columnNames = { POSConstants.NAME, POSConstants.PRICE + " (" + currencySymbol + ")", POSConstants.TAX + " (%)", POSConstants.FOOD_GROUP, POSConstants.VISIBLE, "BUY PRICE", "CHECKBOX" };
//
//		MenuItemExplorerTableModel() {
//			setColumnNames(columnNames);
//		}
//
//		public Object getValueAt(int rowIndex, int columnIndex) {
//			MenuItem item = (MenuItem) rows.get(rowIndex);
//
//			switch (columnIndex) {
//			case 0:
//				return item.getName();
//			case 1:
//				return "₹ " + formatDouble(item.getPrice());
//			case 2:
//				if (item.getTaxList() != null && item.getTaxList().is) {
//					return formatDouble(item.getTax().getRate()) + " %";
//				}
//				return "";
//			case 3:
//				if (item.getParent() != null) {
//					return item.getParent().getName();
//				}
//				return "";
//			case 4:
//				if (item.isVisible()) {
//					return "T";
//				} else {
//					return "F";
//				}
//			case 5:
//				return "₹ " + formatDouble(item.getBuyPrice());
//			case 6:
//				return true;
//			}
//			return null;
//		}
//
//		public void addMenuItem(MenuItem menuItem) {
//			super.addItem(menuItem);
//
//		}
//
//		public void deleteMenuItem(MenuItem category, int index) {
//			super.deleteItem(index);
//		}
//	}
// }
