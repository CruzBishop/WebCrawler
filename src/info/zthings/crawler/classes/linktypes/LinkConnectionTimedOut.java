package info.zthings.crawler.classes.linktypes;

import java.net.URL;

public class LinkConnectionTimedOut extends BasicSpecialLink {
	private URL url;
	
	public LinkConnectionTimedOut(URL source, URL timedout) {
		super(source);
		this.url = timedout;
	}
	
	@Override
	public String getMsg() {
		return "Connection timed out (" + url + ")";
	}
	
}
