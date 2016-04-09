package info.zthings.crawler.classes.statics;

import info.zthings.crawler.classes.ENCLOSIONS;
import info.zthings.crawler.classes.URLStrWrapper;
import info.zthings.crawler.classes.URLWrapper;
import info.zthings.crawler.classes.interfaces.IImplodable;
import info.zthings.crawler.classes.interfaces.ISpecialLinkType;
import info.zthings.crawler.classes.linktypes.LinkJavascript;
import info.zthings.crawler.classes.linktypes.LinkUnsupportedProtocol;
import info.zthings.crawler.common.Util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/** Contains all links of a crawl */
public class CrawlsRegister implements IImplodable {
	private ArrayList<URLWrapper> reg = new ArrayList<URLWrapper>();
	private ArrayList<URLStrWrapper> badURLreg = new ArrayList<URLStrWrapper>();
	private ArrayList<ISpecialLinkType> specialReg = new ArrayList<ISpecialLinkType>();
	
	public void add(URL source, Elements pageLinks) {
		for (Element cLink : pageLinks) {
			try {
				reg.add(new URLWrapper(new URL(cLink.attr("abs:href")), source)); //abs is for the absolute URL (see javadoc)
			} catch (MalformedURLException e) {
				if (cLink.attr("href").startsWith("javascript:")) {
					specialReg.add(new LinkJavascript(source, cLink));
				} else if (e.getMessage().toLowerCase().contains("no protocol")) { //so it's just a protocol that java doesn't reconize (most likely)
					specialReg.add(new LinkUnsupportedProtocol(source, cLink.attr("href")));
				} else {
					Logger.warn("MalformedURL " + cLink.attr("href") + "; attributes:" + Util.implode(cLink.attributes(), ", ", ENCLOSIONS.CURLY));
					e.printStackTrace();
					badURLreg.add(new URLStrWrapper(cLink.attr("abs:href"), source));
				}
			}
		}
	}
	public void add(ISpecialLinkType special) {
		specialReg.add(special);
	}
	
	
	public ArrayList<URLWrapper> getLinkRegister() {
		return this.reg;
	}
	public ArrayList<URLStrWrapper> getBadLinksRegister() {
		return this.badURLreg;
	}
	public ArrayList<ISpecialLinkType> getSpecialLinksRegister() {
		return specialReg;
	}
	

	@Override
	public String implode(String glue) {
		StringBuilder sb = new StringBuilder();
		sb.append("good:{");
		for (URLWrapper en : reg) {
			sb.append(en.getSource());
			sb.append("->");
			sb.append(en.getURL().toString());
			sb.append(", ");
		}
		sb.substring(0, sb.length()-2); //cutoff last ', '
		sb.append("}bad:{");
		for (URLStrWrapper en : badURLreg) {
			sb.append(en.getSource());
			sb.append("->");
			sb.append(en.getURL());
			sb.append(", ");
		}
		sb.substring(0, sb.length()-2); //cutoff last ', '
		sb.append("}special:{");
		for (ISpecialLinkType cl : specialReg) {
			sb.append(cl.getSource());
			sb.append("=");
			sb.append(cl.getMsg());
			sb.append(", ");
		}
		sb.substring(0, sb.length()-2); //cutoff last ', '
		sb.append("}");
		return sb.toString();
	}
}
