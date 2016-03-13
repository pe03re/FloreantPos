package com.floreantpos.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.JButton;
import javax.swing.JLabel;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.io.FileUtils;
import org.jdesktop.swingx.JXDatePicker;
import org.jdesktop.swingx.calendar.DateUtils;

import com.floreantpos.config.AppConfig;
import com.floreantpos.report.Report;
import com.floreantpos.ui.util.UiUtil;

public class ReportGenerationView extends com.floreantpos.swing.TransparentPanel {
	private static ReportGenerationView instance;
	SimpleDateFormat yearFolderFormat = new SimpleDateFormat("yyyy");
	SimpleDateFormat monthFolderFormat = new SimpleDateFormat("MMM");
	SimpleDateFormat dateFolderFormat = new SimpleDateFormat("dd_MM_yyyy");
	SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy_MMM_dd_HH_mm_ss");
	JXDatePicker dStartDate;
	JXDatePicker dEndDate;
	JXDatePicker cStartDate;
	JXDatePicker cEndDate;

	public synchronized static ReportGenerationView getInstance() {
		if (instance == null) {
			instance = new ReportGenerationView();
		}
		return instance;
	}

	private ReportGenerationView() {
		this.setLayout(new MigLayout("", "[][grow]", "[][][][][][grow]"));

		dStartDate = UiUtil.getCurrentMonthStart();
		dEndDate = UiUtil.getCurrentMonthEnd();

		JButton btn1 = new JButton("Generate Daily Reports");
		btn1.setSize(50, 25);

		btn1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printAllReports(dStartDate.getDate(), dEndDate.getDate(), true);
			}
		});

		JButton btn2 = new JButton("Generate Consolidated Reports");
		btn2.setSize(50, 25);

		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				printAllReports(dStartDate.getDate(), dEndDate.getDate(), false);
			}
		});

		this.add(createLabel("All reports will be generated in PDF and Excel format."), "cell 0 0,alignx trailing");
		this.add(createLabel("Start Date" + ":"), "cell 0 2");
		this.add(dStartDate, "cell 0 2");
		this.add(createLabel("End Date" + ":"), "cell 0 3");
		this.add(dEndDate, "cell 0 3");
		this.add(btn1, "cell 0 4");
		this.add(btn2, "cell 0 4");
		this.add(createLabel("Location: " + AppConfig.getReportPath() + "\\Sales_Reports\\"), "cell 0 6");
	}

	protected JLabel createLabel(String text) {
		JLabel label = new JLabel(text);
		label.setText(text);
		return label;
	}

	private void printAllReports(Date startDate, Date endDate, boolean isDaily) {
		startDate = DateUtils.startOfDay(startDate);
		endDate = DateUtils.endOfDay(endDate);

		List<Report> repList = new ArrayList<Report>();
		File f = new File("plugins/");
		try {
			for (File f1 : f.listFiles()) {
				if (f1.getName().endsWith(".jar")) {
					JarFile jarFile = new JarFile(f1);
					Enumeration e = jarFile.entries();
					URL[] urls = { new URL("jar:file:" + f1.getAbsolutePath() + "!/") };
					URLClassLoader cl = URLClassLoader.newInstance(urls);

					while (e.hasMoreElements()) {
						JarEntry je = (JarEntry) e.nextElement();
						if (je.isDirectory() || !je.getName().endsWith(".class")) {
							continue;
						}
						String className = je.getName().substring(0, je.getName().length() - 6);
						className = className.replace('/', '.');
						Class c;
						c = cl.loadClass(className);
						if (Report.class.isAssignableFrom(c)) {
							repList.add((Report) c.newInstance());
						}
					}
				}
			}
			for (Report r : repList) {
				File reportFolder = new File(AppConfig.getReportPath() + "\\Sales_Reports\\" + yearFolderFormat.format(endDate) + "\\" + monthFolderFormat.format(endDate) + "\\"
						+ dateFolderFormat.format(endDate) + "\\");
				File reportFile = new File(reportFolder + "\\" + r.getName() + "_" + fullDateFormat.format(endDate));
				if (!reportFile.exists()) {
					File parent = reportFile.getParentFile();
					if (parent.exists() == false) {
						FileUtils.forceMkdir(parent);
					}
				}
				if (isDaily && r.isDailyReport()) {
					r.exportReport(startDate, endDate, reportFile.getAbsolutePath());
				} else if (!isDaily && !r.isDailyReport()) {
					r.exportReport(startDate, endDate, reportFile.getAbsolutePath());
				}
			}
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
