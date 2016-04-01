package info.zthings.crawler.common;

import java.util.HashMap;
import java.util.Map.Entry;

public class Memory {
	private static HashMap<String, Object> m = new HashMap<String, Object>();
	
	public static String getLocation() {
		return (String) m.get("loc");
	}

	public static void setLocation(String location) {
		m.put("loc", location);
	}

	public static String squash() {
		String str = "";
		
		for (Entry<String, Object> en : m.entrySet()) {
			str += en.getKey() + "=" + en.getValue() + ",";
		}
		
		if (str.length() == 0) return "Memory is empty!";
		return str.substring(0, str.length()-1); //delete last ','
	}
}
