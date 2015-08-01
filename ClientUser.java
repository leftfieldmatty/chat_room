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
	
	public ClientUser(String cName, String pword) throws RemoteException 
	   {
		 super( );	
			clientName = cName;
			password = pword;
			online = false;
	   }

	public void registerForClientCB(ClientCallbackInterface clientCBObj) throws RemoteException 
	{
		clientCallbackObj = clientCBObj;		
	}

	public void unregisterForClientCB(ClientCallbackInterface clientCBObj) throws RemoteException 
	{

	}
	   
	public void doMessageCB(String incomingMsg) throws RemoteException
	{
		clientCallbackObj.messageCB(incomingMsg);
	}

	public void doAddUserCB(String userName) throws RemoteException
	{
		System.out.println("****SERVER  inside doAddUserCB, about to send out " + userName);
		if(clientCallbackObj == null)
		{
			System.out.println("clientCallbackObj is null");
		}
		else
		{
			System.out.println("clientCallbackObj is not null");
		}
		clientCallbackObj.addUserCB(userName);
	}
	
	public void doRemoveUserCB(String userName) throws RemoteException
	{
		System.out.println("****SERVER  inside doRemoveUserCB, about to send out " + userName);
		if(clientCallbackObj == null)
		{
			System.out.println("clientCallbackObj is null");
		}
		else
		{
			System.out.println("clientCallbackObj is not null");
		}
		clientCallbackObj.removeUserCB(userName);
	}
	
	public void doAddRoomCB(String roomName) throws RemoteException
	{
		clientCallbackObj.addRoomCB(roomName);
	}
	
	public void doRemoveRoomCB(String roomName) throws RemoteException
	{
		clientCallbackObj.removeRoomCB(roomName);
	}
	
	public void doJoinRoomCB(String userName, String roomName) throws RemoteException
	{
		clientCallbackObj.joinRoomCB(userName, roomName);
	}
	
	public void doLeaveRoomCB(String userName, String roomName) throws RemoteException
	{
		clientCallbackObj.leaveRoomCB(userName, roomName);
	}
	
	public String getName()
	{
		return clientName;
	}
	
	public String getPW()
	{
		return password;
	}
	
	public void setOnline(boolean status)
	{
		online = status;
	}
	
	public boolean getOnline()
	{
		return online;
	}
}
