package com.floreantpos.report;

import java.util.List;

import com.floreantpos.model.TaxTreatment;

public class ReportItem {
	private String id;
	private String name;
	private double price;
	private int quantity;
	private double total;
	private List<TaxTreatment> taxList;

	public List<TaxTreatment> getTaxList() {
		return taxList;
	}

	public void setTaxList(List<TaxTreatment> taxList) {
		this.taxList = taxList;
	}

	public ReportItem() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
