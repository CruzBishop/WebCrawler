package info.zthings.crawler.commands;

import info.zthings.crawler.classes.Command;
import info.zthings.crawler.common.ConsoleUI;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class CommandHandler {
	private static HashMap<String, Command> cmdList = new HashMap<String, Command>();
	private static boolean inited = false;
	
	public static void init() {
		if (inited) {
			ConsoleUI.warn("CommandHandler is already inited!");
			return;
		}
		
		registerCommand(new DefaultCommands.Help());
		registerCommand(new DefaultCommands.SetLocation());
		registerCommand(new DefaultCommands.Crawl());
		registerCommand(new DefaultCommands.Status());
		registerCommand(new DefaultCommands.Exec());
	}
	
	public static void registerCommand(Command cmd) {
		cmdList.put(cmd.getName(), cmd);
	}
	
	public static void parseCommand(String ... cmd) {
		if (cmd[0].matches("exit") || cmd[0].matches("exit .*")) ConsoleUI.terminate();
		
		for (String n : cmdList.keySet()) {
			if (cmd[0].toLowerCase().equals(n.toLowerCase())) {
				cmdList.get(n).execute(cmd);
				return;
			}
		}
		ConsoleUI.out("Unreconized command: '" + cmd[0].toLowerCase() + "'");
	}
	
	public static Command getCommand(String n) {
		return cmdList.get(n);
	}
	
	public static Set<Entry<String, Command>> getCommandList() {
		return cmdList.entrySet();
	}
}
