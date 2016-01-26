package com.floreantpos.bo.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JTabbedPane;

import com.floreantpos.bo.ui.BackOfficeWindow;
import com.floreantpos.ui.views.AdminTicketManagerView;

public class AdminTicketManagerAction extends AbstractAction {

	public static final String adminTicketManagerName = "Admin Ticket Manager";

	public AdminTicketManagerAction() {
		super(adminTicketManagerName);
	}

	public AdminTicketManagerAction(String name) {
		super(name);
	}

	public AdminTicketManagerAction(String name, Icon icon) {
		super(name, icon);
	}

	public void actionPerformed(ActionEvent e) {
		BackOfficeWindow backOfficeWindow = BackOfficeWindow.getInstance();

		AdminTicketManagerView view = null;
		JTabbedPane tabbedPane = backOfficeWindow.getTabbedPane();
		int index = tabbedPane.indexOfTab(adminTicketManagerName);
		if (index == -1) {
			view = new AdminTicketManagerView();
			tabbedPane.addTab(adminTicketManagerName, view);
		} else {
			view = (AdminTicketManagerView) tabbedPane.getComponentAt(index);
		}
		tabbedPane.setSelectedComponent(view);
	}

}
