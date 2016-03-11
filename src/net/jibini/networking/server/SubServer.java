package net.jibini.networking.server;

/*
 * Handles a portion of connections on a separate thread.
 */
public class SubServer
{
	/*
	 * Server that owns the sub-server.
	 */
	private Server parentServer;
	
	/*
	 * Sets private variables.
	 * 
	 * @param parentServer Parent server of the sub-server.
	 */
	public SubServer(Server parentServer)
	{
		this.parentServer = parentServer;
	}
	
	/*
	 * Server that owns the sub-server.
	 * 
	 * @return Parent server of the sub-server.
	 */
	public Server getParentServer()
	{
		return parentServer;
	}
}
