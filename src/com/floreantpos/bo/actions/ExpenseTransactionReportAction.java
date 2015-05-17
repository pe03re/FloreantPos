package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.inventory.report.ExpenseTransactionView;

public class ExpenseTransactionReportAction extends AbstractAction {

	public static String EXP_TRANS_REPORT = "ExpenseTransactionReport";

	public ExpenseTransactionReportAction() {
		super("Expense Transaction Report");
	}

	public ExpenseTransactionReportAction(String name) {
		super(name);
	}

	public ExpenseTransactionReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane = window.getTabbedPane();

		ExpenseTransactionView reportView = null;
		int index = tabbedPane.indexOfTab(EXP_TRANS_REPORT);
		if (index == -1) {
			reportView = new ExpenseTransactionView();
			tabbedPane.addTab("Expense Transaction Report", reportView);
		} else {
			reportView = (ExpenseTransactionView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(reportView);
	}

}
