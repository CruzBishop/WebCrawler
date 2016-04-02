package info.zthings.crawler.common;

import info.zthings.crawler.commands.Command;
import info.zthings.crawler.commands.CommandHandler;
import info.zthings.crawler.commands.ParameterException;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

public class ConsoleUI {
	private static Scanner sc = new Scanner(System.in);
	private static PrintStream f;
	
	public static void main(String[] args) {
		ConsoleUI.out("CRAWLER v" + Ref.VER);
		ConsoleUI.out(Ref.SEP.substring(0, 9+Ref.VER.length()));
		
		ConsoleUI.out("Initializing...");
		try {
			f = new PrintStream("log.txt");
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		CommandHandler.init();
		Memory.init();
		ConsoleUI.out("Done!");
		
		for (String c : args) {
			ConsoleUI.out("Recieved command \"" + c + "\" through command-line arguments");
			Command cmd = CommandHandler.getCommand(c);
			if (cmd == null) {
				ConsoleUI.out("Unreconized command: " + c);
				continue;
			}
			cmd.execute(new String[] {c});
		}
		
		ConsoleUI.out("Enter command:");
		
		while (true) {
			ConsoleUI.outN("[] ");
			
			String cmd = sc.nextLine();
			
			ConsoleUI.outF("Recieved command: " + cmd);
			
			if (cmd.matches("exit") || cmd.matches("exit .*")) terminate();
			
			try {
				CommandHandler.parseCommand(cmd.split(" "));
			} catch (ParameterException e) {
				ConsoleUI.out(e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void terminate() {
		sc.close();
		System.exit(0);
	}
	
	public static void out(Object s) {
		System.out.println(s);
	}
	/** No newline */
	public static void outN(Object s) {
		System.out.print(s);
	}
	
	public static void warn(Object s) {
		System.out.println("WARNING: " + s);
	}
	
	
	public static void outF(Object s) {
		f.println(s);
	}
	public static void warnF(Object s) {
		f.println(Ref.SEP);
		f.println(s);
		f.println(Ref.SEP);
	}
}
