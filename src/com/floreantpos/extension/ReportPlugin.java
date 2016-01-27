package com.floreantpos.extension;

import javax.swing.AbstractAction;

import net.xeoh.plugins.base.Plugin;

public interface ReportPlugin extends Plugin {

	AbstractAction[] getReportActions();

	String getBaseMenuName();
	
}
