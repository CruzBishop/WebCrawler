package info.zthings.crawler.commands.defaultcommands;

import info.zthings.crawler.classes.interfaces.ICommand;
import info.zthings.crawler.classes.statics.Logger;
import info.zthings.crawler.commands.CommandHandler;
import info.zthings.crawler.commands.CommandParameterException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CmdExec implements ICommand {
	@Override
	public String getHelpText() {
		return "Executes the *.cr script specified in the 1st param";
	}

	@Override
	public void execute(String[] params) {
		if (params.length < 2) CommandParameterException.e(this);
		String s = params[1];
		if (s.indexOf(".") < 0) s += ".cr"; //add default extension
		
		File f = new File(s);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String l;
			while ((l = reader.readLine()) != null) {
				Logger.out.println("[] " + l.split(" ")[0]);
				CommandHandler.parseCommand(l.split(" "));
			}
			reader.close();
		} catch (FileNotFoundException e) {
			Logger.out.println("Script '" + s + "' doesn't exist");
		} catch (IOException e) {
			Logger.out.println("Other IO exception while executing 'exec' command");
			e.printStackTrace();
		}
	}
}
