package info.zthings.crawler.commands.defaultcommands;

import info.zthings.crawler.classes.interfaces.ICommand;
import info.zthings.crawler.classes.statics.Logger;
import info.zthings.crawler.classes.statics.Memory;

public class CmdClear implements ICommand {
	@Override
	public String getHelpText() {
		return "Clears memory (use 1st param to clear specific entry)";
	}
	
	@Override
	public void execute(String[] params) {
		if (params.length < 2) {
			Memory.reset();
		} else {
			if (!Memory.removeValue(params[1])) Logger.out.println("Memory key not found");
		}
	}
}
