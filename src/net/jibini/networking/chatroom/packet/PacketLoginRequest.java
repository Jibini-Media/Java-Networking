package net.jibini.networking.chatroom.packet;

import net.jibini.networking.packet.Packet;

/**
 * Packet sent by client to request login.
 * 
 * @author Zach Goethel
 */
public class PacketLoginRequest extends Packet
{
	private static final long serialVersionUID = 1499074203931631202L;
	
	/**
	 * Requested username for connection.
	 */
	public String username;
	
	/**
	 * Stores new packet data.
	 * 
	 * @param username Requested username for connection.
	 */
	public PacketLoginRequest(String username)
	{
		this.username = username;
	}
}
