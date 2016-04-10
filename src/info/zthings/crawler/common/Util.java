package info.zthings.crawler.common;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Util {
	private static PrintStream fs;
	
	private Util() {}
	
	public static void init() {
		try {
			fs = new PrintStream("important_notices.log");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.setErr(fs);
	}
	
	public static String implode(String[] array, String glue) {
		String buffer = "";
		for (int i=0; i<array.length; i++) {
			if (i>0) {
				buffer += glue + array[i];
			} else buffer += array[i];
		}
		return buffer;
	}

	public static String implode(Object[] array, String glue) {
		String[] buffer = new String[array.length];
		
		for (int i=0; i<array.length; i++) {
			buffer[i] = array[i].toString();
		}
		
		return implode(buffer, glue);
	}
	
	public static void fNotice(String s) {
		fs.println(s);
	}
}
