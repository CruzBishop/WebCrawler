package info.zthings.crawler.classes;

import info.zthings.crawler.common.Ref;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;


public class Crawler {
	//TODO: dual-stream with ConsoleUI & logfile?
	private PrintStream log;
	private final String BASE_LOCATION = "crawls/date-" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH) + "-" + Calendar.getInstance().get(Calendar.MONTH) + "-" + Calendar.getInstance().get(Calendar.YEAR) + "/time-" + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + "-" + Calendar.getInstance().get(Calendar.MINUTE) + "-" + Calendar.getInstance().get(Calendar.SECOND) + "/";	private String loc;
	
	public Crawler(String httploc) {
		this.loc = httploc;
	}

	public void start() {
		try {
			new File(BASE_LOCATION).mkdirs();
			File f = new File(BASE_LOCATION + "start.log");
			f.createNewFile();
			log = new PrintStream(f);
		} catch (IOException e) {
			e.printStackTrace();
		}

		log.println("CRAWLING AT "+loc);
		log.println(Ref.SEP);
		log.println("Requesting page body...");
		
		try {
			URL oracle = new URL(loc);
			BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
			
			String p = BASE_LOCATION + loc.substring(11).replace('.', '-');
			if (p.charAt(p.length()-1) == '/') p = p.substring(0, p.length()-1);
			BodyBuffer bBuffer = new BodyBuffer(p);
			String cl;
			while ((cl = in.readLine()) != null) bBuffer.println(cl);
			in.close();
		} catch (MalformedURLException e) { 
			log.println("URL is invalid for Java: " + loc);
			e.printStackTrace();
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
