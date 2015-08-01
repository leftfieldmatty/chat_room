import iface.ClientCallbackInterface;

import java.rmi.*;

//ServerCallbackInterface
//top level interface for the serverside callback functions
public interface ServerCallbackInterface extends Remote {

	public void registerForClientCB(ClientCallbackInterface clientCBObj) throws java.rmi.RemoteException;
	
	public void unregisterForClientCB(ClientCallbackInterface clientCBObj) throws java.rmi.RemoteException;
	
}
