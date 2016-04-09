package info.zthings.crawler.classes.linktypes;

import info.zthings.crawler.classes.interfaces.ISpecialLinkType;

import java.net.MalformedURLException;
import java.net.URL;

public abstract class BasicSpecialLink implements ISpecialLinkType {
	protected URL source;
	protected String target;
	
	public BasicSpecialLink(URL source, String target) {
		this.source = source;
		this.target = target;
	}
	public BasicSpecialLink(URL source, URL target) {
		this.source = source;
		this.target = target.toString();
	}
	
	@Override
	public URL getSource() {
		return this.source;
	}
	
	protected URL getTargetAsObj() throws MalformedURLException {
		return new URL(this.target);
	}
	
	/*@Override
	public String getDetails() {
		return "";
	}*/
}
