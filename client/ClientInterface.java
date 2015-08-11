package client;
import iface.Chatroom;

import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//ClientInterface
//client side interface thread
public class ClientInterface implements Runnable{

	ClientCallbackImpl clientCB;
	Chatroom serverChatroom;
	LoginDialog log_diag;
	RegisterDialog reg_diag;
	ChatListDialog list_diag;
	String myName;
	private List currentRooms;   // list of all chatrooms
	private Iterator roomIterator; // iterator that's used to go through the client users

	
	//run
	//thread that sets up the object, and all GUIs
	public void run() {
           try {
			clientCB = new ClientCallbackImpl();
			log_diag = new LoginDialog();
			log_diag.registerInterface(this);
			reg_diag = new RegisterDialog();
			reg_diag.registerInterface(this);
			list_diag = new ChatListDialog();
			list_diag.registerInterface(this);
			
			currentRooms = new ArrayList();
			
			clientCB.registerListDiag(list_diag);
			clientCB.registerParentIF(this);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
           
	}
	
	//login
	//takes the hostname, username, and password, and attempts to connect to 
	//the server and create an instance of the RMI object
	//If successful, calls the RMI's verifyUser function
	public void login(String hostname, String username, String password)
	{
		try
        {
		   Registry registry = LocateRegistry.getRegistry(hostname,4446);
           serverChatroom = (Chatroom) registry.lookup( "ChatroomTest");
           
           int ret = serverChatroom.verifyUser(username, password);
           if (ret == 1)// success
           {
           serverChatroom.registerCBs(username, clientCB);
           serverChatroom.getInitialClientsAndRooms(username);
           log_diag.setVisible(false);
           list_diag.setVisible(true);
           log_diag.clearFields();
           myName = username;
           list_diag.setTitle("ChatList - " + myName);

           }
           if(ret == 2)// bad password
           {
        	   log_diag.displayError("Password doesn't match");
           }
           if(ret == -1)// username not found
           {
        	   log_diag.displayError("Username not found");
           }
        }
        catch (Exception e)
        {
        	log_diag.displayError(e);
        } 
	}
	
	//addNewUser
	//takes the hostname, username, and password, and attempts to connect to 
	//the server and create an instance of the RMI object
	//If successful, calls the RMI's addUser function
	public void addNewUser(String hostname, String userName, String password)
	{
		try {
			Registry registry = LocateRegistry.getRegistry(hostname,4446);
			serverChatroom = (Chatroom) registry.lookup( "ChatroomTest");
			
			int ret = serverChatroom.addUser(userName, password);

			if(ret == 1)
			{
	           serverChatroom.registerCBs(userName, clientCB);
	           serverChatroom.getInitialClientsAndRooms(userName);
	           reg_diag.setVisible(false);
	           list_diag.setVisible(true);
	           reg_diag.clearFields();
			   myName = userName;
			   list_diag.setTitle("ChatList - " + myName);
			}
			if(ret == -1)
			{
				reg_diag.displayError("Username already exists");
			}
		} catch (Exception e) {
			reg_diag.displayError(e);
		}
	}
	
	//showRegisterGUI
	//shows the registerGUI, and hides the login GUI
	void showRegisterGUI()
	{
		log_diag.setVisible(false);
		reg_diag.setVisible(true);
	}
	
	//showLoginGUI
	//shows the login GUI, and hides the register GUI
	void showLoginGUI()
	{
		reg_diag.setVisible(false);
		log_diag.setVisible(true);
	}
	
	//sendMessage
	//sends the incoming message to the RMI chatroom object
	void sendMessage(String message, String roomName) throws RemoteException
	{
		serverChatroom.message(message, roomName, myName);
	}
	
	//leaveRoom
	//calls the RMI's userRoomLeave function with a room and user
	void leaveRoom(String roomName, String userName) throws RemoteException
	{
		serverChatroom.userRoomLeave(userName, roomName);
	}
	
	//joinRoom
	//calls the RMI's userRoomJoin function with a room and user
	void joinRoom(String roomName) throws RemoteException
	{
		serverChatroom.userRoomJoin(myName, roomName);
	}
	
	//createRoom
	//calls the RMI's addRoom function with the room name
	void createRoom(String roomName) throws RemoteException
	{
		serverChatroom.addRoom(roomName);
	}
	
	//destroyRoom
	//calls the RMI's removeRoom function with the room name
	void destroyRoom(String roomName) throws RemoteException
	{
	    serverChatroom.removeRoom(roomName);	
	}
	
	//joinUserLocalRoom
	//adds a user to a chatroom
	void joinUserLocalRoom(String userName, String roomName)
	{
		roomIterator = currentRooms.iterator();
		while(roomIterator.hasNext())
		{
			ChatRoomDialog tempRoom = (ChatRoomDialog)roomIterator.next();
			if(tempRoom.getName().equals(roomName))
			{
				tempRoom.addUser(userName);
			}
		}
	}
	
	//joinLocalRoom
	//creates a new chatroom GUI for the room being joined
	void joinLocalRoom(String userName, String roomName)
	{
		if(userName.equals(myName))
		{
			roomIterator = currentRooms.iterator();
			boolean alreadyInRoom = false;
			while(roomIterator.hasNext())
		    {
			    ChatRoomDialog room = (ChatRoomDialog)roomIterator.next();
			    if (room.getName().equals(roomName))
			    {
				    alreadyInRoom = true;
			    }
		    }
			
			if(!alreadyInRoom)
			{
				ChatRoomDialog tempRoom = new ChatRoomDialog(roomName, myName);
				tempRoom.registerInterface(this);
				currentRooms.add(tempRoom);
				try {
					serverChatroom.requestRoomUsers(myName, roomName);
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//leaveLocalRoom
	//destroys the chatroom GUI
	void leaveLocalRoom(String userName, String roomName)
	{
		roomIterator = currentRooms.iterator();
		while(roomIterator.hasNext())
	    {
		    ChatRoomDialog room = (ChatRoomDialog)roomIterator.next();
		    if (room.getName().equals(roomName))
		    {
		    	if(myName.equals(userName))
		    	{
		    		room.setVisible(false);
		    		roomIterator.remove();
		    		room = null;
		    		break;
		    	}
		    	else
		    	{
		    		room.removeUser(userName);
		    	}
		    }
	    }
	}
	
	//logoff
	//logs the user off and exits the program
	int logoff()
	{
		try {
			serverChatroom.signOutUser(myName);
			list_diag.removeAllRooms();
			list_diag.setVisible(false);
			log_diag.setVisible(true);
			serverChatroom = null;
		} catch (RemoteException e) {
			e.printStackTrace();
			return 0;
		}
		return 1;
	}
	
	//displayIncomingMsg
	//passes the incoming message to the appropriate GUI
	void displayIncomingMsg(String msg, String rName)
	{
		roomIterator = currentRooms.iterator();
		while(roomIterator.hasNext())
	    {
		    ChatRoomDialog room = (ChatRoomDialog)roomIterator.next();
		    if (room.getName().equals(rName))
		    {
		    	room.displayIncomingMsg(msg);
			    break;
		    }
	    }
	}
}
