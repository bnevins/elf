/*
 * Reporter.java
 *
 * Created on November 13, 2004, 8:07 PM
 */

package com.elf.util.logging;

import java.util.logging.*;

/**
 *
 * @author  bnevins
 */
public class Reporter
{
	///////////////////////////////////////////////////////////////////////////
	////////         Convenience methods        ///////////////////////////////
	///////////////////////////////////////////////////////////////////////////
	
	public final static void finest(String s)
	{ logger.finest(s); }
	public final static void finest(String s, Object o)
	{ logger.log(Level.FINEST, s, new Object[] { o }); }
	public final static void finest(String s, Object o1, Object o2)
	{ logger.log(Level.FINEST, s, new Object[] { o1, o2 }); }
	public final static void finer(String s)
	{ logger.finer(s); }
	public final static void finer(String s, Object o)
	{ logger.log(Level.FINER, s, new Object[] { o }); }
	public final static void finer(String s, Object o1, Object o2)
	{ logger.log(Level.FINER, s, new Object[] { o1, o2 }); }
	public final static void fine(String s)
	{ logger.fine(s); }
	public final static void fine(String s, Object o)
	{ logger.log(Level.FINE, s, new Object[] { o }); }
	public final static void fine(String s, Object o1, Object o2)
	{ logger.log(Level.FINE, s, new Object[] { o1, o2 }); }
	public final static void info(String s)
	{ logger.info(s); }
	public final static void info(String s, Object o)
	{ logger.log(Level.INFO, s, new Object[] { o }); }
	public final static void info(String s, Object o1, Object o2)
	{ logger.log(Level.INFO, s, new Object[] { o1, o2 }); }
	public final static void warning(String s)
	{ logger.warning(s); }
	public final static void warning(String s, Object o)
	{ logger.log(Level.WARNING, s, new Object[] { o }); }
	public final static void warning(String s, Object o1, Object o2)
	{ logger.log(Level.WARNING, s, new Object[] { o1, o2 }); }
	public final static void severe(String s)
	{ logger.severe(s); }
	public final static void severe(String s, Object o)
	{ logger.log(Level.SEVERE, s, new Object[] { o }); }
	public final static void severe(String s, Object o1, Object o2)
	{ logger.log(Level.SEVERE, s, new Object[] { o1, o2 }); }
	
	///////////////////////////////////////////////////////////////////////////
	
	public final static void setLevel(Level newLevel)
	{
		// the public final should cause this to be inlined...
		logger.setLevel(newLevel);
		
		/* test logging messages
		String me = System.getProperty("user.name");
		if(me != null && me.equals("bnevins"))
		{
			logger.finest("finest");
			logger.finer("finer");
			logger.fine("fine");
			logger.info("info");
			logger.warning("warning");
			logger.severe("severe");
		}
		 **/
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	public final static Logger get()
	{
		// the final should cause this to be inlined...
		return logger;
	}
	
	
	///////////////////////////////////////////////////////////////////////////
	
	public static void main(String[] args)
	{
		Reporter.setLevel(Level.FINE);
		Reporter.warning("test");
		Reporter.warning("test", "some-arg-here");
		Reporter.warning("test", "some-arg-here", "another-arg-here");
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private Reporter()
	{
	}
	
	///////////////////////////////////////////////////////////////////////////
	
	private static			Logger	logger			= null;
	private static final	String	LOGGER_NAME		= "com.elf";
	private static final	Level	DEFAULT_LEVEL	= Level.INFO;
	
	///////////////////////////////////////////////////////////////////////////
	
	static
	{
		try
		{
			// attach a handler that will at least be capable of spitting out FINEST messages
			// the Level of the Logger itself will determine what the handler actually gets...
			Handler h = new ConsoleHandler();
			h.setLevel(Level.FINEST);
			
			logger = Logger.getLogger(LOGGER_NAME, "com.elf.util.logging.LocalStrings");
			
			logger.addHandler(h);
			logger.setLevel(DEFAULT_LEVEL);
			logger.setUseParentHandlers(false);	// this will stop double-logging to the console...
		}
		catch(Throwable t)
		{
			try
			{
				logger = Logger.getLogger(LOGGER_NAME);
				logger.severe("Couldn't create Logger with a resource bundle.  Created a Logger without a Resource Bundle.");
			}
			catch(Throwable t2)
			{
				// now what?
			}
		}
	}
}

