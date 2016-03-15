package net.jibini.networking.chatroom.packet;

import net.jibini.networking.packet.Packet;

/**
 * Contains data about new chat messages.
 * 
 * @author Zach Goethel
 */
public class PacketChatMessage extends Packet
{
	private static final long serialVersionUID = 5382378724363876311L;

	/**
	 * User from whom the packet originated.
	 */
	public String username;
	
	/**
	 * Message sent by the user.
	 */
	public String message;
	
	/**
	 * Stores new packet data.
	 * 
	 * @param username User from whom the packet originated.
	 * @param message Message sent by the user.
	 */
	public PacketChatMessage(String username, String message)
	{
		this.username = username;
		this.message = message;
	}
}
