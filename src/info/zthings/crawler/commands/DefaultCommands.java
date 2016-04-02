package info.zthings.crawler.commands;

import info.zthings.crawler.classes.Crawler;
import info.zthings.crawler.common.ConsoleUI;
import info.zthings.crawler.common.Memory;

import java.util.Map.Entry;

public class DefaultCommands {
	public static class Help implements Command {
		@Override
		public String getName() {
			return "help";
		}
		
		@Override
		public String getHelpText() {
			return "Lists out all commands with their description or supply the first parameter with a commandname to get the description of only that command";
		}
		
		@Override
		public void execute(String[] params) {
			if (params.length == 1) { //just list out
				int longestlength = 0;
				String longestkey = "";
				//First get the longest name
				for (Entry<String, Command> en : CommandHandler.getCommandList()) {
					if (en.getKey().length() > longestlength) {
						ConsoleUI.outF(en.getKey().length() + " > " + longestlength + " making it the longestlength");
						longestlength = en.getKey().length();
						longestkey = en.getKey();
					}
				}
				
				//Calculate needed tabs
				int nTabs = longestlength/10+1;
				String tabs = "\t";
				for (int i=0; i<nTabs; i++) {
					ConsoleUI.outF("Appending " + i + "st tab");
					tabs += "\t";
				}
				ConsoleUI.outF("Tab result: " + tabs);
				
				//Format & print all the entries
				for (Entry<String, Command> en : CommandHandler.getCommandList()) {
					String fTabs;
					if (en.getKey().equals(longestkey)) fTabs = tabs.substring(2);
					else fTabs = tabs.substring(1);
					
					ConsoleUI.out(en.getKey() + fTabs + " - " + en.getValue().getHelpText().replace("\n", "\n"+fTabs+"\t   "));
				}
			} else {
				Command c = CommandHandler.getCommand(params[1]);
				if (c != null) ConsoleUI.out(c.getName() + " - " + c.getHelpText());
				else ConsoleUI.warn("Unreconized command: " + params[1]);
			}
		}
	}


	public static class SetLocation implements Command {
		@Override
		public String getName() {
			return "setlocation";
		}
		@Override
		public String getHelpText() {
			return "Saves the given location (parameter 1) in memory\nUsage: setlocation address_to_crawl";
		}
		@Override
		public void execute(String[] params) {
			//FIXME: check for valid http
			if (params.length < 2) throw new ParameterException(this);
			Memory.setLocation(params[1]);
		}
	}
	
	
	public static class Status implements Command {
		@Override
		public String getName() {
			return "status";
		}

		@Override
		public String getHelpText() {
			return "Displays the current status of memory";
		}

		@Override
		public void execute(String[] params) {
			ConsoleUI.out("Memory status: ");
			ConsoleUI.out(Memory.squash());
		}
	}
	
	
	public static class Crawl implements Command {
		@Override
		public String getName() {
			return "crawl";
		}
		@Override
		public String getHelpText() {
			return "Starts the crawling procedure at the location in memory (set with SetLocation)";
		}
		@Override
		public void execute(String[] params) {
			ConsoleUI.out("Recieved request to start crawling at " + Memory.getLocation());
			Crawler craw = new Crawler(Memory.getLocation());
			craw.start();
		}
	}
}