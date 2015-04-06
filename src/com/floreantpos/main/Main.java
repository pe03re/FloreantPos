package com.floreantpos.main;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;


public class Main {

	private static final String DEVELOPMENT_MODE = "developmentMode";

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Options options = new Options();
		options.addOption(DEVELOPMENT_MODE, true, "State if this is developmentMode");
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp( "main", options );
		CommandLineParser parser = new BasicParser();
		CommandLine commandLine = parser.parse(options, args);
		String optionValue = commandLine.getOptionValue(DEVELOPMENT_MODE);
		
		Application application = Application.getInstance();
		
		if(optionValue != null) {
			application.setDevelopmentMode(Boolean.valueOf(optionValue));
			System.out.println("Development Mode = " + optionValue);
		}
		
		application.start();
	}

}
