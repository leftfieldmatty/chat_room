import java.rmi.RemoteException;

//ServerInterface
//thread that kicks off the ChatroomImpl class
public class ServerInterface implements Runnable{
	
	public void run() {
		try 
		{
			ChatroomImpl serverImpl = new ChatroomImpl();
			//ServerCallbackImpl serverCBImpl = new ServerCallbackImpl();
			//serverImpl.connectCB(serverCBImpl);
		} 
		catch (RemoteException e) 
		{
			e.printStackTrace();
		}
		
	}

	
}
