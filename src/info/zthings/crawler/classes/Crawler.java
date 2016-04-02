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
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;


public class Crawler {
	//TODO: dual-stream with ConsoleUI & logfile?
	private PrintStream log;
	private PrintStream csv;
	private String BASE_LOCATION = "crawls/date-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "-" + (Calendar.getInstance().get(Calendar.MONTH)+1) + "-" + Calendar.getInstance().get(Calendar.YEAR) + "/time-" + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "-" + Calendar.getInstance().get(Calendar.MINUTE) + "-" + Calendar.getInstance().get(Calendar.SECOND) + "/";
	private String httploc;
	private String name;
	private CrawlBuffer linkBuffer = new CrawlBuffer();
	
	public Crawler(String httploc) {
		this.httploc = httploc;
		this.name = "branch_#0__" + fileSafe(httploc);
	}
	public Crawler(String httploc, String name, String base_location) {
		this.httploc = httploc;
		this.name = fileSafe(name);
		this.BASE_LOCATION = base_location;
	}

	private String fileSafe(String s) {
		if (s.indexOf("?") > -1) {
			s = s.substring(0, s.indexOf("?"));
		}
		s = s.replace("http://", "").replace("https://", "").replace("www.", "").replace(".", "_").replace("/", "-");
		return s;
	}

	public void start() {
		//Setup streams
		try {
			File f = new File(BASE_LOCATION + "logs/" + this.name + ".log");
			f.getParentFile().mkdirs();
			f.createNewFile();
			log = new PrintStream(f);
			
			f = new File(BASE_LOCATION + "csv/" + fileSafe(this.httploc) + ".csv");
			f.getParentFile().mkdirs();
			f.createNewFile();
			csv = new PrintStream(f);
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		ConsoleUI.out("Started crawler-object with httploc: " + this.httploc + " and name: " + this.name + " (saving log at " + BASE_LOCATION + this.name + ")");
		log.println("CRAWLING AT "+this.httploc);
		log.println(Ref.sep(12+this.httploc.length()));
		log.println("Requesting document object...");
		
		try {
			Document d;
			try {
				d = Jsoup.connect(this.httploc).get();
			} catch (UnsupportedMimeTypeException e) {
				log.println("File type not supported (" + this.httploc + ")");
				Memory.markLinkCrawled(this.httploc);
				//Not needed cause the parent start() has linked it already
				//Memory.addLink(this.source, this.httploc);
				return;
			}
			this.linkBuffer.add(this.httploc, d.select("a[href]"));
			this.linkBuffer.log(log);
			this.linkBuffer.csv(csv);
			Memory.markLinkCrawled(this.httploc);
			Memory.registerLinks(this.linkBuffer);

			//Add crawlers to the waiting stack
			for (String s : this.linkBuffer.getLinkList()) {
				if (!Memory.isCrawled(s)) {
					ConsoleUI.out("Adding crawler for " + s + " to the waiting stack, as it isn't in memory");
					cleanup();
					Memory.addToStack(new Crawler(s, "branch_#" + Memory.getNextBranchIndex() + "__" + fileSafe(s), this.BASE_LOCATION));
				} else {
					ConsoleUI.out("Not crawling " + s + " as it is already done in this session (branchindex: " + Memory.getBranchIndex(s) + ")");
				}
			}
			Memory.nextOnStack();
		} catch (IllegalArgumentException e) { 
			if (e.getCause() instanceof MalformedURLException) {
				ConsoleUI.err("Invalid URL in memory (" + Memory.getCrawlLocation() + ")");
				log.println("URL is invalid for Java: " + this.httploc);
			}
			e.printStackTrace(log);
		} catch (HttpStatusException e) {
			if (e.getStatusCode() == 404) {
				ConsoleUI.warn("Link (" + this.httploc + ") is broken!");
				log.println("404: DOCUMENT DOESN'T EXIST");
				Memory.markLinkCrawled(this.httploc);
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
	
	@Override
	public String toString() {
		return "http:" + this.httploc + ";name:" + this.name;
	}
	
	public String getBaseLoc() {
		return BASE_LOCATION;
	}
}
