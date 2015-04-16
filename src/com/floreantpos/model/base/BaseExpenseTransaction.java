package com.floreantpos.model.base;

import java.lang.Comparable;
import java.io.Serializable;

/**
 * This is an object that contains data related to the EXPENSE_TRANSACTION
 * table. Do not modify this class because it will be overwritten if the
 * configuration file related to this class is modified.
 * 
 * @hibernate.class table="EXPENSE_TRANSACTION"
 */

public abstract class BaseExpenseTransaction implements Comparable, Serializable {

	public static String REF = "ExpenseTransaction";
	public static String PROP_VENDOR = "vendor";
	public static String PROP_TRANSACTION_DATE = "transactionDate";
	public static String PROP_ID = "id";
	public static String PROP_TRANSACTION_TYPE = "transactionType";
	public static String PROP_AMOUNT = "amount";
	public static String PROP_VAT_PAID = "vatPaid";
	public static String PROP_REMARK = "remark";
	public static String PROP_REFERENCE_NO = "referenceNo";

	// constructors
	public BaseExpenseTransaction() {
		initialize();
	}

	/**
	 * Constructor for primary key
	 */
	public BaseExpenseTransaction(java.lang.Integer id) {
		this.setId(id);
		initialize();
	}

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	// primary key
	private java.lang.Integer id;

	// fields
	protected java.util.Date transactionDate;
	protected java.lang.Double amount;
	protected java.lang.Double vatPaid;

	protected java.lang.String remark;
	protected java.lang.Boolean creditCheck;

	// many to one
	private com.floreantpos.model.ExpenseTransactionType transactionType;
	private com.floreantpos.model.PurchaseOrder referenceNo;
	private com.floreantpos.model.InventoryVendor vendor;


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
	 * Return the value associated with the column: TRANSACTION_DATE
	 */
	public java.util.Date getTransactionDate() {
		return transactionDate;
	}

	/**
	 * Set the value related to the column: TRANSACTION_DATE
	 * 
	 * @param transactionDate
	 *            the TRANSACTION_DATE value
	 */
	public void setTransactionDate(java.util.Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * Return the value associated with the column: AMOUNT
	 */
	public java.lang.Double getAmount() {
		return amount == null ? Double.valueOf(0) : amount;
	}

	/**
	 * Set the value related to the column: AMOUNT
	 * 
	 */
	public void setAmount(java.lang.Double amount) {
		this.amount = amount;
	}

	public java.lang.Double getVatPaid() {
		return vatPaid;
	}

	public void setVatPaid(java.lang.Double vatPaid) {
		this.vatPaid = vatPaid;
	}

	public java.lang.Boolean getCreditCheck() {
		return creditCheck;
	}

	public void setCreditCheck(java.lang.Boolean creditCheck) {
		this.creditCheck = creditCheck;
	}

	/**
	 * Return the value associated with the column: REMARK
	 */
	public java.lang.String getRemark() {
		return remark;
	}

	/**
	 * Set the value related to the column: REMARK
	 * 
	 * @param remark
	 *            the REMARK value
	 */
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}

	/**
	 * Return the value associated with the column: TRANSACTION_TYPE_ID
	 */
	public com.floreantpos.model.ExpenseTransactionType getTransactionType() {
		return transactionType;
	}

	/**
	 * Set the value related to the column: TRANSACTION_TYPE_ID
	 * 
	 * @param transactionType
	 *            the TRANSACTION_TYPE_ID value
	 */
	public void setTransactionType(com.floreantpos.model.ExpenseTransactionType transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * Return the value associated with the column: REFERENCE_ID
	 */
	public com.floreantpos.model.PurchaseOrder getReferenceNo() {
		return referenceNo;
	}

	/**
	 * Set the value related to the column: REFERENCE_ID
	 * 
	 * @param referenceNo
	 *            the REFERENCE_ID value
	 */
	public void setReferenceNo(com.floreantpos.model.PurchaseOrder referenceNo) {
		this.referenceNo = referenceNo;
	}

	
	/**
	 * Return the value associated with the column: VENDOR_ID
	 */
	public com.floreantpos.model.InventoryVendor getVendor() {
		return vendor;
	}

	/**
	 * Set the value related to the column: VENDOR_ID
	 * 
	 * @param vendor
	 *            the VENDOR_ID value
	 */
	public void setVendor(com.floreantpos.model.InventoryVendor vendor) {
		this.vendor = vendor;
	}

	
	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof com.floreantpos.model.ExpenseTransaction))
			return false;
		else {
			com.floreantpos.model.ExpenseTransaction expenseTransaction = (com.floreantpos.model.ExpenseTransaction) obj;
			if (null == this.getId() || null == expenseTransaction.getId())
				return false;
			else
				return (this.getId().equals(expenseTransaction.getId()));
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