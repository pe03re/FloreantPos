package com.floreantpos.ui.views.order.actions;

import java.util.List;

import com.floreantpos.model.MenuGroup;

public interface GroupSelectionListener {
	void groupSelected(MenuGroup foodGroup);

	void groupsSelected(List<MenuGroup> foodGroups);

}
