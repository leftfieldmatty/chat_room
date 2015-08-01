package client;

import java.util.ArrayList;
import java.util.List;


//clientMain
//entry point for the client code
public class clientMain {

	String userName;
	List<String> availRooms = new ArrayList<String>();
	List<String> currentRooms = new ArrayList<String>();
	//static ClientInterface ChatInterface = new ClientInterface();
	//List<ChatroomGUI> ChatGUI = new ArrayList<ChatroomGUI>;
	//ChatroomImpl chatroom = new ChatroomImp();
	
	public static void main(String[] args) 
	{
		System.out.println("****CLIENT  Goodbye World");	
		Thread interfaceThread = new Thread(new ClientInterface());
		interfaceThread.start();
	}
	
	public void createInterface()
	{
		
	}
	
	public void createGUI()
	{
		
	}
	
	public void signUp(String userName, String password)
	{
		
	}
	
}
