package net.jibini.networking.server;

/**
 * Central class for handling client connections.
 */
public class Server
{
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
