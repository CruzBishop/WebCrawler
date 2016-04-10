package info.zthings.crawler.classes.statics;

import info.zthings.crawler.classes.Crawler;
import info.zthings.crawler.classes.ENCLOSIONS;
import info.zthings.crawler.classes.URLStrWrapper;
import info.zthings.crawler.classes.interfaces.ISpecialLinkType;
import info.zthings.crawler.commands.CommandHandler;
import info.zthings.crawler.common.Util;

import java.net.URL;
import java.util.ArrayList;

import org.jsoup.select.Elements;

public class Memory {	
	private static URL startLoc = null;
	private static String savDir = "";
	private static int crawls;
	private static ArrayList<URL> crawledURLs = new ArrayList<URL>();
	private static CrawlsRegister linkRegister = new CrawlsRegister();
	private static ArrayList<Crawler> waitingCrawlers = new ArrayList<Crawler>();
	private static ArrayList<Crawler> crawlersForRetry = new ArrayList<Crawler>();
	private static ArrayList<URLStrWrapper> failedCrawlers = new ArrayList<URLStrWrapper>();
	
	//Getters/setters
	public static URL getStartingLocation() {
		return startLoc;
	}
	public static void setStartingLocation(URL loc) {
		startLoc = loc;
	}
	
	public static ArrayList<URL> getCrawledURLs() {
		return crawledURLs;
	}

	public static void setSaveLocation(String loc) {
		savDir = loc;
	}
	public static void setSaveLocation() {
		savDir = "crawls/" + Util.formatDate("%d%-%mo%-%y%/%h%-%mi%-%s%/" + getStartingLocation() + "/");
	}
	public static String getSaveLocation() {
		return savDir;
	}
	
	public static ArrayList<Crawler> getWaitingCrawlers() {
		return waitingCrawlers;
	}
	public static ArrayList<Crawler> getCrawlersForRetry() {
		return crawlersForRetry;
	}
	public static ArrayList<URLStrWrapper> getFailedCrawlers() {
		return failedCrawlers;
	}
	
	private static int getCompletedCrawls() {
		return crawls;
	}
	
	public static void completedCrawl() {
		crawls++;
	}
	
	
	
	//Waiting-stack stuff
	public static void addToQueue(Crawler crawler) {
		waitingCrawlers.add(crawler);
		markURLCrawled(crawler.getHttploc());
	}
	public static void registerForRetry(Crawler crawler) {
		crawler.setFailedOnce(true);
		crawlersForRetry.add(crawler);
	}
	/** Currently not used (does work though) */
	public static boolean removeFromQueue(Crawler crawler) {
		return waitingCrawlers.remove(crawler);
	}
	
	public static void startQueue(Crawler starter) {
		//is already marked as a starter by the missing source param in constructor (CmdCrawl class)
		addToQueue(starter);
		markURLCrawled(starter.getHttploc());
		while (!waitingCrawlers.isEmpty()) { //the crawler will fill the waiting queue
			waitingCrawlers.get(0).start();
			waitingCrawlers.remove(0);
		}
		Logger.out.println("Redoing the failed crawlers...");
		while (!crawlersForRetry.isEmpty()) {
			crawlersForRetry.get(0).start();
			crawlersForRetry.remove(0);
		}
		CommandHandler.parseCommand("save");
		Logger.out.println("Done with crawling! Check the results at " + savDir);
	}
	
	
	
	
	//Crawled-links stuff
	public static void markURLCrawled(URL loc) {
		crawledURLs.add(loc);
	}
	public static boolean isURLCrawled(URL loc) {
		return crawledURLs.contains(loc);
	}
	
	
	
	//Link registering stuff
	public static void addLinks(URL source, Elements linkElements) {
		linkRegister.add(source, linkElements);
	}
	public static CrawlsRegister getLinkRegister() {
		return linkRegister;
	}
	
	public static void addSpecialLink(ISpecialLinkType linktype) {
		linkRegister.add(linktype);
	}
	
	public static void addFailedLink(URLStrWrapper urlStrWrapper) {
		failedCrawlers.add(urlStrWrapper);
	}

	
	
