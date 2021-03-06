package com.floreantpos.actions;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.JDialog;

import com.floreantpos.IconFactory;
import com.floreantpos.main.Application;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;
import com.floreantpos.services.TicketService;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.dialog.TicketSelectionDialog;
import com.floreantpos.ui.util.TicketUtils;
import com.floreantpos.ui.views.OrderInfoDialog;
import com.floreantpos.ui.views.OrderInfoView;

public class SearchTicketAction extends PosAction {

	private static final String searchString = "Search Ticket";

	public SearchTicketAction() {
		super(searchString); //$NON-NLS-1$
	}

	public SearchTicketAction(boolean showText, boolean showIcon) {
		if (showText) {
			putValue(Action.NAME, searchString); //$NON-NLS-1$
		}
		if (showIcon) {
			putValue(Action.SMALL_ICON, IconFactory.getIcon("search_32.png")); //$NON-NLS-1$
		}
	}

	@Override
	public void execute() {
		try {
			String ticketId = TicketSelectionDialog.takeTicketInput("Enter ticket id", TicketUtils.getTicketPrefix());
			if (ticketId == null) {
				return;
			}

			Ticket ticket = TicketService.getTicket(ticketId);
			if (ticket != null) {
				ticket = TicketDAO.getInstance().loadFullTicket(ticket.getId());
				List<Ticket> tickets = new ArrayList<Ticket>();
				tickets.add(ticket);
				OrderInfoView view;
				view = new OrderInfoView(tickets);
				OrderInfoDialog dialog = new OrderInfoDialog(view);
				dialog.setSize(400, 600);
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setLocationRelativeTo(Application.getPosWindow());
				dialog.setVisible(true);
			}
		} catch (Exception e) {
			POSMessageDialog.showError(e.getMessage(), e);
		}
	}

}
