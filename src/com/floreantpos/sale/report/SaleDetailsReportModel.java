package com.floreantpos.sale.report;

import java.text.DecimalFormat;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.floreantpos.main.Application;

public class SaleDetailsReportModel extends AbstractTableModel {
	private static DecimalFormat formatter = new DecimalFormat("#,##0.00");
	private String currencySymbol;

	private String[] columnNames = { "Date", "Name", "Qty", "Tax", "Price", "Material Cost", "Margin", "Discount" };
	private List<SaleDetailReportItem> items;
	private double grandTotal;

	public SaleDetailsReportModel() {
		super();
		currencySymbol = Application.getCurrencySymbol();
	}

	public int getRowCount() {
		if (items == null) {
			return 0;
		}

		return items.size();
	}

	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public String getColumnName(int column) {
		return columnNames[column];
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		SaleDetailReportItem item = items.get(rowIndex);

		switch (columnIndex) {
		case 0:
			return item.getDate();
		case 1:
			return item.getName();
		case 2:
			return String.valueOf(item.getQuantity());
		case 3:
			return String.valueOf(item.getTaxRate()) + "%";
		case 4:
			return currencySymbol + " " + formatter.format(item.getPrice());
		case 5:
			return currencySymbol + " " + formatter.format(item.getBuyPrice());
		case 6:
			return currencySymbol + " " + formatter.format(item.getProfit());
		case 7:
			return currencySymbol + " " + formatter.format(item.getDiscount());
		}

		return null;
	}

	public List<SaleDetailReportItem> getItems() {
		return items;
	}

	public void setItems(List<SaleDetailReportItem> items) {
		this.items = items;
	}

	public double getGrandTotal() {
		return grandTotal;
	}

	public String getGrandTotalAsString() {
		return currencySymbol + " " + formatter.format(grandTotal);
	}

	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public void calculateGrandTotal() {
		grandTotal = 0;
		if (items == null) {
			return;
		}

		for (SaleDetailReportItem item : items) {
			grandTotal += item.getPrice();
		}
	}
}
