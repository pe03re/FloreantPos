package com.floreantpos.report;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.floreantpos.model.ITicketItem;
import com.floreantpos.model.Ticket;
import com.floreantpos.ui.ticket.TicketItemRowCreator;
import com.floreantpos.util.NumberUtil;

public class TicketDataSource extends AbstractReportDataSource {

	public TicketDataSource() {
		super(new String[] { "itemName", "basePrice", "itemQty", "itemSubtotal" });
	}

	public TicketDataSource(Ticket ticket) {
		super(new String[] { "itemName", "basePrice", "itemQty", "itemSubtotal" });

		setTicket(ticket);
	}

	private void setTicket(Ticket ticket) {
		ArrayList<ITicketItem> rows = new ArrayList<ITicketItem>();

		LinkedHashMap<String, ITicketItem> tableRows = new LinkedHashMap<String, ITicketItem>();
		TicketItemRowCreator.calculateTicketRows(ticket, tableRows);

		rows.addAll(tableRows.values());
		setRows(rows);
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		ITicketItem item = (ITicketItem) rows.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return item.getNameDisplay();
		case 1:
			return String.valueOf(NumberUtil.formatNumber(item.getUnitPriceDisplay()));
		case 2:
			Integer itemCountDisplay = item.getItemCountDisplay();

			if (itemCountDisplay == null) {
				return null;
			}

			return String.valueOf(itemCountDisplay);

		case 3:
			Double total = item.getTotalAmountWithoutModifiersDisplay() - item.getTaxAmountWithoutModifiersDisplay();
			if (total == null) {
				return "0";
			}

			return String.valueOf(NumberUtil.roundToTwoDigit(total));
		}

		return null;
	}
}
