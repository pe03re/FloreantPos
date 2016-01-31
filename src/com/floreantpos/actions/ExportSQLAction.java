package com.floreantpos.actions;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Action;

import org.apache.commons.io.FileUtils;

import com.floreantpos.IconFactory;
import com.floreantpos.config.AppConfig;
import com.floreantpos.main.Application;
import com.floreantpos.ui.dialog.POSMessageDialog;

public class ExportSQLAction extends PosAction {

	private static final String exportSQLName = "Export SQL";
	SimpleDateFormat format = new SimpleDateFormat("yyyy_MMM_dd_hh_mm_ss");

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
		try {
			Date today = new Date();
			String dateFolderName = Application.dateFolderFormat.format(today);
			String backupFileName = "pos_" + format.format(today) + ".sql";
			File f = new File(AppConfig.getXamppPath() + "\\mysqldump.exe");

			ProcessBuilder pb = new ProcessBuilder(f.getCanonicalPath(), "-u", "pos", "-ppos123", "pos");
			File sqlFile = new File(AppConfig.getExportPath() + "\\posBackup\\" + Application.yearFolderFormat.format(today) + "\\" + Application.monthFolderFormat.format(today) + "\\"
					+ dateFolderName + "\\" + backupFileName);
			if (!sqlFile.exists()) {
				File parent = sqlFile.getParentFile();
				if (parent.exists() == false) {
					FileUtils.forceMkdir(parent);
				}
			}
			pb.redirectOutput(Redirect.to(sqlFile));
			Process p = pb.start();

			int exit = p.waitFor();
			System.out.println("Exited status = " + exit);
		} catch (Exception e) {
			POSMessageDialog.showError(e.getMessage(), e);
		}
	}
}
