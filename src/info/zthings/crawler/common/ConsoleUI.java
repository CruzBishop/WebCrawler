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
		
		try {
			f = new PrintStream("log.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		CommandHandler.init();
		Memory.init();
		
		for (String c : args) {
			ConsoleUI.out("Recieved command \"" + c + "\" through command-line arguments");
			
			String[] b;
			if (c.indexOf("]") > c.indexOf("[")) {
				String[] p = c.substring(c.indexOf("[")+1, c.indexOf("]")).split(",");
				ConsoleUI.out("Recieved params [" + Util.implode(p, ",") + "]");
				c = c.substring(0, c.indexOf("["));
				
				b = new String[p.length+1];
				b[0] = c;
				for (int i=0; i<p.length; i++) {
					b[1+i] = p[i];
				}
			} else {
				b = new String[] {c};
			}
			
			Command cmd = CommandHandler.getCommand(c);
			if (cmd == null) {
				ConsoleUI.out("Unreconized command: " + c);
				continue;
			}
			cmd.execute(b);
		}
		
		//Entering main loop
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

	public static void terminate() {
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
	public static void err(String s) {
		System.out.println("ERROR: " + s);
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
