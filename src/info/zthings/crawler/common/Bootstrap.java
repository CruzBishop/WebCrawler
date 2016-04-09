package info.zthings.crawler.common;

import info.zthings.crawler.classes.ENCLOSIONS;
import info.zthings.crawler.classes.interfaces.ICommand;
import info.zthings.crawler.classes.statics.Logger;
import info.zthings.crawler.commands.CommandHandler;

/* Known issues:
 */

public class Bootstrap {
	
	public static void main(String[] args) {
		Logger.init();
		
		Logger.out.println("CRAWLER " + Ref.VER);
		Logger.out.println(Ref.sep(9+Ref.VER.length()));
		
		CommandHandler.init();
		
		execArgCommands(args);
		ConsoleUI.enterCmdLoop();
	}
	
	public static void execArgCommands(String[] args) {
		for (String c : args) {
			Logger.out.println("Recieved command \"" + c + "\" through command-line arguments");
			
			String[] b;
			if (c.indexOf("]") > c.indexOf("[")) {
				String[] p = c.substring(c.indexOf("[")+1, c.indexOf("]")).split(",");
				Logger.out.println("Recieved params " + Util.implode(p, ", ", ENCLOSIONS.SQUARE));
				c = c.substring(0, c.indexOf("["));
				
				b = new String[p.length+1];
				b[0] = c;
				for (int i=0; i<p.length; i++) {
					b[1+i] = p[i];
				}
			} else {
				b = new String[] {c};
			}
			
			ICommand cmd = CommandHandler.getCommand(c);
			if (cmd == null) {
				Logger.out.println("Unreconized command: " + c);
				continue;
			}
			cmd.execute(b);
		}

	}
	
}
