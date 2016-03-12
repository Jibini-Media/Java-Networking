package net.jibini.networking.connection;

import net.jibini.networking.server.SubServer;

/**
 * Listens for new connections to server.
 * 
 * @author Zach Goethel
 */
public abstract class ConnectionListener
{
	/**
	 * Method called by server on new connection.
	 * 
	 * @param connection New connection from client.
	 * @param parent Assigned sub-server of connection.
	 */
	public abstract void onConnection(Connection connection, SubServer parent);
}
