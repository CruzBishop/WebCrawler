package info.zthings.crawler.classes;

public class EnumUpdateException extends RuntimeException {
	private static final long serialVersionUID = -1457313495762083820L;
	
	public EnumUpdateException() {
		super("You forgot to update this method dumbass!");
	}
}
