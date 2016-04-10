package info.zthings.crawler.classes.linktypes;

import java.net.URL;

public abstract class BasicSpecialLink implements ISpecialLinkType {
	protected URL source;
	
	public BasicSpecialLink(URL source) {
		this.source = source;
	}
	
	@Override
	public URL getSource() {
		return this.source;
	}
}
