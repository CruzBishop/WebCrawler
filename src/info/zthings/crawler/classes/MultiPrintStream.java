package info.zthings.crawler.classes;

import java.io.PrintStream;

public class MultiPrintStream {
	PrintStream[] streams;
	
	public MultiPrintStream(PrintStream ... streams) {
		this.streams = streams;
	}

	public void print(Object o) {
		for (PrintStream s : this.streams) {
			s.print(o);
		}
	}

	public void println(Object o) {
		for (PrintStream s : this.streams) {
			s.println(o);
		}
	}

	public void println() {
		for (PrintStream s : this.streams) {
			s.println();
		}
	}
	
}
