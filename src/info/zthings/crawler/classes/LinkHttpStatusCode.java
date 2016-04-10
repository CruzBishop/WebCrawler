package info.zthings.crawler.classes;

import info.zthings.crawler.classes.linktypes.BasicSpecialLink;

import java.net.URL;

import org.jsoup.HttpStatusException;

public class LinkHttpStatusCode extends BasicSpecialLink {
	private URL httploc;
	private HttpStatusException ex;

	public LinkHttpStatusCode(URL source, URL httploc, HttpStatusException e) {
		super(source);
		this.httploc = httploc;
		this.ex = e;
	}
	
	@Override
	public String getMsg() {
		switch (this.ex.getStatusCode()) {
			case 404:
				return "404 File Not Found - " + this.httploc;
			case 403:
				return "403 Acces Denied - " + this.httploc;
			case 503:
				return "503 Service Unavaillable - " + this.httploc;
			default:
				return ex.getStatusCode() + " status";
		}
	}
	
}
