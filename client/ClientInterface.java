package client;
import iface.Chatroom;

import java.rmi.NotBoundException;
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
	ChatListDialog list_diag;
	String myName;
	
	
	public void run() {
           try {
			clientCB = new ClientCallbackImpl();
			ChatroomGUI clientGUI = new ChatroomGUI();
			clientGUI.connectIF(this);
			log_diag = new LoginDialog();
			log_diag.registerInterface(this);
			reg_diag = new RegisterDialog();
			reg_diag.registerInterface(this);
			list_diag = new ChatListDialog();
			list_diag.registerInterface(this);
			
			clientCB.registerListDiag(list_diag);
			//clientCB.registerChatDiag(chat_diag);
			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
           
	}
	
	public void login(String hostname, String username, String password)
	{
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
           list_diag.setVisible(true);
           myName = username;

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
	
	public void addNewUser(String hostname, String userName, String password)
	{
		try {
			Registry registry = LocateRegistry.getRegistry(hostname,4446);
			clientChatroom = (Chatroom) registry.lookup( "ChatroomTest");
		
		
			int ret = clientChatroom.addUser(userName, password);
			if(ret == -1)
			{
				reg_diag.displayError("Username already exists");
			}
			else
			{
				myName = userName;
				//TODO:  call up list GUI
			}
		} catch (Exception e) {
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
	
	void joinRoom(String roomName) throws RemoteException
	{
		clientChatroom.userRoomJoin(myName, roomName);
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
