import java.util.ArrayList;
import java.util.List;


//ClientRoom
//this class represents a chatroom, and contains a name, and list of 
//current users
public class ClientRoom {

	private String roomName;
	private List currentUsers;
	
	//Constructor
	//initializes the list of users and saves the room name
	public ClientRoom(String rName)
	{
		currentUsers = new ArrayList();
		roomName = rName;
	}
	
	//addUser
	//add's a user name to the list of users in the room
	public void addUser(String addedUser)
	{
		currentUsers.add(addedUser);
	}
	
	//removeUser
	//removes the user name from the list of users in the room
	public void removeUser(String removedUser)
	{
		currentUsers.remove(removedUser);
	}
	
	//getName
	//returns the name of the chatroom
	public String getName()
	{
		return roomName;
	}
	
	//getUsers
	//returns the list of strings of the users in the room
	public List getUsers()
	{
		return currentUsers;
	}
}
