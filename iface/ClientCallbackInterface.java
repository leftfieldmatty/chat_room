package iface;

public interface ClientCallbackInterface extends java.rmi.Remote {

	public void messageCB(String incomingMsg) throws java.rmi.RemoteException;
	
	public void addUserCB(String userName) throws java.rmi.RemoteException;
	public void removeUserCB(String userName) throws java.rmi.RemoteException;
	public void addRoomCB(String roomName) throws java.rmi.RemoteException;
	public void removeRoomCB(String roomName) throws java.rmi.RemoteException;
	public void joinRoomCB(String userName, String roomName) throws java.rmi.RemoteException;
	public void leaveRoomCB(String userName, String roomName) throws java.rmi.RemoteException;
}
