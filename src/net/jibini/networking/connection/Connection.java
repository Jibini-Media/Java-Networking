package net.jibini.networking.connection;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.jibini.networking.packet.Packet;
import net.jibini.networking.packet.PacketListener;
import net.jibini.networking.packet.PacketPing;

/**
 * Handles server-to-client or client-to-server connections.
 * 
 * @author Zach Goethel
 */
public class Connection
{
	/**
	 * Whether the connection is active.
	 */
	private boolean connected = false;
	
	/**
	 * Connection's packet listener (user-defined).
	 */
	private PacketListener packetListener = null;
	
	/**
	 * Socket for bi-directional communication.
	 */
	private Socket socket;
	
	/**
	 * Connection's object input stream.
	 */
	private ObjectInputStream input;
	
	/**
	 * Connection's buffered input stream.
	 */
	private BufferedInputStream bufInput;
	
	/**
	 * Connection's object output stream.
	 */
	private ObjectOutputStream output;
	
	/**
	 * Queue of packets for to send over connection.
	 */
	private List<Packet> sendQueue;
	
	/**
	 * Nanotime of last ping packet received.
	 */
	private long lastPingReceived = -1;
	
	/**
	 * Nanotime of last ping packet sent.
	 */
	private long lastPingSent = -1;
	
	/**
	 * Sets up connection to requested location.
	 * 
	 * @param socket Socket to use for all communication.
	 * @throws IOException if an error occurs while setting up a connection.
	 */
	public Connection(Socket socket) throws IOException
	{
		this.socket = socket;
		OutputStream outputStream = socket.getOutputStream();
		output = new ObjectOutputStream(new BufferedOutputStream(outputStream));
		output.writeObject("Flush");
		output.flush();
		InputStream inputStream = socket.getInputStream();
		bufInput = new BufferedInputStream(inputStream);
		input = new ObjectInputStream(bufInput);
		sendQueue = new CopyOnWriteArrayList<Packet>();
		connected = true;
	}
	
	/**
	 * Writes pending packets and checks input/status.
	 * 
	 * @throws IOException If an IO error occurrs while reading or writing.
	 * @throws ClassNotFoundException If read object class is not found.
	 */
	public void updateIO() throws IOException, ClassNotFoundException
	{
		long nano = System.nanoTime();
		if (lastPingReceived == -1)
			lastPingReceived = nano;
		long nanoSinceReceived = nano - lastPingReceived;
		double secondsSinceReceived = (double) nanoSinceReceived / 1000000000;
		long nanoSinceSent = nano - lastPingSent;
		double secondsSinceSent = (double) nanoSinceSent / 1000000000;
		
		if (secondsSinceReceived >= 2)
		{
			disconnect();
			return;
		}

		if (secondsSinceSent >= 0.5)
		{
			sendPacket(new PacketPing());
			lastPingSent = nano;
		}
		
		if (bufInput.available() != 0)
		{
			Object read = input.readObject();
			
			if (read instanceof Packet)
			{
				Packet packet = (Packet) read;
				if (packetListener != null)
					packetListener.onPacketReceived(packet, this);
				if (read instanceof PacketPing)
					lastPingReceived = nano;
			}
		}
		
		if (sendQueue.size() > 0)
		{
			Packet toSend = sendQueue.get(0);
			sendPacket(toSend);
			sendQueue.remove(0);
		}
	}
	
	/**
	 * Adds a packet to the queue of packets to send.
	 * 
	 * @param packet Packet to be sent over connection.
	 */
	public void queuePacket(Packet packet)
	{
		sendQueue.add(packet);
	}
	
	/**
	 * Skips the packet queue and sends a packet.
	 * 
	 * @param packet Packet to be sent over connection.
	 * @throws IOException If an error occurs while writing.
	 */
	public void sendPacket(Packet packet) throws IOException
	{
		output.writeObject(packet);
		output.flush();
	}
	
	/**
	 * Stops reading/writing and closes connection.
	 * 
	 * @throws IOException If closing streams fails.
	 */
	public void disconnect() throws IOException
	{
		connected = false;
		input.close();
		output.close();
		bufInput.close();
		socket.close();
	}
	
	/**
	 * Sets the packet listener to the user's.
	 * 
	 * @param listener New packet listener to assign.
	 */
	public void setPacketListener(PacketListener listener)
	{
		packetListener = listener;
	}
	
	/**
	 * Whether the connection is active.
	 * 
	 * @return Whether or not connection is reading/writing.
	 */
	public boolean isConnected()
	{
		return connected;
	}
	
	/**
	 * Socket for bi-directional communication.
	 * 
	 * @return Socket used for all communication.
	 */
	public Socket getSocket()
	{
		return socket;
	}
	
	/**
	 * Connection's object input stream.
	 * 
	 * @return Stream used to read data on connection.
	 */
	public ObjectInputStream getInputStream()
	{
		return input;
	}
	
	/**
	 * Connection's object output stream.
	 * 
	 * @return Stream used to write data on connection.
	 */
	public ObjectOutputStream getOutputStream()
	{
		return output;
	}
}
