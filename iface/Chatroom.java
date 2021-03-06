package iface;
import java.rmi.*;

public interface Chatroom extends java.rmi.Remote
{
	public int addUser(String userName, String password) throws RemoteException;
	
	public int addRoom(String roomName) throws RemoteException;
	
	public void removeUser(String userName) throws RemoteException;
	
	public void removeRoom(String roomName) throws RemoteException;
	
	public void message(String incomingMsg, String roomName, String userName) throws RemoteException;
	
	public void userRoomJoin(String userName, String roomName) throws RemoteException;
	
	public void userRoomLeave(String userName, String roomName) throws RemoteException;
	
	public int verifyUser(String userName, String password) throws RemoteException;
	
	public void signOutUser(String userName) throws RemoteException;
	
	public void getInitialClientsAndRooms(String userName) throws RemoteException;
	
	public void requestRoomUsers(String userName, String roomName) throws RemoteException;
	
	public void registerCBs(String userName, ClientCallbackInterface CBI) throws RemoteException;
	
}
