package com.floreantpos.actions;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Action;

import com.floreantpos.IconFactory;
import com.floreantpos.Messages;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class ExportSQLAction extends PosAction {

	private static final String exportSQLName = "Export SQL";
	SimpleDateFormat format = new SimpleDateFormat("dd_MMM_yyyy_hh_mm_ss");

	public ExportSQLAction() {
		super(Messages.getString(exportSQLName)); //$NON-NLS-1$
	}

	public ExportSQLAction(boolean showText, boolean showIcon) {
		if (showText) {
			putValue(Action.NAME, Messages.getString(exportSQLName)); //$NON-NLS-1$
		}
		if (showIcon) {
			putValue(Action.SMALL_ICON, IconFactory.getIcon("export_database_icon")); //$NON-NLS-1$
		}
	}

	@Override
	public void execute() {
		try {
			Runtime runtime = Runtime.getRuntime();
			Date d = new Date();
			String backupFileName = "pos_" + format.format(d) + ".sql";
			runtime.exec("F:/xampp/mysql/bin/mysqldump -u pos -ppos123 pos | gzip > F:/backupSQL/" + backupFileName);
		} catch (Exception e) {
			POSMessageDialog.showError(e.getMessage(), e);
		}
	}

}
