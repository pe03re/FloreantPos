package com.floreantpos.actions;

import javax.swing.Action;

import com.floreantpos.IconFactory;
import com.floreantpos.main.Application;

public class ExportSQLAction extends PosAction {

	private static final String exportSQLName = "Export SQL";

	public ExportSQLAction() {
		super(exportSQLName); //$NON-NLS-1$
	}

	public ExportSQLAction(boolean showText, boolean showIcon) {
		if (showText) {
			putValue(Action.NAME, exportSQLName); //$NON-NLS-1$
		}
		if (showIcon) {
			putValue(Action.SMALL_ICON, IconFactory.getIcon("export_database_icon.png")); //$NON-NLS-1$
		}
	}

	@Override
	public void execute() {
		Application.getInstance().takeSqlBackup();
	}
}
