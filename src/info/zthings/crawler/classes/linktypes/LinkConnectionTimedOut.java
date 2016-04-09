package info.zthings.crawler.classes.linktypes;

import java.net.URL;

public class LinkConnectionTimedOut extends BasicSpecialLink {
	public LinkConnectionTimedOut(URL source, URL timedout) {
		super(source, timedout);
	}
	
	@Override
	public String getMsg() {
		return "Connection timed out: " + target;
	}
	
}
