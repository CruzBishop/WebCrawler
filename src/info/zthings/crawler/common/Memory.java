package info.zthings.crawler.common;

import info.zthings.crawler.classes.CrawlBuffer;
import info.zthings.crawler.classes.Crawler;
import info.zthings.crawler.commands.CommandHandler;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

public class Memory {
	private static HashMap<String, Object> m = new HashMap<String, Object>();
	
	public static final Object loc_def_value = "";
	public static final Object crawled_def_value = new ArrayList<String>();
	public static final Object stack_def_value = new ArrayList<Crawler>();
	public static final Object links_def_value = new HashMap<String, ArrayList<String>>();
	
	public static void init() {
		m.put("loc", loc_def_value);
		m.put("crawled", crawled_def_value);
		m.put("stack", stack_def_value);
		m.put("links", links_def_value);
	}
	
	public static HashMap<String, Object> getMap() {
		return m;
	}
	public static void setMemory(HashMap<String, Object> map) {
		m = map;
	}
	
	public static String getCrawlLocation() {
		return (String) m.get("loc");
	}

	public static void setCrawlLocation(String location) {
		m.put("loc", location);
	}
	
	@SuppressWarnings("unchecked")
	public static void addToStack(Crawler crawler) {
		((ArrayList<Crawler>) m.get("stack")).add(crawler);
	}
	@SuppressWarnings("unchecked")
	public static void addToStack(Collection<? extends Crawler> httplocs) {
		((ArrayList<Crawler>) m.get("stack")).addAll(httplocs);
	}
	@SuppressWarnings("unchecked")
	public static void removeFromStack() {
		((ArrayList<Crawler>) m.get("stack")).remove(0);
	}
	@SuppressWarnings("unchecked")
	public static void nextOnStack() {
		ArrayList<Crawler> stack = ((ArrayList<Crawler>) m.get("stack"));
		
		CommandHandler.parseCommand("status", "stack");
		String baseLocation = stack.get(0).getBaseLoc();
		
		stack.remove(0);
		m.put("stack", stack);
		if (stack.size() > 0) stack.get(0).start();
		else {
			ConsoleUI.out("Waiting stack is empty, flushing link data into " + baseLocation + "result.cwr");
			try {
				PrintStream st = new PrintStream(baseLocation + "result.cwr");
				HashMap<String, ArrayList<String>> links = (HashMap<String, ArrayList<String>>) m.get("links");
				
				for (Entry<String, ArrayList<String>> en : links.entrySet()) {
					st.println("#"+en.getKey());
					for (String l : en.getValue()) {
						st.println(l + ",");
					}
				}
				
				st.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public static void markLinkCrawled(String loc) {
		ArrayList<String> buffer = (ArrayList<String>) m.get("crawled");
		buffer.add(loc);
		m.put("crawled", buffer);
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
		return ((ArrayList<String>)m.get("crawled")).contains(s);
	}
	
	@SuppressWarnings("unchecked")
	public static int getNextBranchIndex() {
		return ((ArrayList<String>)m.get("crawled")).size();
	}
	@SuppressWarnings("unchecked")
	public static int getBranchIndex(String s) {
		return ((ArrayList<String>)m.get("crawled")).indexOf(s);
	}

	@SuppressWarnings("unchecked")
	public static void registerLinks(CrawlBuffer buffer) {
		HashMap<String, ArrayList<String>> cMap = ((HashMap<String, ArrayList<String>>)m.get("links"));
		cMap.put(buffer.getSource(), buffer.getLinkList());
	}

	public static Object getKey(String s) {
		return m.get(s);
	}
}
