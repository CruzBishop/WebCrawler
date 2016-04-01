package info.zthings.crawler.common;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Log {
	private static PrintStream f;
	
	public static void init(String fPath) {
		try {
			f = new PrintStream(fPath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void out(Object s) {
		System.out.println(s);
	}
	/** No newline */
	public static void outN(Object s) {
		System.out.print(s);
	}
	
	public static void warn(Object s) {
		System.out.println("WARNING: " + s);
	}
	
	
	public static void outF(Object s) {
		f.println(s);
	}
	public static void warnF(Object s) {
		f.println(Ref.SEP);
		f.println(s);
		f.println(Ref.SEP);
	}
}
