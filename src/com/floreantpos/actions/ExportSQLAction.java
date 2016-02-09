package com.floreantpos.actions;

import java.text.SimpleDateFormat;

import javax.swing.Action;

import com.floreantpos.IconFactory;
import com.floreantpos.main.Application;

public class ExportSQLAction extends PosAction {

	private static final String exportSQLName = "Export SQL";
	SimpleDateFormat format = new SimpleDateFormat("yyyy_MMM_dd_hh_mm_ss");
	SimpleDateFormat yearFolderFormat = new SimpleDateFormat("yyyy");
	SimpleDateFormat monthFolderFormat = new SimpleDateFormat("MMM");
	SimpleDateFormat dateFolderFormat = new SimpleDateFormat("dd_MM_yyyy");

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
