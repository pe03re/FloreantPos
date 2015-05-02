package com.floreantpos.model.base;

import java.io.Serializable;
import java.util.Date;

import com.floreantpos.model.ExpenseHead;
import com.floreantpos.model.ExpenseTransactionType;
import com.floreantpos.model.InventoryVendor;
import com.floreantpos.model.Tax;

/**
 * This is an object that contains data related to the EXPENSE_TRANSACTION
 * table. Do not modify this class because it will be overwritten if the
 * configuration file related to this class is modified.
 * 
 * @hibernate.class table="EXPENSE_TRANSACTION"
 */

public abstract class BaseExpenseTransaction implements Comparable, Serializable {

	public static String REF = "ExpenseTransaction";
	public static String PROP_VENDOR = "inventoryVendor";
	public static String PROP_TRANSACTION_DATE = "transactionDate";
	public static String PROP_ID = "id";
	public static String PROP_TRANSACTION_TYPE = "expenseTransactionType";
	public static String PROP_AMOUNT = "amount";
	public static String PROP_VAT_PAID = "vatPaid";
	public static String PROP_REMARK = "remark";
	public static String PROP_EXP_HEAD = "expenseHead";

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

	private Integer id;
	private ExpenseTransactionType expenseTransactionType;
	private InventoryVendor inventoryVendor;
	private ExpenseHead expenseHead;
	private Date transactionDate;
	private Double amount;
	private Tax vatPaid;
	private boolean creditCheck;
	private String remark;

	public BaseExpenseTransaction(boolean creditCheck) {
		this.creditCheck = creditCheck;
	}

	public BaseExpenseTransaction(ExpenseTransactionType expenseTransactionType, InventoryVendor inventoryVendor, ExpenseHead expenseHead, Date transactionDate, Double amount, Tax vatPaid,
			boolean creditCheck, String remark) {
		this.expenseTransactionType = expenseTransactionType;
		this.inventoryVendor = inventoryVendor;
		this.expenseHead = expenseHead;
		this.transactionDate = transactionDate;
		this.amount = amount;
		this.vatPaid = vatPaid;
		this.creditCheck = creditCheck;
		this.remark = remark;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ExpenseTransactionType getExpenseTransactionType() {
		return this.expenseTransactionType;
	}

	public void setExpenseTransactionType(ExpenseTransactionType expenseTransactionType) {
		this.expenseTransactionType = expenseTransactionType;
	}

	public InventoryVendor getInventoryVendor() {
		return this.inventoryVendor;
	}

	public void setInventoryVendor(InventoryVendor inventoryVendor) {
		this.inventoryVendor = inventoryVendor;
	}

	public ExpenseHead getExpenseHead() {
		return this.expenseHead;
	}

	public void setExpenseHead(ExpenseHead expenseHead) {
		this.expenseHead = expenseHead;
	}

	public Date getTransactionDate() {
		return this.transactionDate;
	}

	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public Double getAmount() {
		return this.amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Tax getVatPaid() {
		return this.vatPaid;
	}

	public void setVatPaid(Tax vatPaid) {
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