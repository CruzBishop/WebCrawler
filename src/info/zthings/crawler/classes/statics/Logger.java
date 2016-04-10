package info.zthings.crawler.classes.statics;

import info.zthings.crawler.classes.MultiPrintStream;
import info.zthings.crawler.common.Util;

import java.io.File;
import java.io.PrintStream;

public class Logger {
	//===============
	//NOT-STATIC PART
	//===============
	private MultiPrintStream s;

	public Logger(MultiPrintStream stream) {
		this.s = stream;
	}
		
	public void print(Object o) {
		s.print(o);
	}
	public void println(Object o) {
		s.println(o);
	}
	public void println() {
		s.println();
	}
	
	/*private String getDebugInfo() {
		StringBuilder sb = new StringBuilder();
		StackTraceElement el = Thread.currentThread().getStackTrace()[3];
		sb.append(el.getFileName());
		sb.append(" (");
		sb.append(el.getLineNumber());
		sb.append(") ");
		sb.append(Util.formatDate("%h%:%m%:%s%"));
		return "[" + sb.toString() + "] ";
	}*/
	
	//===========
	//STATIC PART
	//===========
	public static Logger out;
	public static Logger err;
	private static Logger warns;
	private static Logger errs;
	
	public static void init() {
		String logLoc = "logs/" + Util.formatDate("%d%-%m%-%y%") + "/" + Util.formatDate("%h%-%m%-%s%") + "/";
		new File(logLoc).mkdirs();
		
		try {
			PrintStream outputStream = new PrintStream(logLoc + "output.log");
			out = new Logger(new MultiPrintStream(System.out, new PrintStream(logLoc + "stdOut.log"), outputStream));
			err = new Logger(new MultiPrintStream(System.err, new PrintStream(logLoc + "stdErr.log"), outputStream));
			warns = new Logger(new MultiPrintStream(System.err, new PrintStream(logLoc + "warnings.log"), outputStream));
			errs = new Logger(new MultiPrintStream(System.err, new PrintStream(logLoc + "errors.log"), outputStream));
		} catch (Exception e) {
			System.err.println("Couldn't setup loggers:");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static void warn(Object s) {
		warns.println("WARNING: " + s);
	}
	public static void err(String s) {
		errs.println("ERROR: " + s);
	}
}
