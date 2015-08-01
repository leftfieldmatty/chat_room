import java.util.ArrayList;
import java.util.List;

//serverMain
//entry point for the server side
public class serverMain {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("****SERVER  Hello World");
		
		Thread interfaceThread = new Thread(new ServerInterface());
		interfaceThread.start();
	}
}
