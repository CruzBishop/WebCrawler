package info.zthings.crawler.commands.defaultcommands;

import info.zthings.crawler.classes.ICommand;
import info.zthings.crawler.classes.statics.Logger;
import info.zthings.crawler.classes.statics.Memory;

public class CmdStatus implements ICommand {
	@Override
	public String getHelpText() {
		return "Displays the current status of memory (use 1st param to get a specific index)";
	}

	@Override
	public void execute(String[] params) {
		if (params.length < 2) {
			Logger.out.println("Memory status: ");
			Logger.out.println(Memory.squash());
		} else {
			Logger.out.println("Memory key '" + params[1] + "' status:");
			Logger.out.println(Memory.getValue(params[1]));
		}
	}
}
