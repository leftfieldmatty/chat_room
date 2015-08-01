package client;
import iface.Chatroom;

import java.rmi.RMISecurityManager;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

//ClientInterface
//client side interface thread
public class ClientInterface implements Runnable{

	ClientCallbackImpl clientCB;
	Chatroom clientChatroom;
	
	
	public void run() {
           try {
			clientCB = new ClientCallbackImpl();
			//clientGUI = new ChatroomGUI();
			//clientGUI.connectIF(this);
			ChatroomCMD clientCMD = new ChatroomCMD();
			clientCMD.connectIF(this);
			this.login("chedister", "1234");
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
           
	}
	
	public void login(String username, String password)
	{
		//CHEDITS:  may have to move the registry stuff around
		try
        {
		   Registry registry = LocateRegistry.getRegistry("localhost",4446);
           clientChatroom = (Chatroom) registry.lookup( "ChatroomTest");         //objectname in registry
           System.out.println("****CLIENT  ClientInterface, calling registerCBs");
           int ret = clientChatroom.verifyUser(username, password);
           System.out.println("****CLIENT  verification return is " + ret);
           
           if (ret == 1)
           {
           clientChatroom.registerCBs(username, clientCB); 
           clientChatroom.getInitialClientsAndRooms(username);

           //int retval = clientChatroom.addUser("xanatos", "abcd");
           //clientChatroom.userRoomJoin("chedister", "MATH 101");
           //clientChatroom.userRoomLeave("chedister", "MATH 101");
           //clientChatroom.addRoom("MATH 101");
           //clientChatroom.removeRoom("MATH 101");
           //clientChatroom.message("you got the right stuff", "MATH 101", "junk");
           //clientChatroom.signOutUser("chedister");
           }
        }
        catch (Exception e)
        {
           System.out.println("****CLIENT  ClientInterface exception: " + e.getMessage());
           e.printStackTrace();
        } 
	}
	
	void sendMessage(String message, String userName, String roomName) throws RemoteException
	{
		clientChatroom.message(message, userName, roomName);
	}
	
	void leaveRoom(String roomName, String userName) throws RemoteException
	{
		clientChatroom.userRoomLeave(roomName, userName);
	}
	
	void joinRoom(String roomName, String userName) throws RemoteException
	{
		clientChatroom.userRoomJoin(roomName, userName);
	}
	
	void createRoom(String roomName) throws RemoteException
	{
		clientChatroom.addRoom(roomName);
	}
	
	void destroyRoom(String roomName) throws RemoteException
	{
	    clientChatroom.removeRoom(roomName);	
	}
	
}
