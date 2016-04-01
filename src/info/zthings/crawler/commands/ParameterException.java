package info.zthings.crawler.commands;

public class ParameterException extends RuntimeException {
		private static final long serialVersionUID = -62975215326790L;

		public ParameterException(Command thi) {
			super("Invalid arguments! Run \"help " + thi.getName() + "\" for help");
		}
}