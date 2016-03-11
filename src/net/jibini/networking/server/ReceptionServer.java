package net.jibini.networking.server;

/*
 * Accepts new client connections.
 */
public class ReceptionServer
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
	public ReceptionServer(Server parentServer)
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
