package net.jibini.networking.chatroom.packet;

import net.jibini.networking.packet.Packet;

/**
 * Sent by server telling whether login accepted.
 * 
 * @author Zach Goethel
 */
public class PacketLoginAcceptance extends Packet
{
	private static final long serialVersionUID = -830149053419256721L;

	/**
	 * Whether the server accepted the login.
	 */
	public boolean accepted;
	
	/**
	 * Reason if server denied the login.
	 */
	public String reason;
	
	/**
	 * Stores new packet data.
	 */
	public PacketLoginAcceptance(boolean accepted, String reason)
	{
		this.accepted = accepted;
		this.reason = reason;
	}
}
