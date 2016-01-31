package com.floreantpos.main;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

public class Main {

	private static final String DEVELOPMENT_MODE = "developmentMode";
	private static final String REPORT_MODE = "reportMode";

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Options options = new Options();
		options.addOption(DEVELOPMENT_MODE, true, "State if this is developmentMode");
		options.addOption(REPORT_MODE, true, "State if this is reportMode");
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp("main", options);
		CommandLineParser parser = new BasicParser();
		CommandLine commandLine = parser.parse(options, args);
		String devOptionValue = commandLine.getOptionValue(DEVELOPMENT_MODE);
		String reportOptionValue = commandLine.getOptionValue(REPORT_MODE);

		Application application = Application.getInstance();

		if (devOptionValue != null) {
			application.setDevelopmentMode(Boolean.valueOf(devOptionValue));
			System.out.println("Development Mode = " + devOptionValue);
		}

		if (reportOptionValue != null) {
			application.setReportMode(Boolean.valueOf(reportOptionValue));
			System.out.println("Report Mode = " + reportOptionValue);
		}

		application.start();
	}

}
