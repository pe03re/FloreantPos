package com.floreantpos.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

import org.jdesktop.swingx.JXTable;

import com.floreantpos.POSConstants;
import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.model.PaymentType;
import com.floreantpos.model.Ticket;
import com.floreantpos.model.TicketItem;
import com.floreantpos.swing.PosScrollPane;
import com.floreantpos.ui.dialog.POSMessageDialog;
import com.floreantpos.ui.util.TicketUtils;
import com.floreantpos.util.NumberUtil;

public class TicketListView extends JPanel {
	private JXTable table;
	private TicketListTableModel tableModel;

	public TicketListView() {
		table = new TicketListTable();
		table.setSortable(false);
		table.setColumnControlVisible(false);
		table.setModel(tableModel = new TicketListTableModel());
		tableModel.setPageSize(10000);
		table.setRowHeight(60);
		// table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		// table.setDefaultRenderer(Object.class, new PosTableRenderer());
		table.setGridColor(Color.LIGHT_GRAY);

		TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setPreferredWidth(5);
		columnModel.getColumn(1).setPreferredWidth(2);
		columnModel.getColumn(2).setPreferredWidth(60);
		columnModel.getColumn(3).setPreferredWidth(250);
		columnModel.getColumn(4).setPreferredWidth(30);
		columnModel.getColumn(5).setPreferredWidth(50);
		columnModel.getColumn(6).setPreferredWidth(10);
		columnModel.getColumn(7).setPreferredWidth(10);
		columnModel.getColumn(8).setPreferredWidth(10);
		columnModel.getColumn(9).setPreferredWidth(60);

		columnModel.getColumn(0).setCellRenderer(new ColoredCellRenderer());
		columnModel.getColumn(1).setCellRenderer(new ColoredCellRenderer());
		columnModel.getColumn(2).setCellRenderer(new ColoredCellRenderer());
		columnModel.getColumn(3).setCellRenderer(new ColoredCellRenderer());
		columnModel.getColumn(4).setCellRenderer(new ColoredCellRenderer());
		columnModel.getColumn(5).setCellRenderer(new ColoredCellRenderer());
		columnModel.getColumn(6).setCellRenderer(new ColoredCellRenderer());
		columnModel.getColumn(7).setCellRenderer(new ColoredCellRenderer());
		columnModel.getColumn(8).setCellRenderer(new ColoredCellRenderer());
		columnModel.getColumn(9).setCellRenderer(new ColoredCellRenderer());

		PosScrollPane scrollPane = new PosScrollPane(table, PosScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, PosScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		setLayout(new BorderLayout());

		add(scrollPane);
	}

	public void setTickets(List<Ticket> tickets) {
		tableModel.setRows(tickets);
	}

	public void addTicket(Ticket ticket) {
		tableModel.addItem(ticket);
	}

	public Ticket getSelectedTicket() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow < 0) {
			return null;
		}

		return (Ticket) tableModel.getRowData(selectedRow);
	}

	public List<Ticket> getSelectedTickets() {
		int[] selectedRows = table.getSelectedRows();

		ArrayList<Ticket> tickets = new ArrayList<Ticket>(selectedRows.length);

		for (int i = 0; i < selectedRows.length; i++) {
			Ticket ticket = (Ticket) tableModel.getRowData(selectedRows[i]);
			tickets.add(ticket);
		}

		return tickets;
	}

	// public void removeTicket(Ticket ticket) {
	// tableModel.
	// }

	private class TicketListTable extends JXTable {

		public TicketListTable() {
		}

		@Override
		public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
			ListSelectionModel selectionModel = getSelectionModel();
			boolean selected = selectionModel.isSelectedIndex(rowIndex);
			if (selected) {
				selectionModel.removeSelectionInterval(rowIndex, rowIndex);
			} else {
				selectionModel.addSelectionInterval(rowIndex, rowIndex);
			}
		}
	}

	private class TicketListTableModel extends ListTableModel {
		public TicketListTableModel() {
			super(new String[] { "TICKET ID", "TOKEN", "DATE", "ITEMS", POSConstants.TICKET_TYPE, "STATUS", POSConstants.TOTAL, POSConstants.DUE, "MODE", "CUSTOMER" });
		}

		public Object getValueAt(int rowIndex, int columnIndex) {
			Ticket ticket = (Ticket) rows.get(rowIndex);
			Color c = Color.WHITE;
			if (ticket.getPaymentMode().contains(PaymentType.CARD.name())) {
				c = new Color(255,218,185);
			} else if (ticket.getPaymentMode().contains(PaymentType.CASH.name())) {
				c = Color.WHITE;
			}
			switch (columnIndex) {
			case 0:
				return new ColoredCellData(ticket.getCreateDate().getDate() + "/" + ticket.getSerialId(), c);
			case 1:
				return new ColoredCellData(ticket.getTokenNo().toString(), c);
			case 2:
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy h:mm aaa");
				return new ColoredCellData(simpleDateFormat.format(ticket.getCreateDate()), c);
			case 3:
				List<TicketItem> list = ticket.getTicketItems();
				StringBuilder items = new StringBuilder("");
				int count = 2;
				for (TicketItem ti : list) {
					if (ti.getName().startsWith("**"))
						continue;
					if (count > 0) {
						items.append(", ");
						items.append(ti.getNameDisplay());
						count--;
					} else {
						items.append("+");
						items.append((list.size() - 2));
						items.append(" more");
						break;
					}
				}
				if (items.length() > 0 && items.charAt(0) == ',') {
					return new ColoredCellData(items.substring(2), c);
				} else {
					return new ColoredCellData("", c);
				}

			case 4:
				return new ColoredCellData(ticket.getType().name(), c);
			case 5:
				return new ColoredCellData(TicketUtils.getTicketStatus(ticket), c);
			case 6:
				return new ColoredCellData(String.valueOf(NumberUtil.mathRoundOff(ticket.getTotalAmount())), c);
			case 7:
				return new ColoredCellData(String.valueOf(NumberUtil.mathRoundOff(ticket.getDueAmount())), c);
			case 8:
				if (TicketUtils.getTicketStatus(ticket).contains("OPEN")) {
					return new ColoredCellData("-", c);
				} else {
					return new ColoredCellData(ticket.getPaymentMode(), c);
				}
			case 9:
				if (ticket.getCustomer() != null) {
					return new ColoredCellData(ticket.getCustomer().getName(), c);
				}
				return new ColoredCellData("***", c);
			}

			return null;
		}
	}

	public Ticket getFirstSelectedTicket() {
		List<Ticket> selectedTickets = getSelectedTickets();

		if (selectedTickets.size() == 0 || selectedTickets.size() > 1) {
			POSMessageDialog.showMessage("Please select a ticket");
			return null;
		}

		Ticket ticket = selectedTickets.get(0);

		return ticket;
	}

	public int getFirstSelectedTicketId() {
		Ticket ticket = getFirstSelectedTicket();
		if (ticket == null) {
			return -1;
		}

		return ticket.getId();
	}

	public JXTable getTable() {
		return table;
	}
}
