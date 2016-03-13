package net.jibini.networking.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.jibini.networking.connection.Connection;
import net.jibini.networking.connection.ConnectionListener;
import net.jibini.networking.packet.PacketListener;

/**
 * Central class for handling client connections.
 * 
 * @author Zach Goethel
 */
public class Server
{
	/**
	 * Server's connection listener (user-defined).
	 */
	private ConnectionListener connectionListener = null;
	
	/**
	 * Server's packet listener for all connections (user-defined).
	 */
	private PacketListener packetListener = null;
	
	/**
	 * Server socket for accepting connections.
	 */
	private ServerSocket serverSocket;
	
	/**
	 * Port on which the server operates.
	 */
	private int port;
	
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
	 * List of all connections to server.
	 */
	private List<Connection> connections;
	
	/**
	 * Sets up server objects and settings.
	 * 
	 * @param port Port on which the server should operate.
	 * @param subServerCount Number of sub-servers to be used.
	 */
	public Server(int port, int subServerCount)
	{
		this.port = port;
		this.subServerCount = subServerCount;
		subServers = new SubServer[subServerCount];
		connections = new CopyOnWriteArrayList<Connection>();
	}
	
	/**
	 * Starts all IO operations of server.
	 * 
	 * @throws IOException If an IOException occurs in startup.
	 */
	public void start() throws IOException
	{
		serverSocket = new ServerSocket(port);
		receptionServer = new ReceptionServer(this);
		for (int i = 0; i < subServerCount; i++)
			subServers[i] = new SubServer(this);
		receptionServer.startAccepting();
		for (int i = 0; i < subServerCount; i ++)
			subServers[i].start();
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
		connections.add(connection);
		int leastFull = findLeastFull();
		SubServer subServer = subServers[leastFull];
		subServer.addConnection(connection);
		if (connectionListener != null)
			connectionListener.onConnection(connection, subServer);
		connection.setPacketListener(packetListener);
	}
	
	/**
	 * Handles disconnections from sub-servers.
	 * 
	 * @param connection Disconnection to be handled.
	 */
	public void handleDisconnection(Connection connection, SubServer parent)
	{
		connections.remove(connection);
		parent.removeConnection(connection);
		if (connectionListener != null)
			connectionListener.onDisconnection(connection, parent);
	}
	
	/**
	 * Sets the connection listener to the user's.
	 * 
	 * @param listener New connection listener to assign.
	 */
	public void setConnectionListener(ConnectionListener listener)
	{
		connectionListener = listener;
	}
	
	/**
	 * Sets the packet listener on all connections to the user's.
	 * 
	 * @param listener New packet listener to apply to all connections.
	 */
	public void setPacketListener(PacketListener listener)
	{
		packetListener = listener;
		for (Connection connection : connections)
			connection.setPacketListener(listener);
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
