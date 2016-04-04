package info.zthings.crawler.commands;

import info.zthings.crawler.classes.Command;
import info.zthings.crawler.classes.Crawler;
import info.zthings.crawler.common.ConsoleUI;
import info.zthings.crawler.common.Memory;
import info.zthings.crawler.common.Ref;
import info.zthings.crawler.common.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class DefaultCommands {
	//NOW output command to save memory
	public static class Output implements Command {
		@Override
		public String getName() {
			return "output";
		}

		@Override
		public String getHelpText() {
			return "Flushes memory in result.cwr file\nUse first param for the location of result.cwr, or else the most recent one in Memory is used";
		}

		@Override
		public void execute(String[] params) {
			String lBuff;
			if (params.length < 2) lBuff = Memory.getBaseLoc();
			else lBuff = params[1];
			
			try {
				PrintStream st = new PrintStream(lBuff + "result.cwr");
				HashMap<String, ArrayList<String>> links = (HashMap<String, ArrayList<String>>) Memory.getLinks();
				
				for (Entry<String, ArrayList<String>> en : links.entrySet()) {
					st.println("#"+en.getKey());
					for (String l : en.getValue()) {
						st.println(l + ",");
					}
				}
				
				st.close();
				
				CommandHandler.parseCommand("clear", "links");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
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
			if (params.length < 2) ParameterException.e(this);
			Memory.setCrawlLocation(params[1]);
		}
	}
	
	public static class Status implements Command {
		@Override
		public String getName() {
			return "status";
		}

		@Override
		public String getHelpText() {
			return "Displays the current status of memory (use 1st param to get a specific index)";
		}

		@Override
		public void execute(String[] params) {
			if (params.length < 2) {
				ConsoleUI.out("Memory status: ");
				ConsoleUI.out(Memory.squash());
			} else {
				ConsoleUI.out("Memory key '" + params[1] + "' status:");
				ConsoleUI.out(Memory.getKey(params[1]));
			}
		}
	}
	
	public static class Clear implements Command {
		@Override
		public String getName() {
			return "clear";
		}

		@Override
		public String getHelpText() {
			return "Clears memory (use 1st param the clear specific entry)";
		}

		@Override
		public void execute(String[] params) {
			if (params.length < 2) {
				ConsoleUI.out("Clearing memory...");
				Memory.init();
				CommandHandler.parseCommand("status");
			} else {
				HashMap<String, Object> m = Memory.getMap();
				
				if (params[1].equals("loc")) m.put("loc", Memory.loc_def_value);
				if (params[1].equals("crawled")) m.put("crawled", Memory.crawled_def_value);
				if (params[1].equals("stack")) m.put("stack", Memory.stack_def_value);
				if (params[1].equals("links")) m.put("links", Memory.links_def_value);
				if (params[1].equals("base_loc")) m.put("base_loc", Memory.base_loc_def_value);
				
				Memory.setMemory(m);
				CommandHandler.parseCommand("status", params[1]);
			}
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
			ConsoleUI.out(Ref.sep(48+Memory.getCrawlLocation().length()));
			ConsoleUI.out("Recieved request to start a crawling session at " + Memory.getCrawlLocation());
			if (Memory.getCrawlLocation().equals("")) {
				ConsoleUI.err("No location in memory. Use help setlocation for more info");
				ConsoleUI.out(Ref.sep(48+Memory.getCrawlLocation().length()));
				return;
			}
			Crawler craw = new Crawler(Memory.getCrawlLocation());
			craw.start();
			ConsoleUI.out(Ref.sep(48+Memory.getCrawlLocation().length()));
		}
	}

	public static class Exec implements Command {
		@Override
		public String getName() {
			return "exec";
		}

		@Override
		public String getHelpText() {
			return "Executes the *.cr script specified in the 1st param";
		}

		@Override
		public void execute(String[] params) {
			if (params.length < 2) ParameterException.e(this);
			String s = params[1];
			if (s.indexOf(".") < 0) s += ".cr"; //add default extension
			
			File f = new File(s);
			try {
				BufferedReader reader = new BufferedReader(new FileReader(f));
				String l;
				while ((l = reader.readLine()) != null) {
					ConsoleUI.out("[] " + l.split(" ")[0]);
					CommandHandler.parseCommand(l.split(" "));
				}
				reader.close();
			} catch (FileNotFoundException e) {
				ConsoleUI.out("Script '" + s + "' doesn't exist");
				Util.fNotice("Requested script file '" + f.getAbsolutePath() + "' doesn't exist");
			} catch (IOException e) {
				ConsoleUI.out("Other IO exception");
				e.printStackTrace();
			}
		}
	}
}
