package info.zthings.crawler.commands.defaultcommands;

import info.zthings.crawler.classes.Crawler;
import info.zthings.crawler.classes.ICommand;
import info.zthings.crawler.classes.statics.Logger;
import info.zthings.crawler.classes.statics.Memory;
import info.zthings.crawler.common.Ref;

public class CmdCrawl implements ICommand {
	@Override
	public String getHelpText() {
		return "Starts the crawling procedure at the location in memory (set with SetLocation)";
	}
	@Override
	public void execute(String[] params) {
		Logger.out.println(Ref.sep(48+Memory.getStartingLocation().toString().length()));
		Logger.out.println("Recieved request to start a crawling session at " + Memory.getStartingLocation());
		if (Memory.getStartingLocation().equals("")) {
			Logger.err("No location in memory. Use \"help setlocation\" for more info");
			Logger.out.println(Ref.sep(48+Memory.getStartingLocation().toString().length()));
			return;
		}
		Memory.startQueue(new Crawler(Memory.getStartingLocation(), null));
		Logger.out.println(Ref.sep(48+Memory.getStartingLocation().toString().length()));
	}
}
