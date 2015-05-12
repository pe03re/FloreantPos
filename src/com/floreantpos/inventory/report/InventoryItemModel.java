package com.floreantpos.inventory.report;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import com.floreantpos.bo.ui.explorer.ListTableModel;
import com.floreantpos.model.InventoryItem;
import com.floreantpos.model.InventoryWarehouseItem;
import com.floreantpos.model.dao.InventoryWarehouseItemDAO;

/**
 * @author SOMYA
 * 
 */
public class InventoryItemModel extends ListTableModel {
	SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd/MM/yy HH:mm");

	DecimalFormat decimalFormat = new DecimalFormat("0.00");

	private static final long serialVersionUID = 3011793405270921001L;
	NumberFormat f = new DecimalFormat("0.##");

	public InventoryItemModel() {
		super(new String[] { "inventoryItem", "cafe", "godown" });
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		InventoryItem row = (InventoryItem) getRowData(rowIndex);
		InventoryWarehouseItemDAO dao = InventoryWarehouseItemDAO.getInstance();
		List<InventoryWarehouseItem> listItems = dao.findByInventoryItem(row);
		Double cafeRcpQty = 0.0d;
		Double godownRcpQty = 0.0d;
		if (listItems != null && listItems.size() == 2) {
			cafeRcpQty = listItems.get(0).getTotalRecepieUnits();
			godownRcpQty = listItems.get(1).getTotalRecepieUnits();
		}
		switch (columnIndex) {
		case 0:
			return row.getName();
		case 1:
			if (row.getPackageReplenishLevel() == -100) {
				return "NA";
			} else {
				if (cafeRcpQty <= row.getPackageReplenishLevel()) {
					return formatDouble(cafeRcpQty) + " " + row.getPackagingUnit().getRecepieUnitName();
				} else {
					return formatDouble(cafeRcpQty) + " " + row.getPackagingUnit().getRecepieUnitName();
				}
			}
		case 2:
			if (row.getPackageReorderLevel() == -100) {
				return "NA";
			} else {
				if (godownRcpQty <= row.getPackageReorderLevel()) {
					return formatDouble(godownRcpQty) + " " + row.getPackagingUnit().getRecepieUnitName();
				} else {
					return formatDouble(godownRcpQty) + " " + row.getPackagingUnit().getRecepieUnitName();
				}
			}

		}
		return row.getName();
	}
}
