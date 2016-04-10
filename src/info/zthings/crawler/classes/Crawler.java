package info.zthings.crawler.classes;

import info.zthings.crawler.common.ConsoleUI;
import info.zthings.crawler.common.Memory;
import info.zthings.crawler.common.Ref;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
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
		return s.replace("http://", "").replace("https://", "").replace("www.", "").replace(".", "_").replace("/", "-").replace("?", "_que_").replace("&", "_amp_");
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
			ConsoleUI.err("IOException while setting up streams");
			e.printStackTrace();
		}
				
		ConsoleUI.out("Started crawler-object with httploc: " + this.httploc + " and name: " + this.name + " (saving log at " + BASE_LOCATION + this.name + ")");
		log.println("CRAWLING AT "+this.httploc);
		log.println(Ref.sep(12+this.httploc.length()));
		log.println("Requesting document object...");
		
		try {
			Document d = Jsoup.connect(this.httploc).get();
			this.linkBuffer.add(this.httploc, d.select("a[href]"));
			this.linkBuffer.log(log);
			this.linkBuffer.csv(csv);
			Memory.registerLinks(this.linkBuffer);

			//Add new crawlers to the waiting stack
			for (String s : this.linkBuffer.getLinkList()) {
				if (!Memory.isCrawled(s)) {
					ConsoleUI.out("Adding crawler for " + s + " to the waiting stack, as it isn't in memory");
					cleanup();
					Memory.addToStack(new Crawler(s, "branch_#" + Memory.getNextBranchIndex() + "__" + fileSafe(s), this.BASE_LOCATION));
				} else {
					ConsoleUI.out("Not crawling " + s + " as it is already done in this session (branchindex: " + Memory.getBranchIndex(s) + ")");
				}
			}
		} catch (UnsupportedMimeTypeException e) {
			log.println("File type not supported (" + this.httploc + ")");
			return;
		} catch (IllegalArgumentException e) { 
			if (e.getCause() instanceof MalformedURLException) {
				ConsoleUI.err("Invalid URL (" + this.httploc + ")");
				log.println("URL is invalid: " + this.httploc);
			}
		} catch (MalformedURLException e) {
			if (this.httploc.matches("mailto:.*")) {
				//STUB
			} else if (this.httploc.matches("callto:.*")) {
				//STUB
			} else {
				log.println("Unsupported protocol: " + this.httploc);
				ConsoleUI.err("Detected unknown protocol: " + this.httploc);
				System.err.println("Unkown protocol: " + this.httploc);
				e.printStackTrace();
			}
		} catch (HttpStatusException e) {
			if (e.getStatusCode() == 404) {
				ConsoleUI.warn("Link (" + this.httploc + ") is broken!");
				log.println("404: DOCUMENT DOESN'T EXIST");
			} else if (e.getStatusCode() == 403) {
				ConsoleUI.warn("Acces denied (403): " + this.httploc);
				log.println("403: ACCES DENIED");
			} else if (e.getStatusCode() == 503) { 
				ConsoleUI.warn("Service unavaillable (503): " + this.httploc);
				log.println("503: SERVICE UNAVAILABLE");
			} else {
				ConsoleUI.warn("Other HttpStatus error (" + e.getStatusCode() + "): " + this.httploc);
				log.println("HTTPSTATUS ERROR: " + e.getStatusCode());
			}
		} catch (SocketTimeoutException e) {
			log.println("Connection timed out");
			ConsoleUI.out("Connection timed out: " + this.httploc);
		} catch (Exception e) {
			log.println("Other exception occured");
			System.err.println("OTHER EXCEPTION WHILE CRAWLING " + this.httploc);
			e.printStackTrace();
		} finally {
			cleanup();
			Memory.markLinkCrawled(this.httploc); //no doubles
			Memory.nextOnStack(); //always go to the next one
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
