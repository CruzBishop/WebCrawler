package info.zthings.crawler.common;

import info.zthings.crawler.commands.CommandHandler;

import java.util.Scanner;

public class ConsoleUI {
	private static Scanner sc = new Scanner(System.in);
	
	public static void enterCmdLoop() {
		while (true) {
			System.out.print("[] ");
			String cmd = sc.nextLine();
			if (cmd.matches("exit ?.*")) terminate();
			CommandHandler.parseCommand(cmd.split(" "));
		}
	}

	public static Scanner getScanner() {
		return sc;
	}

	public static void terminate() {
		sc.close();
		System.exit(0);
	}
}
