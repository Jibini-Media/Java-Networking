package net.jibini.networking.chatroom;

import java.io.IOException;
import java.net.Socket;
import net.jibini.networking.connection.Connection;
import net.jibini.networking.connection.ConnectionListener;
import net.jibini.networking.packet.Packet;
import net.jibini.networking.packet.PacketListener;
import net.jibini.networking.server.Server;
import net.jibini.networking.server.SubServer;

/**
 * Allows clients to come and chat.
 * 
 * @author Zach Goethel
 */
public class ChatServer
{
	/**
	 * Server for managing connections to chatroom.
	 */
	private Server chatServer;
	
	/**
	 * Sets up the server and starts operations.
	 * 
	 * @param args Standard application argument array.
	 */
	public void start(String[] args)
	{
		// Parses arguments (-port and -subServers).
		int port = -1;
		int subServers = -1;
		
		for (int i = 0; i < args.length; i ++)
		{
			String arg = args[i];
			
			if (arg.equals("-port"))
			{
				String portString = args[i + 1];
				port = Integer.valueOf(portString);
			} else if (arg.equals("-subServers"))
			{
				String subServersString = args[i + 1];
				subServers = Integer.valueOf(subServersString);
			}
		}
		
		// Sets up and starts the server object.
		initChatServer(port, subServers);
	}
	
	/**
	 * Sets up and starts the server object.
	 */
	private void initChatServer(int port, int subServers)
	{
		try
		{
			// Creats the server, sets the listeners and starts it.
			System.out.println("Starting server on port " + port + " with " + subServers + " sub-servers.");
			chatServer = new Server(port, subServers);
			chatServer.setConnectionListener(connectionListener);
			chatServer.setPacketListener(packetListener);
			chatServer.start();
		} catch (IOException ex)
		{
			// If something goes wrong while setting up, print it out.
			System.out.println("An error occurred while starting the server.");
			ex.printStackTrace();
		}
	}
	
	/**
	 * Handles new connections and disconnections.
	 */
	private ConnectionListener connectionListener = new ConnectionListener()
	{
		@Override
		public void onConnection(Connection connection, SubServer parent)
		{
			// Prints data about the new connection.
			Socket connectSocket = connection.getSocket();
			String ipAddress = connectSocket.getInetAddress().getHostAddress();
			System.out.println("A new client has connected from " + ipAddress);
		}

		@Override
		public void onDisconnection(Connection connection, SubServer parent)
		{
			// Prints data about the disconnection.
			Socket disconnectSocket = connection.getSocket();
			String ipAddress = disconnectSocket.getInetAddress().getHostAddress();
			System.out.println("The client at " + ipAddress + " has disconnected.");
		}
	};
	
	/**
	 * Handles incoming packets from all clients.
	 */
	private PacketListener packetListener = new PacketListener()
	{
		@Override
		public void onPacketReceived(Packet packet, Connection connection)
		{
			// Prints data on the received packet.
			Socket receiveSocket = connection.getSocket();
			String ipAddress = receiveSocket.getInetAddress().getHostAddress();
			System.out.println("A packet has been received from " + ipAddress);
		}
	};
	
	/**
	 * Externally visible instance of the chat server.
	 */
	public static ChatServer instance;
	
	/**
	 * Creates a chat server instance and starts it.
	 * 
	 * @param args Standard application argument array.
	 */
	public static void main(String[] args)
	{
		// Creates and starts a chat server instance.
		instance = new ChatServer();
		instance.start(args);
	}
}
