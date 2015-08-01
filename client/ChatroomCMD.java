package client;

public class ChatroomCMD {

	private ClientInterface myClientIF;
	
	public void connectIF(ClientInterface incomingIF)
	{
		myClientIF = incomingIF;
	}
}
