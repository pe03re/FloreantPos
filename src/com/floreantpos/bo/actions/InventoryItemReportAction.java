package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.inventory.report.InventoryItemView;

public class InventoryItemReportAction extends AbstractAction {

	public static String INV_ITEM = "InventoryItemReport";

	public InventoryItemReportAction() {
		super(INV_ITEM);
	}

	public InventoryItemReportAction(String name) {
		super(name);
	}

	public InventoryItemReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane = window.getTabbedPane();

		InventoryItemView reportView = null;
		int index = tabbedPane.indexOfTab(INV_ITEM);
		if (index == -1) {
			reportView = new InventoryItemView();
			tabbedPane.addTab(INV_ITEM, reportView);
		} else {
			reportView = (InventoryItemView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
