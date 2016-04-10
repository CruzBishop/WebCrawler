package info.zthings.crawler.common;

import info.zthings.crawler.classes.ENCLOSIONS;
import info.zthings.crawler.classes.interfaces.IImplodable;

import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public class Util {
	private Util() {}
	
	public static String formatDate(String str) {
		Calendar c = Calendar.getInstance();
		str = str.replaceAll("%d%", String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
		str = str.replaceAll("%mo%", String.valueOf(c.get(Calendar.MONTH+1))); //cause january = 0
		str = str.replaceAll("%y%", String.valueOf(c.get(Calendar.YEAR)));
		str = str.replaceAll("%h%", String.valueOf(c.get(Calendar.HOUR_OF_DAY)));
		str = str.replaceAll("%mi%", String.valueOf(c.get(Calendar.MINUTE)));
		str = str.replaceAll("%s%", String.valueOf(c.get(Calendar.SECOND)));
		return str;
	}
	
	public static String implode(Iterable<?> list, String glue, ENCLOSIONS encloser) {
		Iterator<?> it = list.iterator();
		
		StringBuilder sb = new StringBuilder();
		
		sb.append(ENCLOSIONS.getOpening(encloser));
		
		while (it.hasNext()) {
			Object o = it.next();
			
			if (!(o instanceof Iterable)) sb.append(o);
			else sb.append(implode((Iterable<?>) o, glue, encloser));
			
			sb.append(glue);
		}
		
		sb.substring(0, sb.length()-glue.length()); //remove last bit of glue
		
		sb.append(ENCLOSIONS.getClosing(encloser));
		
		return sb.toString();
	}
	public static String implode(Object[] array, String glue, ENCLOSIONS encloser) {
		StringBuilder sb = new StringBuilder();
		sb.append(ENCLOSIONS.getOpening(encloser));
		for (Object o : array) {
			sb.append(o.toString());
			sb.append(glue);
		}
		sb.substring(0, sb.length()-glue.length()); //remove last bit of glue
		sb.append(ENCLOSIONS.getClosing(encloser));
		return sb.toString();
	}
	public static String implode(Map<?, ?> map, String glue, ENCLOSIONS encloser) {
		StringBuilder sb = new StringBuilder();
		sb.append(ENCLOSIONS.getOpening(encloser));
		for (Entry<?, ?> en : map.entrySet()) {
			sb.append(en.getKey() + "=" + en.getValue() + glue);
		}
		sb.substring(0, sb.length()-glue.length()); //remove last bit of glue
		sb.append(ENCLOSIONS.getClosing(encloser));
		return sb.toString();
	}
	public static String implode(IImplodable obj, String glue, ENCLOSIONS encloser) {
		StringBuilder sb = new StringBuilder();
		sb.append(ENCLOSIONS.getOpening(encloser));
		sb.append(obj.implode(glue));
		sb.substring(0, sb.length()-glue.length()); //remove last bit of glue
		sb.append(ENCLOSIONS.getClosing(encloser));
		return sb.toString();
	}
}
