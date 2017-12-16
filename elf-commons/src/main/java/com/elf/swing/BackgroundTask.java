/*
 * BackgroundTask.java
 *
 * Created on May 12, 2007, 11:45 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.elf.swing;

/**
 *
 * @author bnevins
 */
/*
 * BackgroundTask.java
 *
 * Created on May 12, 2007, 1:07 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */


import java.util.concurrent.*;

/**
 *
 * @author bnevins
 */
public abstract class BackgroundTask<V> implements Runnable, Future<V>
{
	// must be implemented
	// called in the background thread
	protected abstract V compute() throws Exception;
	
	///////////////////////////////////////////////////////////////////////////
	//    called in the EDT -- designed to be overridden  
	protected void onCompletion(V result, Throwable exception, boolean cancelled)
	{
		
	}

	///////////////////////////////////////////////////////////////////////////
	//    called in the EDT -- designed to be overridden 
	protected void onProgress(int current, int max)
	{
		
	}
	
	///////////////////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////////////////

	protected final void done()
	{
		GuiExecutor.instance().execute(new Runnable()
		{
			public void run()
			{
				V value = null;
				Throwable thrown = null;
				boolean cancelled = false;
				
				try
				{
					value = get();
				}
				catch(ExecutionException e)
				{
					thrown = e.getCause();
				}
				catch(CancellationException e)
				{
					cancelled = true;
				}
				catch(InterruptedException consumed)
				{
					// ignore
				}
				finally
				{
					onCompletion(value, thrown, cancelled);
				}
			}
		}
		);
	}
	protected void setProgress(final int current, final int max)
	{
		GuiExecutor.instance().execute(new Runnable()
		{
			public void run()
			{
				onProgress(current, max);
			}
		});
	}
	
	public boolean cancel(boolean mayInterruptIfRunning)
	{
		return computation.cancel(mayInterruptIfRunning);
	}

	public boolean isCancelled()
	{
		return computation.isCancelled();
	}

	public boolean isDone()
	{
		return computation.isDone();
	}

	public V get() throws InterruptedException, ExecutionException
	{
		return computation.get();
	}

	public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException
	{
		return computation.get(timeout, unit);
	}

	public void run()
	{
		computation.run();
	}
	
	///////////////////////////////////////////////////////////////////////////

	private class Computation extends FutureTask<V>
	{
		public Computation()
		{
			super(new Callable<V>()
			{
				public V call() throws Exception
				{
					return BackgroundTask.this.compute();
				}
			} );
		}
	}

	///////////////////////////////////////////////////////////////////////////

	private final FutureTask<V> computation = new Computation();
}
