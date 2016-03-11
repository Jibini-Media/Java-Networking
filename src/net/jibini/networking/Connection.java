package net.jibini.networking;

import java.net.Socket;

/**
 * Handles server-to-client or client-to-server connections.
 */
public class Connection
{
	/**
	 * Socket for bi-directional communication.
	 */
	private Socket socket;
	
	/**
	 * Sets up connection to requested location.
	 * 
	 * @param socket Socket to use for all communication.
	 */
	public Connection(Socket socket)
	{
		this.socket = socket;
	}
	
	/**
	 * Socket for bi-directional communication.
	 * 
	 * @return Socket used for all communication.
	 */
	public Socket getSocket()
	{
		return socket;
	}
}
