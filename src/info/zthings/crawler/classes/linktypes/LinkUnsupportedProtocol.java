package info.zthings.crawler.classes.linktypes;

import java.net.URL;

public class LinkUnsupportedProtocol extends BasicSpecialLink {
	private URL badurl;

	public LinkUnsupportedProtocol(URL source, URL badurl) {
		super(source);
		this.badurl = badurl;
	}
	
	@Override
	public String getMsg() {
		if (badurl.getProtocol().equalsIgnoreCase("mailto")) {
			return "Mailto-link: " + badurl.toString().substring(6);
		} else if (badurl.getProtocol().equalsIgnoreCase("callto")) {
			return "Callto-link: " + badurl.toString().substring(6);
		} else {
			return "Unsupported protocol: " + badurl.toString();
		}
	}
}
