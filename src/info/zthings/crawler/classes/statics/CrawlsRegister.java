package info.zthings.crawler.classes.statics;

import info.zthings.crawler.classes.ENCLOSIONS;
import info.zthings.crawler.classes.IImplodable;
import info.zthings.crawler.classes.linktypes.ISpecialLinkType;
import info.zthings.crawler.classes.linktypes.LinkJavascript;
import info.zthings.crawler.common.Util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/** Contains all links of a crawl */
public class CrawlsRegister implements IImplodable {
	/** Sourcepage => List of URL's */
	//CHECK refactor to URLWrapper?
	private HashMap<URL, ArrayList<URL>> reg = new HashMap<URL, ArrayList<URL>>();
	/** Sourcepage => List of 'bad' (malformed) URL's */
	//CHECK refactor to URLWrapper?
	private HashMap<URL, ArrayList<String>> badURLreg = new HashMap<URL, ArrayList<String>>();
	private ArrayList<ISpecialLinkType> specialReg = new ArrayList<ISpecialLinkType>();
	
	public void add(URL source, Elements pageLinks) {
		if (reg.containsKey(source) || badURLreg.containsKey(source.toString())) throw new IllegalStateException("BUG: Double crawling! " + source.toString() + " already exists in the register");
		
		ArrayList<URL> pageLinksBuffer = new ArrayList<URL>();
		ArrayList<String> badLinksBuffer = new ArrayList<String>();
		for (Element cLink : pageLinks) {
			//NOTE handle 404's, 403's, mailto at the save loading with getProtocol()
			try {
				if (cLink.attr("href").startsWith("javascript:")) {
					Logger.out.println("Found javascript link");
					specialReg.add(new LinkJavascript(source, cLink));
				} else pageLinksBuffer.add(new URL(cLink.attr("abs:href"))); //abs is for the absolute URL (see javadoc)
			} catch (MalformedURLException e) {
				Logger.warn("MalformedURL: " + cLink.attr("abs:href"));
				Logger.err.println("Caught MalformedURL exception while constructing crawler for element \"" + cLink.outerHtml() + "\"");
				e.printStackTrace();
				badLinksBuffer.add(cLink.attr("abs:href"));
			}
		}
		reg.put(source, pageLinksBuffer);
		badURLreg.put(source, badLinksBuffer);
	}
	public void add(ISpecialLinkType special) {
		specialReg.add(special);
	}
	
	public HashMap<URL, ArrayList<URL>> getLinkRegister() {
		return this.reg;
	}
	public HashMap<URL, ArrayList<String>> getBadLinksRegister() {
		return this.badURLreg;
	}

	@Override
	public String implode(String glue) {
		StringBuilder sb = new StringBuilder();
		sb.append("good:{");
		for (Entry<URL, ArrayList<URL>> en : reg.entrySet()) {
			sb.append(en.getKey().toString());
			sb.append(":");
			sb.append(Util.implode(en.getValue(), ",", ENCLOSIONS.SQUARE));
			sb.append(", ");
		}
		sb.substring(0, sb.length()-2); //cutoff last ', '
		sb.append("}bad:{");
		for (Entry<URL, ArrayList<String>> en : badURLreg.entrySet()) {
			sb.append(en.getKey().toString());
			sb.append(":");
			sb.append(Util.implode(en.getValue(), ",", ENCLOSIONS.SQUARE));
			sb.append(", ");
		}
		sb.substring(0, sb.length()-2); //cutoff last ', '
		sb.append("}special:{");
		//STUB
		sb.append("}");
		return sb.toString();
	}
}
