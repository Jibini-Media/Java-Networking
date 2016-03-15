package net.jibini.networking.chatroom;

import net.jibini.networking.connection.Connection;

/**
 * Stores server-side data on each user.
 * 
 * @author Zach Goethel
 */
public class ChatUser
{
	/**
	 * Username the user is assigned.
	 */
	public String username;
	
	/**
	 * Connection leading to the user.
	 */
	public Connection connection;
	
	/**
	 * Sets chat user profile data.
	 * 
	 * @param username Username the user is assigned.
	 * @param connection Connection leading to the user.
	 */
	public ChatUser(String username, Connection connection)
	{
		this.username = username;
		this.connection = connection;
	}
}
