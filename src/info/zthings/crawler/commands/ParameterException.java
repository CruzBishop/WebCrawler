package info.zthings.crawler.commands;

import info.zthings.crawler.classes.Command;


public class ParameterException {
		public static void e(Command thi) {
			System.out.println("Invalid arguments! Run \"help " + thi.getName() + "\" for help");
		}
}