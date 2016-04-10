package info.zthings.crawler.commands.defaultcommands;

import info.zthings.crawler.classes.interfaces.ICommand;
import info.zthings.crawler.classes.statics.Logger;
import info.zthings.crawler.classes.statics.Memory;
import info.zthings.crawler.commands.CommandParameterException;

import java.net.MalformedURLException;
import java.net.URL;

public class CmdSetLocation implements ICommand {
	@Override
	public String getHelpText() {
		return "Sets the given location (parameter 1) to be the starting page for the next crawl\nUsage: setlocation address_to_crawl";
	}
	@Override
	public void execute(String[] params) {
		if (params.length < 2) CommandParameterException.e(this);
		try {
			Memory.setStartingLocation(new URL(params[1]));
		} catch (MalformedURLException e) {
			Logger.out.println("Invalid starter location: " + params[1]);
		}
	}
}
