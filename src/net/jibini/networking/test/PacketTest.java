package net.jibini.networking.test;

import net.jibini.networking.packet.Packet;

/**
 * Packet for testing server and client.
 * 
 * @author Zach Goethel
 */
public class PacketTest extends Packet
{
	private static final long serialVersionUID = 6705398233684603492L;
	
	/**
	 * Data the test packet carries.
	 */
	public String payload;
}
