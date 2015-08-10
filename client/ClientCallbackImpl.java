package client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import iface.ClientCallbackInterface;

//ClientCallbackImpl
//client side callback interface
public class ClientCallbackImpl extends UnicastRemoteObject
implements ClientCallbackInterface{
ClientCallbackImpl clientCB;
ChatListDialog chat_diag;
ClientInterface parentIF;
	
	//Constructor
	public ClientCallbackImpl() throws RemoteException {
	      super( );
	   }
	
	//messageCB
	//takes the incoming message and sends it to the appropriate chatroom GUI
	public void messageCB(String message, String roomName)
	{
		System.out.println("****CLIENT  messageCB hit, incoming message is " + message);
		parentIF.displayIncomingMsg(message, roomName);
	}

	public void addUserCB(String userName)
	{
		System.out.println("****CLIENT  addUserCB hit, incoming userName is " + userName);
	}
	
	public void removeUserCB(String userName)
	{
		System.out.println("****CLIENT  removeUserCB hit, incoming userName is " + userName);
	}

	//addRoomCB
	//calls the addChat to the appropriate client interface
	public void addRoomCB(String roomName)
	{
		System.out.println("****CLIENT  addRoomCB hit, incoming roomName is " + roomName);
		chat_diag.addChat(roomName);
	}
	
	public void removeRoomCB(String roomName)
	{
		System.out.println("****CLIENT  removeRoomCB hit, incoming roomName is " + roomName);
	}
	
	//joinRoomCB
	//passes the incoming user and room to the client interface for the GUIs
	public void joinRoomCB(String userName, String roomName)
	{
		System.out.println("****CLIENT  joinRoomCB hit, incoming userName is " + userName + " and roomName is " + roomName);
		parentIF.joinLocalRoom(roomName);
	}
	
	//leaveRoomCB
	//passes the leaving user and room to the client interface for the GUIs
	public void leaveRoomCB(String userName, String roomName)
	{
		System.out.println("****CLIENT  leaveRoomCB hit, incoming userName is " + userName + " and roomName is " + roomName);
		parentIF.leaveLocalRoom(userName, roomName);
	}
	
	//userJoinRoomCB
	//adds the username to the room, passes it to the GUI
	public void userJoinRoomCB(String userName, String roomName)
	{
		System.out.println("****CLIENT  userJoinRoomCB hit, incoming userName is " + userName + " and roomName is " + roomName);
		parentIF.joinUserLocalRoom(userName, roomName);
	}
	
	//registerListDiag
	//registers the list of chatroom windows
	public void registerListDiag(ChatListDialog incomingDiag)
	{
		chat_diag = incomingDiag;
	}
	
	//registerParentIF
	//registers the associated ClientInterface object
	public void registerParentIF(ClientInterface incomingIF)
	{
		parentIF = incomingIF;
	}


	
}
