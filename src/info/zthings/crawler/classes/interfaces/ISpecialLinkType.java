package info.zthings.crawler.classes.interfaces;

import java.net.URL;

public interface ISpecialLinkType {
	public abstract URL getSource();
	public abstract String getMsg();
	//FUTURE (for when loading)
	//public abstract RGB(?) getColor();
	//public abstract String getDetails();
}
