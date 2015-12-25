package com.floreantpos.model.base;

import java.io.Serializable;

import com.floreantpos.model.Tax;

/**
 * This is an object that contains data related to the TAX_TREATMENT table. Do
 * not modify this class because it will be overwritten if the configuration
 * file related to this class is modified.
 * 
 * @hibernate.class table="TAX_TREATMENT"
 */

public abstract class BaseTaxTreatment implements Comparable, Serializable {

	public static String REF = "TaxTreatment";
	public static String PROP_ID = "id";
	public static String PROP_NAME = "tax";
	public static String PROP_IS_PRICE_INCLUDED = "isIncludedInPrice";

	// constructors
	public BaseTaxTreatment() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseTaxTreatment(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	protected Tax tax;
	protected boolean includedInPrice;

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="identity" column="ID"
	 */
	public java.lang.Integer getId() {
		return id;
	}

	public Tax getTax() {
		return tax;
	}

	public void setTax(Tax tax) {
		this.tax = tax;
	}

	public boolean isIncludedInPrice() {
		return includedInPrice;
	}

	public void setIncludedInPrice(boolean includedInPrice) {
		this.includedInPrice = includedInPrice;
	}

	/**
	 * Set the unique identifier of this class
	 * 
	 * @param id
	 *            the new ID
	 */
	public void setId(java.lang.Integer id) {
		this.id = id;
		this.hashCode = Integer.MIN_VALUE;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.floreantpos.model.TaxTreatment))
			return false;
		else {
			com.floreantpos.model.TaxTreatment taxT = (com.floreantpos.model.TaxTreatment) obj;
			if (null == this.getId() || null == taxT.getId())
				return false;
			else
				return (this.getId().equals(taxT.getId()));
		}
	}

	public int hashCode() {
		if (Integer.MIN_VALUE == this.hashCode) {
			if (null == this.getId())
				return super.hashCode();
			else {
				String hashStr = this.getClass().getName() + ":" + this.getId().hashCode();
				this.hashCode = hashStr.hashCode();
			}
		}
		return this.hashCode;
	}

	public int compareTo(Object obj) {
		if (obj.hashCode() > hashCode())
			return 1;
		else if (obj.hashCode() < hashCode())
			return -1;
		else
			return 0;
	}

	public String toString() {
		return super.toString();
	}

}