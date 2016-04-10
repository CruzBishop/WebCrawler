package info.zthings.crawler.commands;

import info.zthings.crawler.classes.interfaces.ICommand;
import info.zthings.crawler.classes.statics.Logger;
import info.zthings.crawler.commands.defaultcommands.CmdClear;
import info.zthings.crawler.commands.defaultcommands.CmdCrawl;
import info.zthings.crawler.commands.defaultcommands.CmdExec;
import info.zthings.crawler.commands.defaultcommands.CmdHelp;
import info.zthings.crawler.commands.defaultcommands.CmdSave;
import info.zthings.crawler.commands.defaultcommands.CmdSetLocation;
import info.zthings.crawler.commands.defaultcommands.CmdSetSaveDir;
import info.zthings.crawler.commands.defaultcommands.CmdStatus;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class CommandHandler {
	private static HashMap<String, ICommand> cmdList = new HashMap<String, ICommand>();
	private static boolean inited = false;
	
	public static void init() {
		if (inited) {
			Logger.warn("CommandHandler is already inited!");
			return;
		}
		
		registerCommand(new CmdHelp());
		registerCommand(new CmdSetLocation());
		registerCommand(new CmdCrawl());
		registerCommand(new CmdStatus());
		registerCommand(new CmdExec());
		registerCommand(new CmdClear());
		registerCommand(new CmdSave());
		registerCommand(new CmdSetSaveDir());
		inited = true;
	}
	
	public static void registerCommand(ICommand cmd) {
		cmdList.put(cmd.getClass().getSimpleName().toLowerCase().substring(3), cmd); //cutoff the Cmd prefix
	}
	
	public static void parseCommand(String ... cmd) {
		for (String n : cmdList.keySet()) {
			if (cmd[0].equalsIgnoreCase(n)) {
				cmdList.get(n).execute(cmd);
				return;
			}
		}
		Logger.out.println("Unreconized command: '" + cmd[0].toLowerCase() + "'");
	}
	
	public static ICommand getCommand(String n) {
		return cmdList.get(n);
	}
	
	public static Set<Entry<String, ICommand>> getCommandList() {
		return cmdList.entrySet();
	}
}
