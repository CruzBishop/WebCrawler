package info.zthings.crawler.commands.defaultcommands;

import info.zthings.crawler.classes.interfaces.ICommand;
import info.zthings.crawler.classes.statics.CrawlsRegister;
import info.zthings.crawler.classes.statics.Logger;
import info.zthings.crawler.classes.statics.Memory;
import info.zthings.crawler.commands.CommandHandler;

import java.io.File;
import java.io.IOException;

import org.ini4j.Wini;

public class CmdSave implements ICommand {
	@Override
	public String getHelpText() {
		return "Flushes memory in result.dat file\nUse first param for the location of result.dat, or else the most recent one in Memory is used";
	}

	@Override
	public void execute(String[] params) {
		String saveDir;
		if (params.length < 2) saveDir = Memory.getSaveLocation();
		else saveDir = params[1];
		
		CrawlsRegister linkRegister = Memory.getLinkRegister();
		try {
			Wini sav = new Wini(new File(saveDir + "results.dat"));
			//STUB FIXME save results
			sav.store();
		} catch (IOException e) {
			Logger.out.println("IOException while trying to save the link-register");
			e.printStackTrace();
		}
		CommandHandler.parseCommand("clear", "links");
	}
}
