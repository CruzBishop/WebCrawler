package info.zthings.crawler.commands.defaultcommands;

import info.zthings.crawler.classes.interfaces.ICommand;
import info.zthings.crawler.classes.statics.Logger;
import info.zthings.crawler.commands.CommandHandler;

import java.util.Map.Entry;

public class CmdHelp implements ICommand {
	@Override
	public String getHelpText() {
		return "Lists out all commands with their description (or supply the first parameter with a commandname to get the description of only that command)";
	}
	
	@Override
	public void execute(String[] params) {
		if (params.length == 1) { //just list out
			int longestlength = 0;
			String longestkey = "";
			//First get the longest name
			for (Entry<String, ICommand> en : CommandHandler.getCommandList()) {
				if (en.getKey().length() > longestlength) {
					longestlength = en.getKey().length();
					longestkey = en.getKey();
				}
			}
			
			//Calculate needed tabs
			int nTabs = longestlength/10+1;
			String tabs = "\t";
			for (int i=0; i<nTabs; i++) {
				tabs += "\t";
			}
			
			//Format & print all the entries
			for (Entry<String, ICommand> en : CommandHandler.getCommandList()) {
				String fTabs;
				if (en.getKey().equals(longestkey)) fTabs = tabs.substring(2);
				else fTabs = tabs.substring(1);
				
				Logger.out.println(en.getKey() + fTabs + " - " + en.getValue().getHelpText().replace("\n", "\n"+fTabs+"\t   "));
			}
		} else {
			ICommand c = CommandHandler.getCommand(params[1]);
			if (c != null) Logger.out.println(c.getClass().getName().toLowerCase() + " - " + c.getHelpText());
			else Logger.warn("Unreconized command: " + params[1]);
		}
	}
}
