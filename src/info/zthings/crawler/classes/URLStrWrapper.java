package info.zthings.crawler.classes;

import java.net.URL;

public class URLStrWrapper {
	private String url;
	private URL source;
	
	public URLStrWrapper(String url, URL source) {
		this.url = url;
		this.source = source;
	}
	
	public String getURL() {
		return this.url;
	}
	
	public URL getSource() {
		return this.source;
	}
	
}
