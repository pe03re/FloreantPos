package com.floreantpos.extension;

import javax.swing.AbstractAction;
import javax.swing.JTabbedPane;

import com.floreantpos.model.MenuItem;

import net.xeoh.plugins.base.Plugin;

public interface InventoryPlugin extends Plugin {
//	void showInventoryItemEntryDialog();
//	void showInventoryGroupEntryDialog();
//	void showInventoryLocationEntryDialog();
//	void showInventoryMetacodeEntryDialog();
//	void showInventoryVendorEntryDialog();
//	void showInventoryWarehouseEntryDialog();
	
	AbstractAction[] getActions();
	void addRecepieView(JTabbedPane tabbedPane,MenuItem m);
}
