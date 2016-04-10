package info.zthings.crawler.classes.linktypes;

import java.net.URL;

public interface ISpecialLinkType {
	public abstract URL getSource();
	public abstract String getMsg();
	//FUTURE (for when loading)
	//public abstract RGB(?) getColor();
}
