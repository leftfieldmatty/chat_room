package client;

import java.util.ArrayList;
import java.util.List;


//clientMain
//entry point for the client code
public class clientMain {

	String userName;
	
	public static void main(String[] args) 
	{
		Thread interfaceThread = new Thread(new ClientInterface());
		interfaceThread.start();
	}
	
}
