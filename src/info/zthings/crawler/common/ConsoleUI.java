package info.zthings.crawler.common;

import info.zthings.crawler.commands.Command;
import info.zthings.crawler.commands.CommandHandler;
import info.zthings.crawler.commands.ParameterException;

import java.util.Scanner;

public class ConsoleUI {
	private static Scanner sc = new Scanner(System.in);
	
	public static void main(String[] args) {
		Log.out("CRAWLER v" + Ref.VER);
		Log.out(Ref.SEP.substring(0, 9+Ref.VER.length()));
		
		Log.out("Initializing...");
		Log.init("log.txt");
		CommandHandler.init();
		Log.out("Done!");
		
		for (String c : args) {
			Log.out("Recieved command \"" + c + "\" through command-line arguments");
			Command cmd = CommandHandler.getCommand(c);
			if (cmd == null) {
				Log.out("Unreconized command: " + c);
				continue;
			}
			cmd.execute(new String[] {c});
		}
		
		Log.out("Enter command:");
		
		while (true) {
			Log.outN("[] ");
			
			String cmd = sc.nextLine();
			
			Log.outF("Recieved command: " + cmd);
			
			if (cmd.matches("exit") || cmd.matches("exit .*")) terminate();
			
			try {
				CommandHandler.parseCommand(cmd.split(" "));
			} catch (ParameterException e) {
				Log.out(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void terminate() {
		sc.close();
		System.exit(0);
	}
}
