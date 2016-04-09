package info.zthings.crawler.classes;

import info.zthings.crawler.classes.linktypes.LinkConnectionTimedOut;
import info.zthings.crawler.classes.linktypes.LinkHttpStatusCode;
import info.zthings.crawler.classes.linktypes.LinkMalformedURL;
import info.zthings.crawler.classes.linktypes.LinkUnsupportedMimeType;
import info.zthings.crawler.classes.linktypes.LinkUnsupportedProtocol;
import info.zthings.crawler.classes.statics.Logger;
import info.zthings.crawler.classes.statics.Memory;

import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
	private URL httploc;
	private URL source;
	private boolean failedOnce;
	private boolean starter;
	
	public Crawler(URL url, URL source) {
		this.httploc = url;
		this.failedOnce = false;
		this.starter = source == null;
		this.source = source;
	}

	public void start() {				
		Logger.out.println("Started crawler-object with httploc: " + this.httploc);
		Logger.out.println();
		
		try {
			Document d;
			try {
				d = Jsoup.connect(this.httploc.toString()).get();
			} catch (UnknownHostException e) {
				throw new HttpStatusException("Host not reconized", 404, this.httploc.toString());
			}
			
			Elements pageLinks = d.select("a[href]");
			Memory.addLinks(this.httploc, pageLinks);
			
			//Add new crawlers to the waiting queue
			int done = 0;
			int skipped = 0;
			for (Element el : pageLinks) {
				String cLink = el.attr("abs:href");
				URL cURL;
				try {
					cURL = new URL(cLink);
				} catch (MalformedURLException e) {
					Memory.addSpecialLink(new LinkMalformedURL(this.httploc, cLink));
					break; //next element
				}
				
				if (!Memory.isURLCrawled(cURL)) {
					Memory.addToQueue(new Crawler(cURL, this.httploc));
					done++;
				} else {
					skipped++;
				}
			}
			Logger.out.println();
			
			if (skipped > 0) Logger.out.println("Skipped " + skipped + " links cause they're already crawled");
			Logger.out.println("Added " + done + " crawlers to the waiting stack");
			
			Logger.out.println();
			
			Logger.out.println("Crawl of " + this.httploc + " was succesfull!");
			Memory.completedCrawl();
		} catch (UnsupportedMimeTypeException e) {
			if (!starter) {
				Memory.addSpecialLink(new LinkUnsupportedMimeType(this.source, this.httploc, e));
				Memory.completedCrawl();
			}
			else Logger.err("File type of URL " + this.httploc + " is not supported");
		} catch (MalformedURLException e) {
			if (!this.httploc.getProtocol().matches("https?")) { //possible when thrown by Jsoup.connect
				if (!starter) {
					Memory.addSpecialLink(new LinkUnsupportedProtocol(this.source, this.httploc));
					Memory.completedCrawl();
				}
				else Logger.err("Protocol '" + this.httploc.getProtocol() + "' is unsupported");
			} else { //where the heck did this came from then?
				Logger.err.println("Other MalformedURLException: ");
				e.printStackTrace();
				Memory.addFailedLink(new URLStrWrapper(this.httploc.toString(), this.source));
			}
		} catch (IllegalArgumentException e) { 
			if (e.getCause() instanceof MalformedURLException) { //thrown by Jsoup.connnect
				if (!starter) {
					Memory.addSpecialLink(new LinkMalformedURL(this.source, this.httploc.toString()));
					Memory.completedCrawl();
				}
				else Logger.err("Malformed URL: " + this.httploc);
			} else {
				Logger.err.println("Other IllegalArgumentException:");
				e.printStackTrace();
				Memory.addFailedLink(new URLStrWrapper(this.httploc.toString(), this.source));
			}
		} catch (HttpStatusException e) {
			Logger.err.println("Recieved HTTP status code '" + e.getStatusCode() + "' at URL " + this.httploc);
			if (!starter) {
				Memory.addSpecialLink(new LinkHttpStatusCode(this.source, this.httploc, e));
				Memory.completedCrawl();
			} else Logger.err("Can't crawl from " + this.httploc + ", statuscode: " + e.getStatusCode());
		} catch (SSLException e) {
			Logger.err.println("SSL exception while crawling at " + this.httploc);
			if (!starter) {
				if (!this.failedOnce) Memory.registerForRetry(this);
				else {
					Logger.err.println("Crawler for " + this.httploc + " already failed once");
					Memory.addFailedLink(new URLStrWrapper(this.httploc.toString(), this.source));
				}
			} else Logger.err("Can't crawl from " + this.httploc + " as it reports an SSLException");
		} catch (SocketTimeoutException e) {
			if (!failedOnce) {
				Logger.warn("Connection timed out: " + this.httploc + ", added to retry-list");
				Memory.registerForRetry(this);
			} else {
				Logger.err("Connection timed out (again), not crawling " + this.httploc + " (added to timeout list)");
				if (!starter) Memory.addSpecialLink(new LinkConnectionTimedOut(this.source, this.httploc));
				else Logger.err("Can't crawl " + this.httploc + ", connection timed out");
			}
		} catch (Exception e) {
			Logger.err("Unknown exception while crawling at " + this.httploc);
			e.printStackTrace();
			Memory.addFailedLink(new URLStrWrapper(this.httploc.toString(), this.source));
		} finally {
			Logger.out.println();
			Memory.printStatus();
			Logger.out.println();
			Logger.out.println();
		}
	}
	
	public void setFailedOnce(boolean failed) {
		this.failedOnce = failed;
	}
	public void setStarter(boolean starter) {
		this.starter = starter;
	}

	public URL getHttploc() {
		return this.httploc;
	}
}
