package net.jibini.networking.test;

import java.net.Socket;
import net.jibini.networking.connection.Connection;
import net.jibini.networking.connection.ConnectionListener;
import net.jibini.networking.discovery.BeatListener;
import net.jibini.networking.discovery.Heart;
import net.jibini.networking.packet.Packet;
import net.jibini.networking.packet.PacketListener;
import net.jibini.networking.server.Server;
import net.jibini.networking.server.SubServer;

/**
 * Main class for testing the API.
 * 
 * @author Zach Goethel
 */
public class Test
{
	/**
	 * Starting method for testing.
	 * 
	 * @param args Standard argument array.
	 * @throws Throwable If anything goes wrong (do not do this).
	 */
	public static void main(String[] args) throws Throwable
	{
		// testServerAndClients();
		testHeartbeatAndListener();
	}
	
	/**
	 * Creates a test server and infinite test clients.
	 * 
	 * @throws Throwable If anything goes wrong (do not do this).
	 */
	public static void testServerAndClients() throws Throwable
	{
		ConnectionListener connectionListener = new ConnectionListener()
		{
			@Override
			public void onConnection(Connection connection, SubServer parent)
			{
				System.out.println("New connection!");
				System.out.print(connection);
				System.out.print(" on ");
				System.out.println(parent);
			}
		};
		
		PacketListener packetListener = new PacketListener()
		{
			@Override
			public void onPacketReceived(Packet packet, Connection connection)
			{
				System.out.println("Packet received!");
				System.out.print(connection);
				System.out.print(" received ");
				System.out.println(packet);
			}
		};
		
		Server testServer = new Server(25566, 4);
		testServer.setConnectionListener(connectionListener);
		testServer.setPacketListener(packetListener);
		testServer.start();
		
		while (true)
		{
			Socket socket = new Socket("127.0.0.1", 25566);
			Connection connection = new Connection(socket);
			connection.sendPacket(new PacketTest());
			Thread.sleep(100);
		}
	}
	
	/**
	 * Starts a heartbeat and listens for it.
	 * 
	 * @throws Throwable If anything goes wrong (do not do this).
	 */
	public static void testHeartbeatAndListener() throws Throwable
	{
		PacketTest beat = new PacketTest();
		beat.payload = "ServIP=192.168.X.XXX";
		Heart heart = new Heart("229.5.38.17", 25567, beat);
		heart.start();
		
		BeatListener beatListener = new BeatListener("229.5.38.17", 25567)
		{
			@Override
			public void onHeartBeat(Packet beat)
			{
				System.out.print("Received heartbeat: ");
				System.out.println(((PacketTest) beat).payload);
			}
		};
		
		beatListener.start();
	}
}
