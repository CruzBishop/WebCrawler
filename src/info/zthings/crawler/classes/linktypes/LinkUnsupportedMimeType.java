package info.zthings.crawler.classes.linktypes;

import java.net.URL;

import org.jsoup.UnsupportedMimeTypeException;

public class LinkUnsupportedMimeType extends BasicSpecialLink {
	private String type;

	public LinkUnsupportedMimeType(URL source, URL file, UnsupportedMimeTypeException ex) {
		super(source, file);
		this.type = ex.getMimeType();
	}
	
	@Override
	public String getMsg() {
		return "Mime-type:" + this.type;
	}
	
	/*@Override
	public String getDetails() {
		return target;
	}*/
}
