package info.zthings.crawler.classes;

import info.zthings.crawler.common.Ref;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.Calendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Crawler {
	//TODO: dual-stream with ConsoleUI & logfile?
	private PrintStream log;
	private final String BASE_LOCATION = "crawls/date-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "-" + Calendar.getInstance().get(Calendar.MONTH) + "-" + Calendar.getInstance().get(Calendar.YEAR) + "/time-" + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "-" + Calendar.getInstance().get(Calendar.MINUTE) + "-" + Calendar.getInstance().get(Calendar.SECOND) + "/";	private String loc;
	private String n;
	
	public Crawler(String httploc) {
		this.loc = httploc;
		this.n = "start.log";
	}
	public Crawler(String httploc, String name) {
		this.loc = httploc;
		this.n = name;
	}

	public void start() {
		try {
			new File(BASE_LOCATION).mkdirs();
			File f = new File(BASE_LOCATION + this.n);
			f.createNewFile();
			log = new PrintStream(f);
		} catch (IOException e) {
			e.printStackTrace();
		}

		log.println("CRAWLING AT "+loc);
		log.println(Ref.SEP.substring(0, 12+loc.length()));
		log.println("Requesting document object...");
		
		try {
			Document d = Jsoup.connect(loc).get();
			Elements links = d.select("a[href]");
			
			for (Element e : links) {
				log.println("Found link: " + e.attr("href"));
			}
		} catch (MalformedURLException e) { 
			log.println("URL is invalid for Java: " + loc);
			e.printStackTrace(log);
		} catch (IOException e) {
			log.println("Other IO exception occured");
			e.printStackTrace();
		} finally {
			cleanup();
		}
	}

	private void cleanup() {
		log.close();
	}
}
