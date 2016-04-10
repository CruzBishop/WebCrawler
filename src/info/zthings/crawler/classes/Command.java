package info.zthings.crawler.classes;

public interface Command {
	public abstract String getName();
	public abstract String getHelpText();
	/** Params index 0 is always the command name used (in original upper/lower/camle case */
	public abstract void execute(String[] params);
}
