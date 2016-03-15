package net.jibini.networking.chatroom;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.jibini.networking.chatroom.packet.PacketLoginAcceptance;
import net.jibini.networking.chatroom.packet.PacketLoginRequest;
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
	 * List of profiles of connected chat users.
	 */
	private List<ChatUser> chatUsers;
	
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
		
		// Creates the chat user list.
		chatUsers = new CopyOnWriteArrayList<ChatUser>();
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
	 * Called when login request is received.
	 * 
	 * @param username Requested username for connection.
	 * @param connection Connection which received the packet.
	 * @throws IOException If response packet write fails.
	 */
	private void handleLoginRequest(String username, Connection connection) throws IOException
	{
		// Decides whether username is in use.
		boolean usernameUsed = false;
		for (ChatUser user : chatUsers)
			if (user.username.equals(username))
				usernameUsed = true;
		
		// Empty packet and host address.
		PacketLoginAcceptance acceptance;
		String hostAddress = connection.getSocket().getInetAddress().getHostAddress();
		
		// Replies to the client about acceptance.
		if (usernameUsed)
		{
			String reason = "Username is already in use.";
			acceptance = new PacketLoginAcceptance(false, reason);
			connection.sendPacket(acceptance);
			connection.disconnect();
			System.out.println(hostAddress + " denied for reason: " + reason);
		} else
		{
			acceptance = new PacketLoginAcceptance(true, null);
			connection.sendPacket(acceptance);
			ChatUser user = new ChatUser(username, connection);
			chatUsers.add(user);
			System.out.println(hostAddress + " accepted as " + username);
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
			// Prints connection data.
			String hostAddress = connection.getSocket().getInetAddress().getHostAddress();
			System.out.println("Connection from " + hostAddress);
		}

		@Override
		public void onDisconnection(Connection connection, SubServer parent)
		{
			// Prints disconnection data.
			String hostAddress = connection.getSocket().getInetAddress().getHostAddress();
			System.out.println(hostAddress + " disconnected.");
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
			try
			{
				// Handles packet based on types.
				if (packet instanceof PacketLoginRequest)
					handleLoginRequest(((PacketLoginRequest) packet).username, connection);
			} catch (Throwable thrown)
			{
				System.out.println("An error occurred while receiving a packet.");
				thrown.printStackTrace();
			}
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
