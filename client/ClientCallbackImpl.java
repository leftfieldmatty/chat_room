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
		parentIF.displayIncomingMsg(message, roomName);
	}

	public void addUserCB(String userName)
	{
	}
	
	public void removeUserCB(String userName)
	{
	}

	//addRoomCB
	//calls the addChat to the appropriate client interface
	public void addRoomCB(String roomName)
	{
		chat_diag.addChat(roomName);
	}
	
	public void removeRoomCB(String roomName)
	{
	}
	
	//joinRoomCB
	//passes the incoming user and room to the client interface for the GUIs
	public void joinRoomCB(String userName, String roomName)
	{
		parentIF.joinLocalRoom(userName, roomName);
	}
	
	//leaveRoomCB
	//passes the leaving user and room to the client interface for the GUIs
	public void leaveRoomCB(String userName, String roomName)
	{
		parentIF.leaveLocalRoom(userName, roomName);
	}
	
	//userJoinRoomCB
	//adds the username to the room, passes it to the GUI
	public void userJoinRoomCB(String userName, String roomName)
	{
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
