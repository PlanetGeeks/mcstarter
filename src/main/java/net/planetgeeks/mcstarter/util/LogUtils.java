package net.planetgeeks.mcstarter.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import lombok.Getter;
import lombok.NonNull;

public class LogUtils
{
	@Getter
	public static Logger log = getFormattedLogger("[MC_STARTER]");
	
	/**
	 * Get a logger with the default McStarter Log formatter.
	 * 
	 * @param name - the logger name.
	 * @return a Logger.
	 */
	public static Logger getFormattedLogger(@NonNull String name)
	{
		Logger logger = Logger.getLogger(name);

		logger.setUseParentHandlers(false);
		logger.addHandler(getConsoleHandler());

		return logger;
	}

	private static ConsoleHandler getConsoleHandler()
	{
		ConsoleHandler handler = new ConsoleHandler();

		handler.setFormatter(new Formatter());

		return handler;
	}

	/**
	 * The default McStarter Log Formatter.
	 
	 * @author Flood2d
	 */
	public static class Formatter extends java.util.logging.Formatter
	{
		@Override
		public String format(LogRecord record)
		{
			StringBuffer sb = new StringBuffer();

			sb.append(getFormattedDate(record.getMillis()));
			sb.append(String.format(" %s - ", record.getLevel()));
			sb.append(formatMessage(record));
			sb.append("\n");
			
			return sb.toString();
		}
		
		private static String getFormattedDate(long millis)
		{
			return new SimpleDateFormat("[yyyy-mm-dd hh:mm:ss]").format(new Date(millis));
		}
	}
}
