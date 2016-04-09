package info.zthings.crawler.classes;

import info.zthings.crawler.classes.linktypes.LinkConnectionTimedOut;
import info.zthings.crawler.classes.linktypes.LinkMalformedURL;
import info.zthings.crawler.classes.linktypes.LinkUnsupportedMimeType;
import info.zthings.crawler.classes.linktypes.LinkUnsupportedProtocol;
import info.zthings.crawler.classes.statics.Logger;
import info.zthings.crawler.classes.statics.Memory;
import info.zthings.crawler.common.Util;

import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
	private URL httploc;
	private URL source;
	private boolean failed;
	private boolean starter;
	private String fileName;
	
	public Crawler(URL url, URL source) {
		this.httploc = url;
		this.failed = false;
		this.starter = source == null;
		this.source = source;
		this.fileName = "crawls/" + Util.encodeURL(url);
	}

	public void start() {				
		Logger.out.println("Started crawler-object with httploc: " + this.httploc + " (saving log at " + Memory.getSaveLocation() + "logs/" + this.fileName + ".log)");

		try {
			Document d = Jsoup.connect(this.httploc.toString()).get();
			Elements pageLinks = d.select("a[href]");
			Memory.addLinks(this.httploc, pageLinks);
			
			//Add new crawlers to the waiting queue
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
					Logger.out.println("Adding crawler for " + cLink + " to the waiting stack, as it isn't in memory");
					Memory.addToQueue(new Crawler(cURL, this.httploc));
				} else {
					Logger.out.println("Not crawling " + cLink + " as it is already done in this session");
				}
			}
			Logger.out.println();
			Logger.out.println("Crawl of " + this.httploc + " was succesfull! (log is at " + Memory.getSaveLocation() + "logs/" + this.fileName + ".log)");
			Logger.out.println();
		} catch (UnsupportedMimeTypeException e) {
			Logger.out.println("File type not supported for URL (" + this.httploc + ")");
			if (!starter) Memory.addSpecialLink(new LinkUnsupportedMimeType(this.source, e));
			else Logger.err("File type of URL " + this.httploc + " is not supported");
		} catch (MalformedURLException e) {
			if (!this.httploc.getProtocol().matches("https?")) { //possible when thrown by Jsoup.connect
				if (!starter) Memory.addSpecialLink(new LinkUnsupportedProtocol(this.source, this.httploc));
				else Logger.err("Protocol '" + this.httploc.getProtocol() + "' is unsupported");
			} else { //where the heck did this came from then?
				Logger.err.println("Other MalformedURLException: ");
				e.printStackTrace();
			}
		} catch (IllegalArgumentException e) { 
			if (e.getCause() instanceof MalformedURLException) { //thrown by Jsoup.connnect
				if (!starter) Memory.addSpecialLink(new LinkMalformedURL(this.source, this.httploc.toString()));
				else Logger.err("Malformed URL: " + this.httploc);
			} else {
				Logger.err.println("Other IllegalArgumentException:");
				e.printStackTrace();
			}
		} catch (HttpStatusException e) {
			Logger.err.println("ERROR: RECIEVED HTTP STATUS CODE: " + e.getStatusCode());
			if (!starter) Memory.addSpecialLink(new LinkHttpStatusCode(this.source, this.httploc, e));
			else Logger.err("Can't crawl from " + this.httploc + ", statuscode: " + e.getStatusCode());
		} catch (SocketTimeoutException e) {
			if (!failed) {
				Logger.warn("Connection timed out: " + this.httploc + ", added to retry-list");
				Memory.registerForRetry(this);
			} else {
				Logger.err("Connection timed out (again), not crawling " + this.httploc + " (added to timeout list)");
				if (!starter) Memory.addSpecialLink(new LinkConnectionTimedOut(this.source, this.httploc));
				else Logger.err("Can't crawl " + this.httploc + ", connection timed out");
			}
		} catch (Exception e) {
			Logger.err("OTHER EXCEPTION WHILE CRAWLING AT " + this.httploc);
			e.printStackTrace();
		}
	}
	
	public void setFailed(boolean failed) {
		this.failed = failed;
	}
	public void setStarter(boolean starter) {
		this.starter = starter;
	}

	public URL getHttploc() {
		return this.httploc;
	}
}
