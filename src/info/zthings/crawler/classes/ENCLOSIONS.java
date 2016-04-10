package info.zthings.crawler.classes;

public enum ENCLOSIONS {
	SQUARE, CURLY, ROUND, NONE;
	
	public static String getOpening(ENCLOSIONS encloser) {
		switch (encloser) {
			case CURLY:
				return "{";
			case ROUND:
				return "(";
			case SQUARE:
				return "[";
			case NONE:
				return "";
			default:
				throw new EnumUpdateException();
		}
	}
	
	public static String getClosing(ENCLOSIONS encloser) {
		switch (encloser) {
			case CURLY:
				return "}";
			case ROUND:
				return ")";
			case SQUARE:
				return "]";
			case NONE:
				return "";
			default:
				throw new EnumUpdateException();
		}
	}
}
