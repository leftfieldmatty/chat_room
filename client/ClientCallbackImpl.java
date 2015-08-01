package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import iface.ClientCallbackInterface;

//ClientCallbackImpl
//client side callback interface
public class ClientCallbackImpl extends UnicastRemoteObject
implements ClientCallbackInterface{
ClientCallbackImpl clientCB;
	
	public ClientCallbackImpl() throws RemoteException {
	      super( );
			//clientCB = new ClientCallbackImpl();
	   }
	
	public void messageCB(String message)
	{
		System.out.println("****CLIENT  messageCB hit, incoming message is " + message);
	}
	
	public void addUserCB(String userName)
	{
		System.out.println("****CLIENT  addUserCB hit, incoming userName is " + userName);
	}
	
	public void removeUserCB(String userName)
	{
		System.out.println("****CLIENT  removeUserCB hit, incoming userName is " + userName);

	}
	
	public void addRoomCB(String roomName)
	{
		System.out.println("****CLIENT  addRoomCB hit, incoming roomName is " + roomName);

	}
	
	public void removeRoomCB(String roomName)
	{
		System.out.println("****CLIENT  removeRoomCB hit, incoming roomName is " + roomName);
	}
	
	public void joinRoomCB(String userName, String roomName)
	{
		System.out.println("****CLIENT  joinRoomCB hit, incoming userName is " + userName + " and roomName is " + roomName);
	}
	
	public void leaveRoomCB(String userName, String roomName)
	{
		System.out.println("****CLIENT  leaveRoomCB hit, incoming userName is " + userName + " and roomName is " + roomName);	
	}
	
	
}
