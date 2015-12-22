package com.floreantpos.ui.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.floreantpos.model.Ticket;

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

}
