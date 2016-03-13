package net.jibini.networking.discovery;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import net.jibini.networking.packet.Packet;

/**
 * Server that transmits packets via multicast.
 * 
 * @author Zach Goethel
 */
public class Heart
{
	/**
	 * Name of the multicast group.
	 */
	private String groupName;
	
	/**
	 * Port on which to broadcast.
	 */
	private int port;
	
	/**
	 * Packet to be broadcasted.
	 */
	private Packet beat;
	
	/**
	 * Socket for multicast broadcasting.
	 */
	private MulticastSocket multicastSocket;
	
	/**
	 * Datagram packet holding beat data.
	 */
	private DatagramPacket datagramPacket;
	
	/**
	 * Sets up settings and socket.
	 * 
	 * @param groupName Name of the multicast group.
	 * @param port Port on which the heartbeat is broadcasted.
	 * @param beat Packet to be sent as a heartbeat.
	 * @throws IOException If an error occurs in setup.
	 */
	public Heart(String groupName, int port, Packet beat) throws IOException
	{
		this.groupName = groupName;
		this.port = port;
		this.beat = beat;
		multicastSocket = new MulticastSocket();
		setBeat(beat);
	}
	
	/**
	 * Starts the heartbeat thread.
	 */
	public void start()
	{
		beatingThread.start();
	}
	
	/**
	 * Broadcasts a beat via multicast.
	 * 
	 * @throws IOException If multicast broadcast fails.
	 */
	public void beat() throws IOException
	{
		multicastSocket.send(datagramPacket);
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
	 * Port on which to broadcast.
	 * 
	 * @return Port on which the heartbeat is broadcasted.
	 */
	public int getPort()
	{
		return port;
	}
	
	/**
	 * Packet to be broadcasted.
	 * 
	 * @return Packet being broadcasted as a heartbeat.
	 */
	public Packet getBeat()
	{
		return beat;
	}
	
	/**
	 * Sets the packet to be broadcasted.
	 * 
	 * @param beat New packet to be broadcasted as a heartbeat.
	 * @throws IOException If datagram packet creation fails.
	 */
	public void setBeat(Packet beat) throws IOException
	{
		this.beat = beat;
		InetAddress group = InetAddress.getByName(groupName);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
		objectOutputStream.writeObject(beat);
		objectOutputStream.flush();
		objectOutputStream.close();
		byte[] buf = byteArrayOutputStream.toByteArray();
		datagramPacket = new DatagramPacket(buf, buf.length, group, port);
	}
	
	/**
	 * Separate thread for broadcasting.
	 */
	private Thread beatingThread = new Thread(new Runnable()
	{
		@Override
		public void run()
		{
			while (true)
			{
				try
				{
					beat();
					Thread.sleep(500);
				} catch (IOException ex)
				{
					System.out.println("An error occurred while broadcasting beat.");
					ex.printStackTrace();
				} catch (InterruptedException ex)
				{
					System.out.println("An error occurred between heartbeats.");
					ex.printStackTrace();
				}
			}
		}
	});
}
