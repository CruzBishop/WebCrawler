package info.zthings.crawler.commands;

import info.zthings.crawler.classes.interfaces.ICommand;
import info.zthings.crawler.classes.statics.Logger;


public class CommandParameterException {
		public static void e(ICommand c) {
			Logger.out.println("Invalid arguments! Run \"help " + c.getClass().getName() + "\" for help");
		}
}