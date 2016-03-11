package net.jibini.networking.server;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.jibini.networking.Connection;

/**
 * Handles a portion of connections on a separate thread.
 */
public class SubServer
{
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
}
