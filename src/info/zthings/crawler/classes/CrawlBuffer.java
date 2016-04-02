package info.zthings.crawler.classes;

import java.io.PrintStream;
import java.util.ArrayList;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CrawlBuffer {
	private ArrayList<String> strLinks = new ArrayList<String>();
	private String source;
	
	public void add(String source, Elements links) {
		for (Element e : links) {
			strLinks.add(e.attr("abs:href"));
		}
		this.source = source;
	}

	public void log(PrintStream log) {
		log.println("Links of page: " + this.source);
		for (int i=0; i<this.strLinks.size(); i++) {
			log.println("Link #" + i + ": " + this.strLinks.get(i));
		}
	}

	public void csv(PrintStream csv) {
		StringBuilder sb = new StringBuilder();
		for (String s : strLinks) {
			sb.append(s + ",");
		}
		if (sb.length() > 0) csv.println(sb.toString().substring(0, sb.length()-2)); //delete last comma
	}

	public ArrayList<String> linkList() {
		return this.strLinks;
	}
}
