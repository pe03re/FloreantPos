package com.floreantpos.report;

import java.io.FileOutputStream;
import java.util.Date;

import org.jdesktop.swingx.calendar.DateUtils;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.view.JRViewer;

public abstract class Report {
	public static final int REPORT_TYPE_1 = 0;
	public static final int REPORT_TYPE_2 = 1;

	private Date startDate;
	private Date endDate;
	private int reportType = REPORT_TYPE_1;
	protected JRViewer viewer;
	protected JasperPrint print;
	protected boolean dailyReport;
	protected String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Report(String name){
		this.name = name;
	}
	
	public Report(){
		
	}
	
	public boolean isDailyReport() {
		return dailyReport;
	}

	public void setDailyReport(boolean dailyReport) {
		this.dailyReport = dailyReport;
	}

	public void refresh() throws Exception {
		Date date1 = DateUtils.startOfDay(getStartDate());
		Date date2 = DateUtils.endOfDay(getEndDate());
		generateReport(date1, date2);

		viewer = new JRViewer(print);
	}

	public abstract void generateReport(Date startDate, Date endDate) throws Exception;

	public void exportReportPDF(Date startDate, Date endDate, String reportPath) throws Exception {
		generateReport(startDate, endDate);

		JRExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
		exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, new FileOutputStream(reportPath));
		exporter.exportReport();
	}

	public abstract boolean isDateRangeSupported();
	
	public abstract void createModels(Date date1, Date date2);

	public abstract boolean isTypeSupported();

	public JRViewer getViewer() {
		return viewer;
	}

	public Date getEndDate() {
		if (endDate == null) {
			return new Date();
		}
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getReportType() {
		return reportType;
	}

	public void setReportType(int reportType) {
		this.reportType = reportType;
	}

	public Date getStartDate() {
		if (startDate == null) {
			return new Date();
		}
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

}
