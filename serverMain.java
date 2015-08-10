import java.util.ArrayList;
import java.util.List;

//serverMain
//entry point for the server side
public class serverMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Thread interfaceThread = new Thread(new ServerInterface());
		interfaceThread.start();
	}
}
