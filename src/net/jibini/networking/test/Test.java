package net.jibini.networking.test;

import net.jibini.networking.Connection;
import net.jibini.networking.server.Server;
import net.jibini.networking.server.SubServer;

/**
 * Main class for testing the API.
 */
public class Test
{
	/**
	 * Starting method for testing.
	 * 
	 * @param args standard argument array.
	 */
	public static void main(String[] args)
	{
		Server testServer = new Server(4);
		
		for (int i = 0; i < 64; i ++)
		{
			int leastFull = testServer.findLeastFull();
			System.out.println(leastFull);
			SubServer sub = testServer.getSubServers()[leastFull];
			sub.addConnection(new Connection(null));
		}
	}
}
