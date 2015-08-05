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
	LoginDialog log_diag;
	RegisterDialog reg_diag;
	
	
	public void run() {
           try {
			clientCB = new ClientCallbackImpl();
			//clientGUI = new ChatroomGUI();
			//clientGUI.connectIF(this);
			ChatroomGUI clientGUI = new ChatroomGUI();
			clientGUI.connectIF(this);
			log_diag = new LoginDialog();
			log_diag.registerInterface(this);
			reg_diag = new RegisterDialog();
			reg_diag.registerInterface(this);
			
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
           
	}
	
	public void login(String hostname, String username, String password)
	{
		//CHEDITS:  may have to move the registry stuff around
		try
        {
		   Registry registry = LocateRegistry.getRegistry(hostname,4446);
           clientChatroom = (Chatroom) registry.lookup( "ChatroomTest");         //objectname in registry
           int ret = clientChatroom.verifyUser(username, password);
           
           if (ret == 1)// success
           {
           clientChatroom.registerCBs(username, clientCB); 
           clientChatroom.getInitialClientsAndRooms(username);
           log_diag.setVisible(false);

           //int retval = clientChatroom.addUser("xanatos", "abcd");
           //clientChatroom.userRoomJoin("chedister", "MATH 101");
           //clientChatroom.userRoomLeave("chedister", "MATH 101");
           //clientChatroom.addRoom("MATH 101");
           //clientChatroom.removeRoom("MATH 101");
           //clientChatroom.message("you got the right stuff", "MATH 101", "junk");
           //clientChatroom.signOutUser("chedister");
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
	
	public void addNewUser(String userName, String password)
	{
		try {
			int ret = clientChatroom.addUser(userName, password);
			if(ret == -1)
			{
				reg_diag.displayError("Username already exists");
			}
			else
			{
				//TODO:  call up list GUI
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			reg_diag.displayError(e);
		}

	}
	
	void showRegisterGUI()
	{
		log_diag.setVisible(false);
		reg_diag.setVisible(true);
	}
	
	void showLoginGUI()
	{
		reg_diag.setVisible(false);
		log_diag.setVisible(true);
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
