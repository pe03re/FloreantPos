package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.report.ReportViewer;
import com.floreantpos.sale.report.SaleDetailsReport;

public class SalesDetailsReportAction extends AbstractAction {

	public SalesDetailsReportAction() {
		super("Sale Detail Report");
	}

	public SalesDetailsReportAction(String name) {
		super(name);
	}

	public SalesDetailsReportAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow window = BackOfficeWindow.getInstance();
		JTabbedPane tabbedPane = window.getTabbedPane();

		ReportViewer viewer = null;
		int index = tabbedPane.indexOfTab("Sale Detail Report");
		if (index == -1) {
			viewer = new ReportViewer(new SaleDetailsReport());
			tabbedPane.addTab("Sale Detail Report", viewer);
		} else {
			viewer = (ReportViewer) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(viewer);
	}

}
