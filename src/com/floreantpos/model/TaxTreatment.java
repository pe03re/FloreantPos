package com.floreantpos.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.floreantpos.model.base.BaseTaxTreatment;

@XmlRootElement(name = "TaxTreatment")
public class TaxTreatment extends BaseTaxTreatment {
	private static final long serialVersionUID = 1L;

	public TaxTreatment() {
		super();
	}

	public TaxTreatment(TaxTreatment t) {
		this.tax = t.tax;
		this.includedInPrice = t.includedInPrice;
	}

	/**
	 * Constructor for primary key
	 */
	public TaxTreatment(java.lang.Integer id) {
		super(id);
	}

	public String getUniqueId() {
		return ("taxTmt" + "_" + getId()).replaceAll("\\s+", "_");
	}

}