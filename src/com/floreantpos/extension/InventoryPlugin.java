package com.floreantpos.extension;

import javax.swing.AbstractAction;
import javax.swing.JTabbedPane;

import net.xeoh.plugins.base.Plugin;

import com.floreantpos.model.MenuItem;

public interface InventoryPlugin extends Plugin {
	// void showInventoryItemEntryDialog();
	// void showInventoryGroupEntryDialog();
	// void showInventoryLocationEntryDialog();
	// void showInventoryMetacodeEntryDialog();
	// void showInventoryVendorEntryDialog();
	// void showInventoryWarehouseEntryDialog();

	AbstractAction[] getInventoryActions();

	AbstractAction[] getExpenseActions();

	void addRecepieView(JTabbedPane tabbedPane, MenuItem m);

	AbstractAction[] getEntityActions();
}
