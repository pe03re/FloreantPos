package com.floreantpos.model.base;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.floreantpos.model.InventoryGroup;
import com.floreantpos.model.InventoryTransaction;
import com.floreantpos.model.InventoryWarehouseItem;
import com.floreantpos.model.ItemCompVendPack;
import com.floreantpos.model.PackagingUnit;
import com.floreantpos.model.RecepieItem;

/**
 * This is an object that contains data related to the INVENTORY_ITEM table. Do
 * not modify this class because it will be overwritten if the configuration
 * file related to this class is modified.
 * 
 * @hibernate.class table="INVENTORY_ITEM"
 */

public abstract class BaseInventoryItem implements Comparable, Serializable {

	public static String REF = "InventoryItem";
	public static String PROP_PACKAGE_BARCODE = "packageBarcode";
	public static String PROP_PACKAGING_UNIT = "packagingUnit";
	public static String PROP_DESCRIPTION = "description";
	public static String PROP_ITEM_GROUP = "inventoryGroup";
	public static String PROP_VISIBLE = "visible";
	public static String PROP_AVERAGE_RUNIT_PRICE = "averageRUnitPrice";
	public static String PROP_SORT_ORDER = "sortOrder";
	public static String PROP_PACKAGE_REPLENISH_LEVEL = "packageReplenishLevel";
	public static String PROP_NAME = "name";
	public static String PROP_LAST_UPDATE_DATE = "lastUpdateDate";
	public static String PROP_CREATE_TIME = "createTime";
	public static String PROP_ID = "id";
	public static String PROP_PACKAGE_REORDER_LEVEL = "packageReorderLevel";

	protected Integer id;
	protected PackagingUnit packagingUnit;
	protected InventoryGroup inventoryGroup;
	protected Date createTime;
	protected Date lastUpdateDate;
	protected String name;
	protected String packageBarcode;
	protected Integer sortOrder;
	protected Integer packageReorderLevel;
	protected Integer packageReplenishLevel;
	protected String description;
	protected double averageRunitPrice;
	protected boolean visible;
	protected Set<RecepieItem> recepieItems = new HashSet<RecepieItem>(0);
	protected Set<ItemCompVendPack> itemCompVendPacks = new HashSet<ItemCompVendPack>(0);
	protected Set<InventoryWarehouseItem> inventoryWarehouseItems = new HashSet<InventoryWarehouseItem>(0);
	protected Set<InventoryTransaction> inventoryTransactions = new HashSet<InventoryTransaction>(0);

	protected void initialize() {
	}

	private int hashCode = Integer.MIN_VALUE;

	public BaseInventoryItem() {
		initialize();
	}

	public BaseInventoryItem(int id) {
		this.id = id;
	}

	public BaseInventoryItem(PackagingUnit packagingUnit, Date createTime, Date lastUpdateDate, String name, int packageReorderLevel, int packageReplenishLevel, double averageRunitPrice,
			boolean visible) {
		this.packagingUnit = packagingUnit;
		this.createTime = createTime;
		this.lastUpdateDate = lastUpdateDate;
		this.name = name;
		this.packageReorderLevel = packageReorderLevel;
		this.packageReplenishLevel = packageReplenishLevel;
		this.averageRunitPrice = averageRunitPrice;
		this.visible = visible;
	}

	public BaseInventoryItem(PackagingUnit packagingUnit, InventoryGroup inventoryGroup, Date createTime, Date lastUpdateDate, String name, String packageBarcode, Integer sortOrder,
			int packageReorderLevel, int packageReplenishLevel, String description, double averageRunitPrice, boolean visible, Set<RecepieItem> recepieItems, Set<RecepieItem> recepieItems_1,
			Set<ItemCompVendPack> itemCompVendPacks, Set<InventoryWarehouseItem> inventoryWarehouseItems, Set<InventoryTransaction> inventoryTransactions) {
		this.packagingUnit = packagingUnit;
		this.inventoryGroup = inventoryGroup;
		this.createTime = createTime;
		this.lastUpdateDate = lastUpdateDate;
		this.name = name;
		this.packageBarcode = packageBarcode;
		this.sortOrder = sortOrder;
		this.packageReorderLevel = packageReorderLevel;
		this.packageReplenishLevel = packageReplenishLevel;
		this.description = description;
		this.averageRunitPrice = averageRunitPrice;
		this.visible = visible;
		this.recepieItems = recepieItems;
		this.itemCompVendPacks = itemCompVendPacks;
		this.inventoryWarehouseItems = inventoryWarehouseItems;
		this.inventoryTransactions = inventoryTransactions;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PackagingUnit getPackagingUnit() {
		return this.packagingUnit;
	}

	public void setPackagingUnit(PackagingUnit packagingUnit) {
		this.packagingUnit = packagingUnit;
	}

	public InventoryGroup getInventoryGroup() {
		return this.inventoryGroup;
	}

	public void setInventoryGroup(InventoryGroup inventoryGroup) {
		this.inventoryGroup = inventoryGroup;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPackageBarcode() {
		return this.packageBarcode;
	}

	public void setPackageBarcode(String packageBarcode) {
		this.packageBarcode = packageBarcode;
	}

	public Integer getSortOrder() {
		return sortOrder == null ? Integer.valueOf(0) : sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public int getPackageReorderLevel() {
		return packageReorderLevel == null ? Integer.valueOf(0) : packageReorderLevel;
	}

	public void setPackageReorderLevel(int packageReorderLevel) {
		this.packageReorderLevel = packageReorderLevel;
	}

	public int getPackageReplenishLevel() {
		return packageReplenishLevel == null ? Integer.valueOf(0) : packageReplenishLevel;
	}

	public void setPackageReplenishLevel(int packageReplenishLevel) {
		this.packageReplenishLevel = packageReplenishLevel;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getAverageRunitPrice() {
		return this.averageRunitPrice;
	}

	public void setAverageRunitPrice(double averageRunitPrice) {
		this.averageRunitPrice = averageRunitPrice;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Set<RecepieItem> getRecepieItems() {
		return this.recepieItems;
	}

	public void setRecepieItems(Set<RecepieItem> recepieItems) {
		this.recepieItems = recepieItems;
	}

	public Set<ItemCompVendPack> getItemCompVendPacks() {
		return this.itemCompVendPacks;
	}

	public void setItemCompVendPacks(Set<ItemCompVendPack> itemCompVendPacks) {
		this.itemCompVendPacks = itemCompVendPacks;
	}

	public Set<InventoryWarehouseItem> getInventoryWarehouseItems() {
		return this.inventoryWarehouseItems;
	}

	public void setInventoryWarehouseItems(Set<InventoryWarehouseItem> inventoryWarehouseItems) {
		this.inventoryWarehouseItems = inventoryWarehouseItems;
	}

	public Set<InventoryTransaction> getInventoryTransactions() {
		return this.inventoryTransactions;
	}

	public void setInventoryTransactions(Set<InventoryTransaction> inventoryTransactions) {
		this.inventoryTransactions = inventoryTransactions;
	}

	public boolean equals(Object obj) {
		if (null == obj)
			return false;
		if (!(obj instanceof BaseInventoryItem))
			return false;
		else {
			BaseInventoryItem inventoryItem = (BaseInventoryItem) obj;
			if (null == this.getId() || null == inventoryItem.getId())
				return false;
			else
				return (this.getId().equals(inventoryItem.getId()));
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