package info.zthings.crawler.classes.linktypes;

import java.net.URL;

public class LinkMalformedURL extends BasicSpecialLink {
	private String malformedurl;
	
	public LinkMalformedURL(URL source, String malformedurl) {
		super(source);
		this.malformedurl = malformedurl;
	}
	
	@Override
	public String getMsg() {
		return "MalformedURL: " + malformedurl;
	}
	
}
