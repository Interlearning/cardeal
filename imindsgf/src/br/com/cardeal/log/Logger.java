package br.com.cardeal.log;

import org.apache.log4j.Level;

public class Logger {
	
	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger("log");
	
	public static void setLogLevel() {
		logger.setLevel(Level.OFF);		
	}
	
	public static void info(String msg) {
		System.out.println(msg);
	}
	
	public static void log(String msg) {
		System.out.println(msg);
	}
	
	public static void logException(Exception e) {
		e.printStackTrace();
	}
	
}
