package com.floreantpos.ui.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.floreantpos.model.OrderType;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketStatus;

public class TicketUtils {

	public static String getTicketHeader(Ticket ticket) {
		String header = "***";
		Date ticketDate = ticket.getCreateDate();
		String year = null;
		int yearInt = ticketDate.getYear() - 100;
		if (ticketDate.getMonth() >= 3) {
			year = yearInt + "-" + (yearInt + 1);
		} else {
			year = (yearInt - 1) + "-" + yearInt;
		}
		String month = new SimpleDateFormat("/MM/").format(ticketDate);
		String date = new SimpleDateFormat("dd/").format(ticketDate);
		System.out.println(year + month + date + ticket.getSerialId());
		header = year + month + date + ticket.getSerialId();
		return header;
	}

	public static String getTicketPrefix() {
		String header = "***";
		Date ticketDate = new Date();
		String year = null;
		int yearInt = ticketDate.getYear() - 100;
		if (ticketDate.getMonth() >= 3) {
			year = yearInt + "-" + (yearInt + 1);
		} else {
			year = (yearInt - 1) + "-" + yearInt;
		}
		String month = new SimpleDateFormat("/MM/").format(ticketDate);
		String date = new SimpleDateFormat("dd/").format(ticketDate);
		header = year + month + date;
		return header;
	}

	public static String getTicketStatus(Ticket ticket) {
		if (ticket.getType() == OrderType.PICKUP) {
			return "Will pickup";
		} else if (ticket.getType() == OrderType.HOME_DELIVERY) {
			if (ticket.getAssignedDriver() == null) {
				return "Driver not assigned";
			} else if (ticket.isPaid()) {
				return "Out for Delivery";
			}
			return "Driver assigned";
		} else if (ticket.getType() == OrderType.DRIVE_THRU) {
			return "Not delivered";
		}

		if (ticket.isRefunded()) {
			return "REFUNDED";
		}

		if (ticket.isPaid()) {
			if (ticket.getStatus() != null) {
				return TicketStatus.valueOf(ticket.getStatus()).toString();
			}
			return "PAID";
		}

		return "OPEN";
	}

}
