package info.zthings.crawler.classes.interfaces;

public interface ICommand {
	public abstract String getHelpText();
	/** Params index 0 is always the command name used (in original upper/lower/camle case */
	public abstract void execute(String[] params);
}
