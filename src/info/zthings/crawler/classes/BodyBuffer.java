package info.zthings.crawler.classes;

import info.zthings.crawler.common.Ref;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;

public class BodyBuffer {
	private PrintStream s;
	private String b = "";
	private ArrayList<String> r = new ArrayList<String>();

	public BodyBuffer(String loc) {
		try {
			this.s = new PrintStream(loc + "_body.buff");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		Calendar c = Calendar.getInstance();
		s.println("CRAWLED AT " + c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR) + " " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND));
		s.println(Ref.SEP);
		s.println();
	}

	public void println(String str) {
		this.s.println(str);
		b += str + "\n";
		r.add(str);
	}
	
	public String getCompleteBody() {
		return b;
	}
	
	public ArrayList<String> getRows() {
		return r;
	}
}
