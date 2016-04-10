package info.zthings.crawler.common;

public class Ref {
	public static final String VER = "1.0.0";
	
	public static String sep(int len) {
		String sep = "";
		for (int i=0; i<len; i++) {
			sep += "=";
		}
		return sep;
	}
}
