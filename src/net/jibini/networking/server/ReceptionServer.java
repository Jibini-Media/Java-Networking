package net.jibini.networking.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import net.jibini.networking.connection.Connection;

/**
 * Accepts new client connections.
 * 
 * @author Zach Goethel
 */
public class ReceptionServer
{
	/**
	 * Whether the reception server is accepting.
	 */
	private boolean accepting = false;
	
	/**
	 * Server that owns the reception server.
	 */
	private Server parentServer;
	
	/**
	 * Parent server's server socket.
	 */
	private ServerSocket serverSocket;
	
	/**
	 * Sets parent server and server socket.
	 * 
	 * @param parentServer Parent server of the sub-server.
	 */
	public ReceptionServer(Server parentServer)
	{
		this.parentServer = parentServer;
		serverSocket = parentServer.getServerSocket();
	}
	
	/**
	 * Starts accepting new connections.
	 */
	public void startAccepting()
	{
		accepting = true;
		receptionThread.start();
	}
	
	/**
	 * Stops accepting new connections.
	 */
	public void stopAccepting()
	{
		accepting = false;
	}
	
	/**
	 * Whether the reception server is accepting.
	 * 
	 * @return If the reception thread is running.
	 */
	public boolean isAccepting()
	{
		return accepting;
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
	
	/**
	 * Separate thread for accepting connections.
	 */
	private Thread receptionThread = new Thread(new Runnable()
	{
		@Override
		public void run()
		{
			while (accepting)
			{
				try
				{
					Socket accepted = serverSocket.accept();
					Connection conn = new Connection(accepted);
					parentServer.handleNewConnection(conn);
				} catch (IOException ex)
				{
					if (accepting)
					{
						System.out.println("An error occurred while setting up a new connection.");
						ex.printStackTrace();
					}
				}
			}
		}
	});
}
