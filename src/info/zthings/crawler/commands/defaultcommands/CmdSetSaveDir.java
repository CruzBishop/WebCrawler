package info.zthings.crawler.commands.defaultcommands;

import info.zthings.crawler.classes.ICommand;
import info.zthings.crawler.classes.statics.Memory;
import info.zthings.crawler.commands.CommandParameterException;
import info.zthings.crawler.common.Util;

public class CmdSetSaveDir implements ICommand {
	
	@Override
	public String getHelpText() {
		return "Sets the location where the data for the next crawl will be saved to the first param"
				+ "\nDefault value is /Day/Month/Year/Hour/Minute/Second/StartingLocation"
				+ "\nUse %x% to insert a date format (d for day, m for month, y for year, h for hour, m for minutes & s for seconds)";
	}
	
	@Override
	public void execute(String[] params) {
		if (params.length < 2) CommandParameterException.e(this);
		Memory.setSaveLocation(Util.formatDate(params[1]));
	}
	
}
