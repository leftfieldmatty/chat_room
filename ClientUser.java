import iface.ClientCallbackInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

//ClientUser
//this class represents a ClientUser, and contains information such as
//the name, password, and online status
//also has hooks for the callback functions on the Client side
public class ClientUser extends UnicastRemoteObject
implements ServerCallbackInterface{

	private String clientName;
	private String password;
	private boolean online;
	private ClientCallbackInterface clientCallbackObj;
	
	//Constructor
	//saves the username, password
	public ClientUser(String cName, String pword) throws RemoteException 
	   {
		 super( );	
			clientName = cName;
			password = pword;
			online = false;
	   }

	//saves off the callback interface for the client side
	public void registerForClientCB(ClientCallbackInterface clientCBObj) throws RemoteException 
	{
		clientCallbackObj = clientCBObj;		
	}

	
	public void unregisterForClientCB(ClientCallbackInterface clientCBObj) throws RemoteException 
	{

	}
	   
	//doMessageCB
	//calls the message callback to the appropriate client
	public void doMessageCB(String incomingMsg, String roomName) throws RemoteException
	{
		clientCallbackObj.messageCB(incomingMsg, roomName);
	}

	//doAddUserCB
	//calls the add user callback to the appropriate client
	public void doAddUserCB(String userName) throws RemoteException
	{
		clientCallbackObj.addUserCB(userName);
	}
	
	//doRemoveUserCB
	//calls the remove user callback to the appropriate client
	public void doRemoveUserCB(String userName) throws RemoteException
	{
		clientCallbackObj.removeUserCB(userName);
	}
	
	//doAddRoomCB
	//calls the add room callback to the appropriate client
	public void doAddRoomCB(String roomName) throws RemoteException
	{
		clientCallbackObj.addRoomCB(roomName);
	}
	
	//doRemoveRoomCB
	//calls the remove room callback to the appropriate client
	public void doRemoveRoomCB(String roomName) throws RemoteException
	{
		clientCallbackObj.removeRoomCB(roomName);
	}
	
	//doJoinRoomCB
	//calls the join room callback to the appropriate client
	public void doJoinRoomCB(String userName, String roomName) throws RemoteException
	{
		clientCallbackObj.joinRoomCB(userName, roomName);
	}
	
	//doLeaveRoomCB
	//calls the leave room callback to the appropriate client
	public void doLeaveRoomCB(String userName, String roomName) throws RemoteException
	{
		clientCallbackObj.leaveRoomCB(userName, roomName);
	}
	
	//getName
	//returns the name of the client user
	public String getName()
	{
		return clientName;
	}
	
	//getPW
	//returns the saved password of the client
	public String getPW()
	{
		return password;
	}
	
	//setOnline
	//sets the online/offline status of the user
	public void setOnline(boolean status)
	{
		online = status;
	}
	
	//getOnline
	//returns the online/offline status of the client
	public boolean getOnline()
	{
		return online;
	}
}
