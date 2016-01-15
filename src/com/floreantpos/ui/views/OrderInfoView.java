package com.floreantpos.ui.views;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;
import net.sf.jasperreports.engine.JasperPrint;

import com.floreantpos.model.PosTransaction;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.dao.PosTransactionDAO;
import com.floreantpos.report.ReceiptPrintService;
import com.floreantpos.report.TicketPrintProperties;
import com.floreantpos.swing.PosScrollPane;
import com.floreantpos.ui.util.TicketUtils;

public class OrderInfoView extends JPanel {
	private List<Ticket> tickets;

	public OrderInfoView(List<Ticket> tickets) throws Exception {
		super();
		this.tickets = tickets;
		createUI();
	}

	private void createUI() throws Exception {
		JPanel reportPanel = new JPanel(new MigLayout("wrap 1, ax 50%", "", ""));
		PosScrollPane scrollPane = new PosScrollPane(reportPanel);
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);

		for (int i = 0; i < tickets.size(); i++) {
			Ticket ticket = (Ticket) tickets.get(i);

			JLabel ticketLabel = new JLabel();
			ticketLabel.setText("#TICKET STATUS: " + TicketUtils.getTicketStatus(ticket));

			TicketPrintProperties printProperties = new TicketPrintProperties("Invoice: " + TicketUtils.getTicketNumber(ticket), true, true, true);
			PosTransaction trans = PosTransactionDAO.getInstance().getConsolidatedTransactionByTicket(ticket);
			HashMap map = ReceiptPrintService.populateTicketProperties(ticket, printProperties, trans);
			JasperPrint jasperPrint = ReceiptPrintService.createGeneralTicketPrint(ticket, map, trans);

			TicketReceiptView receiptView = new TicketReceiptView(jasperPrint);
			reportPanel.add(ticketLabel);
			reportPanel.add(receiptView.getReportPanel());
		}

		setLayout(new BorderLayout());
		add(scrollPane);
	}

	public void print() throws Exception {
		for (Iterator iter = tickets.iterator(); iter.hasNext();) {
			Ticket ticket = (Ticket) iter.next();

			ReceiptPrintService.printTicket(ticket);
		}
	}

	public void kitchenPrint() throws Exception {
		for (Iterator iter = tickets.iterator(); iter.hasNext();) {
			Ticket ticket = (Ticket) iter.next();

			ReceiptPrintService.printToKitchen(ticket, true);
		}
	}
}
