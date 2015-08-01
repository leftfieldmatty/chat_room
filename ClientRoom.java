import java.util.ArrayList;
import java.util.List;


//ClientRoom
//this class represents a chatroom, and contains a name, and list of 
//current users
public class ClientRoom {

	private String roomName;
	private List currentUsers;
	
	public ClientRoom(String rName)
	{
		currentUsers = new ArrayList();
		roomName = rName;
	}
	
	public void addUser(String addedUser)
	{
		currentUsers.add(addedUser);
	}
	
	public void removeUser(String removedUser)
	{
		currentUsers.remove(removedUser);
	}
	
	public String getName()
	{
		return roomName;
	}
	
	public List getUsers()
	{
		return currentUsers;
	}
}
