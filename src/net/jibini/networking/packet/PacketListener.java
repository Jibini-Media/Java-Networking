package net.jibini.networking.packet;

import net.jibini.networking.connection.Connection;

/**
 * Listens for packets received from connection.
 * 
 * @author Zach Goethel
 */
public abstract class PacketListener
{
	/**
	 * Method called by connection on packet received.
	 * 
	 * @param packet Packet received by connection.
	 * @param connection Connection that received the packet.
	 */
	public abstract void onPacketReceived(Packet packet, Connection connection);
}
