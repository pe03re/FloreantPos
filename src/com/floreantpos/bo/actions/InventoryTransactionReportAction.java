package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.inventory.report.InventoryTransactionView;

public class InventoryTransactionReportAction extends AbstractAction {

	public static String INV_TRANS_REPORT = "InventoryTransactionReport";

	public InventoryTransactionReportAction() {
		super("Inventory Transaction Report");
	}

	public InventoryTransactionReportAction(String name) {
		super(name);
	}

	public InventoryTransactionReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane = window.getTabbedPane();

		InventoryTransactionView reportView = null;
		int index = tabbedPane.indexOfTab(INV_TRANS_REPORT);
		if (index == -1) {
			reportView = new InventoryTransactionView();
			tabbedPane.addTab(INV_TRANS_REPORT, reportView);
		} else {
			reportView = (InventoryTransactionView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
