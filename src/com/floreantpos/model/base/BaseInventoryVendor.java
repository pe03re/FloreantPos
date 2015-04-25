package com.floreantpos.model.base;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.floreantpos.model.ExpenseTransaction;
import com.floreantpos.model.InventoryTransaction;
import com.floreantpos.model.ItemCompVendPack;
import com.floreantpos.model.Person;

/**
 * This is an object that contains data related to the INVENTORY_VENDOR table.
 * Do not modify this class because it will be overwritten if the configuration
 * file related to this class is modified.
 * 
 * @hibernate.class table="INVENTORY_VENDOR"
 */

public abstract class BaseInventoryVendor implements Comparable, Serializable {

	public static String REF = "InventoryVendor";
	public static String PROP_NAME = "name";
	public static String PROP_PHONE = "phone";
	public static String PROP_EMAIL = "email";
	public static String PROP_ADDRESS = "address";
	public static String PROP_VISIBLE = "visible";
	public static String PROP_ID = "id";
	public static String PROP_EXP_TYPE_VENDOR = "expenseTypeVendor";
	public static String PROP_PERSON_P2 = "personByP2Id";
	public static String PROP_PERSON_P3 = "personByP3Id";
	public static String PROP_PERSON_P4 = "personByP4Id";
	public static String PROP_PERSON_P5 = "personByP5Id";
	public static String PROP_PERSON_P6 = "personByP6Id";
	public static String PROP_PERSON_P7 = "personByP7Id";

	// constructors
	public BaseInventoryVendor() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseInventoryVendor(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	/**
	 * Constructor for required fields
	 */
	public BaseInventoryVendor(java.lang.Integer id, java.lang.String name) {

		this.setId(id);
		this.setName(name);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private Integer id;
	private Person personByP2Id;
	private Person personByP5Id;
	private Person personByP4Id;
	private Person personByP6Id;
	private Person personByP3Id;
	private Person personByP7Id;
	private String name;
	private String phone;
	private String email;
	private String address;
	private Boolean visible;
	private boolean expenseTypeVendor;
	private Set<ExpenseTransaction> expenseTransactions = new HashSet<ExpenseTransaction>(0);
	private Set<InventoryTransaction> inventoryTransactions = new HashSet<InventoryTransaction>(0);
	private Set<ItemCompVendPack> itemCompVendPacks = new HashSet<ItemCompVendPack>(0);

	public BaseInventoryVendor(String name, boolean expenseTypeVendor) {
		this.name = name;
		this.expenseTypeVendor = expenseTypeVendor;
	}

	public BaseInventoryVendor(Person personByP2Id, Person personByP5Id, Person personByP4Id, Person personByP6Id, Person personByP3Id, Person personByP7Id, String name, String phone, String email,
			String address, Boolean visible, boolean expenseTypeVendor, Set<ExpenseTransaction> expenseTransactions, Set<InventoryTransaction> inventoryTransactions,
			Set<ItemCompVendPack> itemCompVendPacks) {
		this.personByP2Id = personByP2Id;
		this.personByP5Id = personByP5Id;
		this.personByP4Id = personByP4Id;
		this.personByP6Id = personByP6Id;
		this.personByP3Id = personByP3Id;
		this.personByP7Id = personByP7Id;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.address = address;
		this.visible = visible;
		this.expenseTypeVendor = expenseTypeVendor;
		this.expenseTransactions = expenseTransactions;
		this.inventoryTransactions = inventoryTransactions;
		this.itemCompVendPacks = itemCompVendPacks;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Person getPersonByP2Id() {
		return this.personByP2Id;
	}

	public void setPersonByP2Id(Person personByP2Id) {
		this.personByP2Id = personByP2Id;
	}

	public Person getPersonByP5Id() {
		return this.personByP5Id;
	}

	public void setPersonByP5Id(Person personByP5Id) {
		this.personByP5Id = personByP5Id;
	}

	public Person getPersonByP4Id() {
		return this.personByP4Id;
	}

	public void setPersonByP4Id(Person personByP4Id) {
		this.personByP4Id = personByP4Id;
	}

	public Person getPersonByP6Id() {
		return this.personByP6Id;
	}

	public void setPersonByP6Id(Person personByP6Id) {
		this.personByP6Id = personByP6Id;
	}

	public Person getPersonByP3Id() {
		return this.personByP3Id;
	}

	public void setPersonByP3Id(Person personByP3Id) {
		this.personByP3Id = personByP3Id;
	}

	public Person getPersonByP7Id() {
		return this.personByP7Id;
	}

	public void setPersonByP7Id(Person personByP7Id) {
		this.personByP7Id = personByP7Id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Boolean getVisible() {
		return this.visible;
	}

	public void setVisible(Boolean visible) {
		this.visible = visible;
	}

	public boolean isExpenseTypeVendor() {
		return this.expenseTypeVendor;
	}

	public void setExpenseTypeVendor(boolean expenseTypeVendor) {
		this.expenseTypeVendor = expenseTypeVendor;
	}

	public Set<ExpenseTransaction> getExpenseTransactions() {
		return this.expenseTransactions;
	}

	public void setExpenseTransactions(Set<ExpenseTransaction> expenseTransactions) {
		this.expenseTransactions = expenseTransactions;
	}

	public Set<InventoryTransaction> getInventoryTransactions() {
		return this.inventoryTransactions;
	}

	public void setInventoryTransactions(Set<InventoryTransaction> inventoryTransactions) {
		this.inventoryTransactions = inventoryTransactions;
	}

	public Set<ItemCompVendPack> getItemCompVendPacks() {
		return this.itemCompVendPacks;
	}

	public void setItemCompVendPacks(Set<ItemCompVendPack> itemCompVendPacks) {
		this.itemCompVendPacks = itemCompVendPacks;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.floreantpos.model.InventoryVendor))
			return false;
		else {
			com.floreantpos.model.InventoryVendor inventoryVendor = (com.floreantpos.model.InventoryVendor) obj;
			if (null == this.getId() || null == inventoryVendor.getId())
				return false;
			else
				return (this.getId().equals(inventoryVendor.getId()));
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