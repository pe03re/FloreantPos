package com.floreantpos.model.base;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.floreantpos.model.Company;
import com.floreantpos.model.InventoryItem;
import com.floreantpos.model.InventoryTransaction;
import com.floreantpos.model.InventoryTransactionType;
import com.floreantpos.model.InventoryVendor;
import com.floreantpos.model.InventoryWarehouse;
import com.floreantpos.model.PackSize;

/**
 * This is an object that contains data related to the INVENTORY_TRANSACTION
 * table. Do not modify this class because it will be overwritten if the
 * configuration file related to this class is modified.
 * 
 * @hibernate.class table="INVENTORY_TRANSACTION"
 */

public abstract class BaseInventoryTransaction implements Comparable, Serializable {

	public static String REF = "InventoryTransaction";
	public static String PROP_INVENTORY_ITEM = "inventoryItem";
	public static String PROP_QUANTITY = "quantity";
	public static String PROP_TO_WAREHOUSE = "inventoryWarehouseByToWarehouseId";
	public static String PROP_VENDOR = "inventoryVendor";
	public static String PROP_TRANSACTION_DATE = "transactionDate";
	public static String PROP_FROM_WAREHOUSE = "inventoryWarehouseByFromWarehouseId";
	public static String PROP_ID = "id";
	public static String PROP_TRANSACTION_TYPE = "transactionType";
	public static String PROP_TOTAL_PRICE = "totalPrice";
	public static String PROP_VAT_PAID = "vatPaid";
	public static String PROP_CREDIT_CHECK = "creditCheck";
	public static String PROP_REMARK = "remark";
	public static String PROP_DISCOUNT = "discount";
	public static String PROP_PACK_SIZE = "packSize";

	// constructors
	public BaseInventoryTransaction() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseInventoryTransaction(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	private Integer id;
	private InventoryTransactionType inventoryTransactionType;
	private InventoryWarehouse inventoryWarehouseByFromWarehouseId;
	private InventoryWarehouse inventoryWarehouseByToWarehouseId;
	private InventoryItem inventoryItem;
	private Company company;
	private PackSize packSize;
	private InventoryVendor inventoryVendor;
	private Date transactionDate;
	private double quantity;
	private double totalPrice;
	private double vatPaid;
	private boolean creditCheck;
	private String remark;
	private double discount;
	private Set<InventoryTransaction> inventoryTransactions = new HashSet<InventoryTransaction>(0);

	public BaseInventoryTransaction(InventoryTransaction inventoryTransaction, InventoryItem inventoryItem, Company company, PackSize packSize, InventoryVendor inventoryVendor, Date transactionDate,
			double quantity, double totalPrice, double vatPaid, boolean creditCheck, double discount) {
		this.inventoryTransactionType = inventoryTransactionType;
		this.inventoryItem = inventoryItem;
		this.company = company;
		this.packSize = packSize;
		this.inventoryVendor = inventoryVendor;
		this.transactionDate = transactionDate;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.vatPaid = vatPaid;
		this.creditCheck = creditCheck;
		this.discount = discount;
	}

	public BaseInventoryTransaction(InventoryTransaction inventoryTransaction, InventoryWarehouse inventoryWarehouseByFromWarehouseId, InventoryWarehouse inventoryWarehouseByToWarehouseId,
			InventoryItem inventoryItem, Company company, PackSize packSize, InventoryVendor inventoryVendor, Date transactionDate, double quantity, double totalPrice, double vatPaid,
			boolean creditCheck, String remark, double discount, Set<InventoryTransaction> inventoryTransactions) {
		this.inventoryTransactionType = inventoryTransactionType;
		this.inventoryWarehouseByFromWarehouseId = inventoryWarehouseByFromWarehouseId;
		this.inventoryWarehouseByToWarehouseId = inventoryWarehouseByToWarehouseId;
		this.inventoryItem = inventoryItem;
		this.company = company;
		this.packSize = packSize;
		this.inventoryVendor = inventoryVendor;
		this.transactionDate = transactionDate;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
		this.vatPaid = vatPaid;
		this.creditCheck = creditCheck;
		this.remark = remark;
		this.discount = discount;
		this.inventoryTransactions = inventoryTransactions;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public InventoryWarehouse getInventoryWarehouseByFromWarehouseId() {
		return this.inventoryWarehouseByFromWarehouseId;
	}

	public void setInventoryWarehouseByFromWarehouseId(InventoryWarehouse inventoryWarehouseByFromWarehouseId) {
		this.inventoryWarehouseByFromWarehouseId = inventoryWarehouseByFromWarehouseId;
	}

	public InventoryWarehouse getInventoryWarehouseByToWarehouseId() {
		return this.inventoryWarehouseByToWarehouseId;
	}

	public void setInventoryWarehouseByToWarehouseId(InventoryWarehouse inventoryWarehouseByToWarehouseId) {
		this.inventoryWarehouseByToWarehouseId = inventoryWarehouseByToWarehouseId;
	}

	public InventoryItem getInventoryItem() {
		return this.inventoryItem;
	}

	public void setInventoryItem(InventoryItem inventoryItem) {
		this.inventoryItem = inventoryItem;
	}

	public Company getCompany() {
		return this.company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	public PackSize getPackSize() {
		return this.packSize;
	}

	public void setPackSize(PackSize packSize) {
		this.packSize = packSize;
	}

	public InventoryVendor getInventoryVendor() {
		return this.inventoryVendor;
	}

	public void setInventoryVendor(InventoryVendor inventoryVendor) {
		this.inventoryVendor = inventoryVendor;
	}

	public Date getTransactionDate() {
		return this.transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public double getQuantity() {
		return this.quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public double getTotalPrice() {
		return this.totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public double getVatPaid() {
		return this.vatPaid;
	}

	public void setVatPaid(double vatPaid) {
		this.vatPaid = vatPaid;
	}

	public boolean isCreditCheck() {
		return this.creditCheck;
	}

	public void setCreditCheck(boolean creditCheck) {
		this.creditCheck = creditCheck;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public double getDiscount() {
		return this.discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public Set<InventoryTransaction> getInventoryTransactions() {
		return this.inventoryTransactions;
	}

	public void setInventoryTransactions(Set<InventoryTransaction> inventoryTransactions) {
		this.inventoryTransactions = inventoryTransactions;
	}

	public InventoryTransactionType getInventoryTransactionType() {
		return inventoryTransactionType;
	}

	public void setInventoryTransactionType(InventoryTransactionType inventoryTransactionType) {
		this.inventoryTransactionType = inventoryTransactionType;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.floreantpos.model.InventoryTransaction))
			return false;
		else {
			com.floreantpos.model.InventoryTransaction inventoryTransaction = (com.floreantpos.model.InventoryTransaction) obj;
			if (null == this.getId() || null == inventoryTransaction.getId())
				return false;
			else
				return (this.getId().equals(inventoryTransaction.getId()));
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