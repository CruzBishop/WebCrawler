package info.zthings.crawler.classes;

import java.net.URL;

public class URLWrapper {
	private URL url;
	private URL source;
	
	public URLWrapper(URL url, URL source) {
		this.url = url;
		this.source = source;
	}
	
	public URL getURL() {
		return this.url;
	}
	
	public URL getSource() {
		return this.source;
	}
	
}
