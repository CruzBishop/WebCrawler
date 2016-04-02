package info.zthings.crawler.classes;

import info.zthings.crawler.common.ConsoleUI;
import info.zthings.crawler.common.Memory;
import info.zthings.crawler.common.Ref;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.util.Calendar;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class Crawler {
	//TODO: dual-stream with ConsoleUI & logfile?
	private PrintStream log;
	private final String BASE_LOCATION = "crawls/date-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "-" + Calendar.getInstance().get(Calendar.MONTH) + "-" + Calendar.getInstance().get(Calendar.YEAR) + "/time-" + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "-" + Calendar.getInstance().get(Calendar.MINUTE) + "-" + Calendar.getInstance().get(Calendar.SECOND) + "/";
	private String loc;
	private String name;
	private CrawlBuffer buff = new CrawlBuffer();
	
	public Crawler(String httploc) {
		this.loc = httploc;
		this.name = "start.log";
	}
	public Crawler(String httploc, String name) {
		this.loc = httploc;
		this.name = name;
	}

	public void start() {
		try {
			new File(BASE_LOCATION).mkdirs();
			File f = new File(BASE_LOCATION + this.name);
			f.createNewFile();
			log = new PrintStream(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ConsoleUI.out("Started crawler-object with httploc: " + this.loc + " and name: " + this.name + " (saving log at " + BASE_LOCATION + this.name + ")");
		
		log.println("CRAWLING AT "+this.loc);
		log.println(Ref.SEP.substring(0, 12+this.loc.length()));
		log.println("Requesting document object...");
		
		try {
			Document d = Jsoup.connect(this.loc).get();
			this.buff.add(this.loc, d.select("a[href]"));
			this.buff.log(log);
			Memory.addLink(this.loc);
			
			//Start a crawler at all the found links
			for (String s : this.buff.linkList()) {
				if (!Memory.isCrawled(s)) {
					ConsoleUI.out("Crawling " + s + " now, as it isn't in memory");
					cleanup();
					new Crawler(s, "branch_#" + Memory.getNextBranchIndex() + "__" + s.replace("http://", "").replace("www.", "").replace(".", "_").replace("/", "-")+".log").start(); 
				} else {
					ConsoleUI.out("Not crawling " + s + " as it is already done in this session (branchindex: " + Memory.getBranchIndex(s) + ")");
					
				}
			}
		} catch (IllegalArgumentException e) { 
			if (e.getCause() instanceof MalformedURLException) log.println("URL is invalid for Java: " + this.loc);
			e.printStackTrace(log);
		} catch (HttpStatusException e) {
			if (e.getStatusCode() == 404) {
				ConsoleUI.warn("Link (" + this.loc + ") is broken!");
				log.println("404: DOCUMENT DOESN'T EXIST");
				Memory.addLink(this.loc);
			}
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
