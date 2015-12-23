package com.floreantpos.services;

import java.util.Date;

import com.floreantpos.POSConstants;
import com.floreantpos.PosException;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.TicketDAO;

public class TicketService {
	public static Ticket getTicket(String id) {
		String[] prefix = id.split("/");
		int date = Integer.parseInt(prefix[2]);
		int month = Integer.parseInt(prefix[1]) - 1;
		int year = 0;
		if (month >= 3) {
			year = Integer.parseInt(prefix[0].split("-")[0]);
		} else {
			year = Integer.parseInt(prefix[0].split("-")[1]);
		}
		TicketDAO dao = new TicketDAO();
		Ticket ticket = dao.findTicketByDateAndSerialId(new Date(100 + year, month, date), prefix[3]);

		if (ticket == null) {
			throw new PosException(POSConstants.NO_TICKET_WITH_ID + " " + id + " " + POSConstants.FOUND);
		}

		return ticket;
	}

	public static Ticket getTicket(int ticketId) {
		TicketDAO dao = new TicketDAO();
		Ticket ticket = dao.get(Integer.valueOf(ticketId));

		if (ticket == null) {
			throw new PosException(POSConstants.NO_TICKET_WITH_ID + " " + ticketId + " " + POSConstants.FOUND);
		}

		return ticket;
	}
}
