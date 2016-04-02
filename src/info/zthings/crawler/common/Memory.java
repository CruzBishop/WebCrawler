package info.zthings.crawler.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class Memory {
	private static HashMap<String, Object> m = new HashMap<String, Object>();
	
	public static void init() {
		m.put("loc", "");
		m.put("links", new ArrayList<String>());
	}
	
	public static String getCrawlLocation() {
		return (String) m.get("loc");
	}

	public static void setCrawlLocation(String location) {
		m.put("loc", location);
	}
	
	@SuppressWarnings("unchecked")
	public static void addLink(String loc) {
		ArrayList<String> buffer = (ArrayList<String>) m.get("links");
		buffer.add(loc);
		m.put("links", buffer);
	}
	
	public static String squash() {
		String str = "";
		
		for (Entry<String, Object> en : m.entrySet()) {
			if (en.getValue() instanceof Iterable) {
				String buff = "";
				byte off = 0;
				for (Object o : (Iterable<?>)en.getValue()) {
					off = 2;
					buff += o.toString() + ", "; 
				}
				
				str += en.getKey() + "={" + buff.substring(0, buff.length()-off) + "}, "; //delete last ', '
			} else str += en.getKey() + "=" + en.getValue() + ", ";
		}
		
		return str.substring(0, str.length()-2); //delete last ', '
	}

	@SuppressWarnings("unchecked")
	public static boolean isCrawled(String s) {
		return ((ArrayList<String>)m.get("links")).contains(s);
	}
	
	@SuppressWarnings("unchecked")
	public static int getNextBranchIndex() {
		return ((ArrayList<String>)m.get("links")).size();
	}
	@SuppressWarnings("unchecked")
	public static int getBranchIndex(String s) {
		return ((ArrayList<String>)m.get("links")).indexOf(s);
	}
	
	
}
