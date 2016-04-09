package info.zthings.crawler.classes.linktypes;

import java.net.URL;

public class LinkMalformedURL extends BasicSpecialLink {
	
	public LinkMalformedURL(URL source, String malformedurl) {
		super(source, malformedurl);
	}
	
	@Override
	public String getMsg() {
		return "MalformedURL: " + target;
	}
	
}
