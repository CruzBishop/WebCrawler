package info.zthings.crawler.classes.linktypes;

import info.zthings.crawler.classes.interfaces.ISpecialLinkType;

import java.net.URL;

import org.jsoup.nodes.Element;

public class LinkJavascript implements ISpecialLinkType {
	private URL source;
	private Element el;
	
	public LinkJavascript(URL source, Element link) {
		this.source = source;
		this.el = link;
	}
	
	@Override
	public String getMsg() {
		return "Javascript method " + el.attr("href").substring(11);
	}

	@Override
	public URL getSource() {
		return source;
	}
	
}
