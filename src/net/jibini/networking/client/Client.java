package net.jibini.networking.client;

import net.jibini.networking.connection.Connection;

/**
 * Handles a connection to a server.
 * 
 * @author Zach Goethel
 */
public class Client
{
	/**
	 * Whether the client is running.
	 */
	private boolean running = false;
	
	/**
	 * Connection being handled by client.
	 */
	private Connection connection;
	
	/**
	 * Sets client settings.
	 * 
	 * @param connection Connection to be handled.
	 */
	public Client(Connection connection)
	{
		this.connection = connection;
	}
	
	/**
	 * Begins managing the connection.
	 */
	public void start()
	{
		running = true;
		handlingThread.start();
	}
	
	/**
	 * Stops handling and closes connection.
	 */
	public void stop()
	{
		running = false;
	}
	
	/**
	 * Connection being handled by client.
	 * 
	 * @return Connection wrapped in client.
	 */
	public Connection getConnection()
	{
		return connection;
	}
	
	/**
	 * Separate thread for handling the connection.
	 */
	private Thread handlingThread = new Thread(new Runnable()
	{
		@Override
		public void run()
		{
			while (running)
			{
				try
				{
					connection.updateIO();
				} catch (Throwable thrown)
				{
					System.out.println("An error occurred while updating connection.");
					thrown.printStackTrace();
				}
				
				if (!connection.isConnected())
					stop();
			}
			
			try
			{
				connection.disconnect();
			} catch (Throwable thrown)
			{
				System.out.println("An error occurred while closing connection.");
				thrown.printStackTrace();
			}
		}
	});
}
