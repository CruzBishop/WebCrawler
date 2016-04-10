package info.zthings.crawler.classes.linktypes;

import java.net.URL;

import org.jsoup.nodes.Element;

public class LinkJavascript extends BasicSpecialLink {
	private Element el;
	
	public LinkJavascript(URL source, Element link) {
		super(source);
		this.el = link;
	}
	
	@Override
	public String getMsg() {
		return "Javascript method " + el.attr("href").substring(11);
	}
	
}
