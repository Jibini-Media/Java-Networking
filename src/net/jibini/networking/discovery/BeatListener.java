package net.jibini.networking.discovery;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import net.jibini.networking.packet.Packet;

/**
 * Listens for heartbeat broadcasts.
 * 
 * @author Zach Goethel
 */
public abstract class BeatListener
{
	/**
	 * Name of the multicast group.
	 */
	private String groupName;
	
	/**
	 * Port on which to listen for heartbeats.
	 */
	private int port;
	
	/**
	 * Socket for multicast listening.
	 */
	private MulticastSocket multicastSocket;
	
	/**
	 * Prepares to listen for beats.
	 * 
	 * @param groupName Name of the multicast group.
	 * @param port Port to liston on for beats.
	 * @throws IOException If multicast setup fails.
	 */
	public BeatListener(String groupName, int port) throws IOException
	{
		this.groupName = groupName;
		this.port = port;
		multicastSocket = new MulticastSocket(port);
		multicastSocket.joinGroup(InetAddress.getByName(groupName));
	}
	
	/**
	 * Starts the thread to listen for heartbeats.
	 */
	public void start()
	{
		listeningThread.start();
	}
	
	/**
	 * Reads packet from a datagram packet.
	 * 
	 * @param datagram Datagram packet from which to read data.
	 * @return Packet contained by the datagram packet.
	 */
	private Packet getPacketFromDatagram(DatagramPacket datagram)
	{
		byte[] data = datagram.getData();
		
		if (data != null)
		{
			try
			{
				ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
				Packet beat = (Packet) objectInputStream.readObject();
				objectInputStream.close();
				return beat;
			} catch (IOException ex)
			{
				System.out.println("An error occurred while reading a heartbeat.");
				ex.printStackTrace();
			} catch (ClassNotFoundException ex)
			{
				System.out.println("An error occurred while casting the beat.");
				ex.printStackTrace();
			}
		}
		
		return null;
	}
	
	/**
	 * Name of the multicast group.
	 * 
	 * @return User-assigned name of the multicast group.
	 */
	public String getGroupName()
	{
		return groupName;
	}
	
	/**
	 * Port on which to listen.
	 * 
	 * @return Port on which to listen for heartbeats.
	 */
	public int getPort()
	{
		return port;
	}
	
	/**
	 * Separate thread for listening for heartbeats.
	 */
	private Thread listeningThread = new Thread(new Runnable()
	{
		@Override
		public void run()
		{
			while (true)
			{
				try
				{
					DatagramPacket datagramPacket = new DatagramPacket(new byte[1500], 1500);
					multicastSocket.receive(datagramPacket);
					Packet beat = getPacketFromDatagram(datagramPacket);
					onHeartBeat(beat);
				} catch (Exception ex)
				{
					System.out.println("An error occurred while checking multicast.");
				}
			}
		}
	});
	
	/**
	 * Called when a hearbeat is heard.
	 * 
	 * @param beat The detected heartbeat packet.
	 */
	public abstract void onHeartBeat(Packet beat);
}
