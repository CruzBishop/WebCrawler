package info.zthings.crawler.common;

public class Util {
	private Util() {}
	
	public static String implode(String[] array, String glue) {
		String buffer = "";
		for (int i=0; i<array.length; i++) {
			if (i>0) {
				buffer += glue + array[i];
			} else buffer += array[i];
		}
		return buffer;
	}

	public static String implode(Object[] array, String glue) {
		String[] buffer = new String[array.length];
		
		for (int i=0; i<array.length; i++) {
			buffer[i] = array[i].toString();
		}
		
		return implode(buffer, glue);
	}
}
