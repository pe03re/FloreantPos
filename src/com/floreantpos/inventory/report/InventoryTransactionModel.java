package com.floreantpos.inventory.report;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.model.InOutEnum;
import com.floreantpos.model.InventoryTransaction;
import com.floreantpos.model.dao.InventoryTransactionDAO;

/**
 * @author SOMYA
 * 
 */
public class InventoryTransactionModel extends ListTableModel {
	SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yy HH:mm");

	DecimalFormat decimalFormat = new DecimalFormat("0.00");

	public InventoryTransactionModel() {
		super(new String[] { "transactionDate", "inventoryTransactionType", "inventoryWarehouse", "inventoryItem", "company", "packSize", "inventoryVendor", "quantity", "totalPrice", "vatPaid",
				"creditCheck", "remark", "discount", "unit", "currentUnitPrice", "lastUnitPrice" });
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		InventoryTransaction data = (InventoryTransaction) rows.get(rowIndex);
		InOutEnum inOutEnum = InOutEnum.fromInt(data.getInventoryTransactionType().getInOrOut().intValue());
		InventoryTransaction lastTrans = InventoryTransactionDAO.getInstance().findByICVP(data.getInventoryItem(), data.getCompany(), data.getInventoryVendor(), data.getPackSize(), data.getId());
		switch (columnIndex) {
		case 0:
			return dateFormat2.format(data.getTransactionDate());
		case 1:
			return data.getInventoryTransactionType().getName();
		case 2:
			if (inOutEnum == InOutEnum.IN) {
				return data.getInventoryWarehouseByToWarehouseId().getName();
			} else if (inOutEnum == InOutEnum.OUT || inOutEnum == InOutEnum.ADJUSTMENT || inOutEnum == InOutEnum.WASTAGE || inOutEnum == InOutEnum.CONSUMPTION) {
				return data.getInventoryWarehouseByFromWarehouseId().getName();
			} else if (inOutEnum == InOutEnum.MOVEMENT) {
				return data.getInventoryWarehouseByFromWarehouseId().getName() + " -> " + data.getInventoryWarehouseByToWarehouseId().getName();
			} else {
				return "";
			}
		case 3:
			if (data.getInventoryItem() != null) {
				return data.getInventoryItem().getName();
			} else {
				return "";
			}
		case 4:
			return data.getCompany().getName();
		case 5:
			return Integer.valueOf(data.getPackSize().getSize());
		case 6:
			return data.getInventoryVendor().getName();
		case 7:
			if (data.getInventoryItem() != null) {
				return formatDouble(data.getQuantity());
			} else {
				return "";
			}
		case 8:
			if (inOutEnum == InOutEnum.IN || inOutEnum == InOutEnum.OUT) {
				return decimalFormat.format(data.getTotalPrice());
			} else {
				return "-";
			}
		case 9:
			if (inOutEnum == InOutEnum.IN || inOutEnum == InOutEnum.OUT) {
				return decimalFormat.format(data.getVatPaid().getRate());
			} else {
				return "-";
			}
		case 10:
			if (inOutEnum == InOutEnum.IN || inOutEnum == InOutEnum.OUT) {
				if (data.isCreditCheck()) {
					return "T";
				} else {
					return "F";
				}
			} else {
				return "-";
			}
		case 11:
			return data.getRemark();
		case 12:
			return decimalFormat.format(data.getDiscount());
		case 13:
			if (data.getInventoryItem() != null && data.getInventoryItem().getPackagingUnit() != null) {
				if (inOutEnum == InOutEnum.IN || inOutEnum == InOutEnum.OUT || inOutEnum == InOutEnum.MOVEMENT) {
					return data.getInventoryItem().getPackagingUnit().getName();
				} else {
					return data.getInventoryItem().getPackagingUnit().getRecepieUnitName();
				}
			} else {
				return "";
			}
		case 14:
			if (data.getQuantity() > 0) {
				return formatDouble(data.getTotalPrice() / data.getQuantity());
			} else {
				return "";
			}
		case 15:
			if (lastTrans != null && lastTrans.getQuantity() > 0) {
				return formatDouble(lastTrans.getTotalPrice() / lastTrans.getQuantity());
			} else {
				return "";
			}
		}
		return null;
	}
}
