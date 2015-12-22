package com.floreantpos.actions;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JDialog;

import com.floreantpos.IconFactory;
import com.floreantpos.Messages;
import com.floreantpos.POSConstants;
import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.views.OrderInfoDialog;
import com.floreantpos.ui.views.OrderInfoView;

public class PrintLastTicketAction extends PosAction {

	public PrintLastTicketAction() {
		super(Messages.getString("PrintLastTicket")); //$NON-NLS-1$
	}

	public PrintLastTicketAction(boolean showText, boolean showIcon) {
		if (showText) {
			putValue(Action.NAME, Messages.getString("Clockout")); //$NON-NLS-1$
		}
		if (showIcon) {
			putValue(Action.SMALL_ICON, IconFactory.getIcon("print_32.png")); //$NON-NLS-1$
		}
	}

	@Override
	public void execute() {
		Ticket lastTicket = TicketDAO.getInstance().findLastTicket();
		if (lastTicket != null) {
			lastTicket = TicketDAO.getInstance().loadFullTicket(lastTicket.getId());
			List<Ticket> tickets = new ArrayList<Ticket>();
			tickets.add(lastTicket);
			OrderInfoView view;
			try {
				view = new OrderInfoView(tickets);
				OrderInfoDialog dialog = new OrderInfoDialog(view);
				dialog.setSize(400, 600);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setLocationRelativeTo(Application.getPosWindow());
				dialog.setVisible(true);
			} catch (Exception e) {
				POSMessageDialog.showError(POSConstants.ERROR_MESSAGE, e);
			}
		}
	}

}
