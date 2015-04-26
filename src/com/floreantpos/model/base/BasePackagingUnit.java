package com.floreantpos.model.base;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.floreantpos.model.InventoryItem;

/**
 * This is an object that contains data related to the PACKAGING_UNIT table. Do
 * not modify this class because it will be overwritten if the configuration
 * file related to this class is modified.
 * 
 * @hibernate.class table="PACKAGING_UNIT"
 */

public abstract class BasePackagingUnit implements Comparable, Serializable {

	public static String REF = "PackagingUnit";
	public static String PROP_NAME = "name";
	public static String PROP_FACTOR = "factor";
	public static String PROP_SHORT_NAME = "shortName";
	public static String PROP_ID = "id";
	public static String PROP_DIMENSION = "dimension";
	public static String PROP_BASE_UNIT = "baseUnit";

	// constructors
	public BasePackagingUnit() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BasePackagingUnit(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	private String name;
	private String recepieUnitName;
	private Double factor;
	private Set<InventoryItem> inventoryItems = new HashSet<InventoryItem>(0);

	public BasePackagingUnit(String name, String recepieUnitName, Double factor, Set<InventoryItem> inventoryItems) {
		this.name = name;
		this.recepieUnitName = recepieUnitName;
		this.factor = factor;
		this.inventoryItems = inventoryItems;
	}

	public Set<InventoryItem> getInventoryItems() {
		return this.inventoryItems;
	}

	public void setInventoryItems(Set<InventoryItem> inventoryItems) {
		this.inventoryItems = inventoryItems;
	}

	/**
	 * Return the unique identifier of this class
	 * 
	 * @hibernate.id generator-class="identity" column="ID"
	 */
	public java.lang.Integer getId() {
		return id;
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

	/**
	 * Return the value associated with the column: NAME
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Set the value related to the column: NAME
	 * 
	 * @param name
	 *            the NAME value
	 */
	public void setName(java.lang.String name) {
		this.name = name;
	}

	/**
	 * Return the value associated with the column: RECEPIE_UNIT_NAME
	 */
	public java.lang.String getRecepieUnitName() {
		return recepieUnitName;
	}

	/**
	 * Set the value related to the column: SHORT_NAME
	 * 
	 * @param shortName
	 *            the SHORT_NAME value
	 */
	public void setRecepieUnitName(java.lang.String recepieUnitName) {
		this.recepieUnitName = recepieUnitName;
	}

	/**
	 * Return the value associated with the column: FACTOR
	 */
	public java.lang.Double getFactor() {
		return factor == null ? Double.valueOf(0) : factor;
	}

	/**
	 * Set the value related to the column: FACTOR
	 * 
	 * @param factor
	 *            the FACTOR value
	 */
	public void setFactor(java.lang.Double factor) {
		this.factor = factor;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.floreantpos.model.PackagingUnit))
			return false;
		else {
			com.floreantpos.model.PackagingUnit packagingUnit = (com.floreantpos.model.PackagingUnit) obj;
			if (null == this.getId() || null == packagingUnit.getId())
				return false;
			else
				return (this.getId().equals(packagingUnit.getId()));
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