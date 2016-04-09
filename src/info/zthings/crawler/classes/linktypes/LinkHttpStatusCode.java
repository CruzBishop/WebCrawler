package info.zthings.crawler.classes.linktypes;

import java.net.URL;

import org.jsoup.HttpStatusException;

public class LinkHttpStatusCode extends BasicSpecialLink {
	private HttpStatusException ex;

	public LinkHttpStatusCode(URL source, URL httploc, HttpStatusException e) {
		super(source, httploc);
		this.ex = e;
	}
	
	@Override
	public String getMsg() {
		switch (this.ex.getStatusCode()) {
			case 404:
				return "404 File Not Found - " + target;
			case 403:
				return "403 Acces Denied - " + target;
			case 503:
				return "503 Service Unavaillable - " + target;
			default:
				return ex.getStatusCode() + " status";
		}
	}
	
}