	//Memory value stuff
	public static String getValue(String str) {
		if (str.equalsIgnoreCase("crawledURLs")) {
			return Util.implode(crawledURLs, ", ", ENCLOSIONS.CURLY);
		} else if (str.equalsIgnoreCase("linkRegister")) {
			return Util.implode(linkRegister, ", ", ENCLOSIONS.CURLY);
		} else if (str.equalsIgnoreCase("waitingCrawlers")) {
			return Util.implode(waitingCrawlers, ", ", ENCLOSIONS.CURLY);
		} else if (str.equalsIgnoreCase("startLoc")) {
			return startLoc.toString();
		} else if (str.equalsIgnoreCase("savDir")) {
			return savDir;
		} else if (str.equalsIgnoreCase("crawlersForRetry")) {
			return Util.implode(crawlersForRetry, ", ", ENCLOSIONS.CURLY);
		} else if (str.equalsIgnoreCase("failedCrawlers")) {
			return Util.implode(failedCrawlers, ", ", ENCLOSIONS.CURLY);
		} else {
			return null;
		}
	}
	public static boolean removeValue(String str) {
		if (str.equalsIgnoreCase("crawledURLs")) {
			crawledURLs = new ArrayList<URL>();
			return true;
		} else if (str.equalsIgnoreCase("linkRegister")) {
			linkRegister = new CrawlsRegister();
			return true;
		} else if (str.equalsIgnoreCase("waitingCrawlers")) {
			waitingCrawlers = new ArrayList<Crawler>();
			return true;
		} else if (str.equalsIgnoreCase("startLoc")) {
			startLoc = null;
			return true;
		} else if (str.equalsIgnoreCase("savDir")) {
			setSaveLocation();
			return true;
		} else if (str.equalsIgnoreCase("crawlersForRetry")) {
			crawlersForRetry = new ArrayList<Crawler>();
			return true;
		} else if (str.equalsIgnoreCase("failedCrawlers")) {
			failedCrawlers = new ArrayList<URLStrWrapper>();
			return true;
		} else {
			return false;
		}
	}
	public static String squash() {
		StringBuilder s = new StringBuilder();
		s.append("{");
		
		s.append("crawledURLs:");
		s.append(Util.implode(crawledURLs, ", ", ENCLOSIONS.SQUARE));
		s.append(", ");
		
		s.append("linkRegister:");
		s.append(Util.implode(linkRegister, ", ", ENCLOSIONS.SQUARE));
		s.append(", ");
		
		s.append("waitingCrawlers:");
		s.append(Util.implode(waitingCrawlers, ", ", ENCLOSIONS.SQUARE));
		s.append(", ");
		
		s.append("startLoc:");
		s.append(startLoc.toString());
		s.append(", ");
		
		s.append("savLoc:");
		s.append(savDir);
		s.append(", ");
		
		s.append("crawlersForRetry:");
		s.append(Util.implode(crawlersForRetry, ", ", ENCLOSIONS.SQUARE));
		s.append(", ");
		
		s.append("failedCrawlers:");
		s.append(Util.implode(failedCrawlers, ", ", ENCLOSIONS.SQUARE));
		//s.append(", ");
		
		s.append("}");
		return s.toString();
	}
	public static void reset() {
		crawledURLs = new ArrayList<URL>();
		linkRegister = new CrawlsRegister();
		waitingCrawlers = new ArrayList<Crawler>();
		startLoc = null;
		setSaveLocation();
		crawlersForRetry = new ArrayList<Crawler>();
		failedCrawlers = new ArrayList<URLStrWrapper>();
	}
	public static void printStatus() {
		Logger.out.println("Current status:");
		Logger.out.println(getCompletedCrawls() + " completed crawls");
		Logger.out.println("\t" + linkRegister.getLinkRegister().size() + " urls mapped");
		Logger.out.println("\t" + linkRegister.getBadLinksRegister().size() + " bad-urls mapped");
		Logger.out.println("\t" + linkRegister.getSpecialLinksRegister().size() + " special-links mapped");
		Logger.out.println(getCrawledURLs().size() + " URLs that are marked as crawled");
		Logger.out.println(getWaitingCrawlers().size() + " waiting crawlers");
		Logger.out.println(getCrawlersForRetry().size() + " crawlers registered for a retry");
		Logger.out.println(getFailedCrawlers().size() + " failed crawlers");
	}
}
