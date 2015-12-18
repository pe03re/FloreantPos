package com.floreantpos.ui.views.order.actions;

import java.util.List;

import com.floreantpos.model.MenuGroup;
import com.floreantpos.model.MenuItem;

public interface ItemSelectionListener {
	void itemSelected(MenuItem menuItem);

	void itemSelectionFinished(List<MenuGroup> parent);
}
