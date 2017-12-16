/*
 * GuiExecutor.java
 *
 * Created on May 11, 2007, 11:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.swing;

import java.util.List;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
/**
 *
 * @author bnevins
 */
public class GuiExecutor 
{
	private GuiExecutor()
	{
	}

	///////////////////////////////////////////////////////////////////////////
	
	public static GuiExecutor instance()
	{
		return instance;
	}

	///////////////////////////////////////////////////////////////////////////
	
	public void execute(Runnable r)
	{
		if(SwingUtilities.isEventDispatchThread())
			r.run();
		else
			SwingUtilities.invokeLater(r);
	}

	///////////////////////////////////////////////////////////////////////////
	
	private static final GuiExecutor instance = new GuiExecutor();
}
