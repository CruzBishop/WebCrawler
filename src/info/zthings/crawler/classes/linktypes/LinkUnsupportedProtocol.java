package info.zthings.crawler.classes.linktypes;

import info.zthings.crawler.classes.statics.Logger;

import java.net.MalformedURLException;
import java.net.URL;

public class LinkUnsupportedProtocol extends BasicSpecialLink {
	private boolean isURLObj;

	public LinkUnsupportedProtocol(URL source, URL badurl) {
		super(source, badurl);
		this.isURLObj = true;
	}
	
	public LinkUnsupportedProtocol(URL source, String badurl) {
		super(source, badurl);
		this.isURLObj = false;
	}

	@Override
	public String getMsg() {
		if (this.isURLObj) { //constructed with an URL, so it has to be an unsupported protocol thrown by jsoup
			try {
				if (getTargetAsObj().getProtocol().equalsIgnoreCase("mailto")) {
					return "Mailto-link: " + this.target.substring(6);
				} else if (getTargetAsObj().getProtocol().equalsIgnoreCase("callto")) {
					return "Callto-link: " + this.target.substring(6);
				} else {
					return "Unsupported protocol: " + this.target;
				}
			} catch (MalformedURLException e) {
				Logger.fatal("BUG: constructed with URL, but can't reconstruct URL-obj", e);
				return null; //it exited in Logger.fatal
			}
		} else { //constructed with string, so it has to be an protocol java.net.URL doesn't reconize
			if (target.startsWith("tel:")) {
				return "Phone number: " + target.substring(4);
			} else if (target.startsWith("whatsapp://")) {
				return "Whatsapp share-link";
			} else {
				return "Unknown protocol: " + target;
			}
		}
	}
	
	/*@Override
	public String getDetails() {
		if (badurl != null) {
			return badurl.toString();
		} else {
			return badurl_str;
		}
	}*/
}
