package net.jibini.networking.server;

import java.net.SocketException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.jibini.networking.connection.Connection;

/**
 * Handles a portion of connections on a separate thread.
 * 
 * @author Zach Goethel
 */
public class SubServer
{
	/**
	 * Whether the sub-server is managing connections.
	 */
	private boolean running = false;
	
	/**
	 * Server that owns the sub-server.
	 */
	private Server parentServer;
	
	/**
	 * List of connections belonging to sub-server.
	 */
	private List<Connection> connections;
	
	/**
	 * Creates connection list and stores parent.
	 * 
	 * @param parentServer Parent server of the sub-server.
	 */
	public SubServer(Server parentServer)
	{
		this.parentServer = parentServer;
		connections = new CopyOnWriteArrayList<Connection>();
	}
	
	/**
	 * Starts sub-server handling connections.
	 */
	public void start()
	{
		running = true;
		handlingThread.start();
	}
	
	/**
	 * Stops sub-server from handling connections.
	 */
	public void stop()
	{
		running = false;
	}
	
	/**
	 * Adds a connection to be managed.
	 * 
	 * @param connection Connection to add to be managed.
	 */
	public void addConnection(Connection connection)
	{
		connections.add(connection);
	}
	
	/**
	 * Removes a connection that is being managed.
	 * 
	 * @param connection Connection to remove from being managed.
	 */
	public void removeConnection(Connection connection)
	{
		connections.remove(connection);
	}
	
	/**
	 * Whether the sub-server is managing connections.
	 * 
	 * @return If the sub-server threads are active.
	 */
	public boolean isRunning()
	{
		return running;
	}
	
	/**
	 * Number of connections currently being managed.
	 * 
	 * @return Amount of connections being managed.
	 */
	public int getConnectionCount()
	{
		return connections.size();
	}
	
	/**
	 * Server that owns the sub-server.
	 * 
	 * @return Parent server of the sub-server.
	 */
	public Server getParentServer()
	{
		return parentServer;
	}
	
	/**
	 * Separate thread for handling connections.
	 */
	private Thread handlingThread = new Thread(new Runnable()
	{
		@Override
		public void run()
		{
			while (running)
			{
				for (Connection connection : connections)
					try
					{
						if (connection.isConnected())
							connection.updateIO();
						else
							parentServer.handleDisconnection(connection, SubServer.this);
					} catch (Throwable thrown)
					{
						if (!(thrown instanceof SocketException))
						{
							System.out.println("An error occurred while updating connection IO.");
							thrown.printStackTrace();
						}
					}
				
				try
				{
					Thread.sleep(5);
				} catch (InterruptedException ex)
				{
					System.out.println("An error occurred in a looped sleep.");
					ex.printStackTrace();
				}
			}
		}
	});
}
