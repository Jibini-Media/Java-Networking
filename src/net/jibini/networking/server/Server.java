package net.jibini.networking.server;

import java.net.ServerSocket;
import net.jibini.networking.Connection;

/**
 * Central class for handling client connections.
 */
public class Server
{
	/**
	 * Server socket for accepting connections.
	 */
	private ServerSocket serverSocket;
	
	/**
	 * Number of sub-servers to be used.
	 */
	private int subServerCount;
	
	/**
	 * Server for accepting new connections.
	 */
	private ReceptionServer receptionServer;
	
	/**
	 * Array of sub-servers in use.
	 */
	private SubServer[] subServers;
	
	/**
	 * Sets up server objects and settings.
	 * 
	 * @param subServerCount Number of sub-servers to be used.
	 */
	public Server(int subServerCount)
	{
		this.subServerCount = subServerCount;
		receptionServer = new ReceptionServer(this);
		subServers = new SubServer[subServerCount];
		for (int i = 0; i < subServerCount; i++)
			subServers[i] = new SubServer(this);
	}
	
	/**
	 * Number of sub-servers to be used.
	 * 
	 * @return How many sub-servers are in use.
	 */
	public int getSubServerCount()
	{
		return subServerCount;
	}
	
	/**
	 * Finds least full sub-server index.
	 * 
	 * @return Index of sub-server with the least connections.
	 */
	public int findLeastFull()
	{
		int least = -1;
		int result = -1;
		
		for (int i = 0; i < subServerCount; i ++)
		{
			int subServerConn = subServers[i].getConnectionCount();
			
			if (subServerConn < least || least == -1)
			{
				least = subServerConn;
				result = i;
			}
		}
		
		return result;
	}
	
	/**
	 * Handles new connections from reception.
	 * 
	 * @param connection Connection to assign to sub-server.
	 */
	public void handleNewConnection(Connection connection)
	{
		
	}
	
	/**
	 * Server socket for accepting connections.
	 * 
	 * @return Server's communication socket.
	 */
	public ServerSocket getServerSocket()
	{
		return serverSocket;
	}
	
	/**
	 * Server for accepting new connections.
	 * 
	 * @return Connection reception server instance.
	 */
	public ReceptionServer getReceptionServer()
	{
		return receptionServer;
	}
	
	/**
	 * Array of sub-servers in use.
	 * 
	 * @return Array containing all sub-servers.
	 */
	public SubServer[] getSubServers()
	{
		return subServers;
	}
}
