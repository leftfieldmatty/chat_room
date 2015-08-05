package client;
 
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class ChatListDialog extends JFrame {
	
	private static ChatListDialog instance_ =null;
	
	private ChatListDialog(String name) {
		super(name);
		setSize(400,400);
		setLocation(600,300);
		setVisible(true);
		//setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public static ChatListDialog getObj() {
		if(instance_==null) {
			instance_=new ChatListDialog("ChatListDialog");
		}
		return instance_;
	}
	
	public static void main( String[] args )
	{
		ChatListDialog.getObj().addWindowListener(new WindowListener() {
			public void windowClosed(WindowEvent e) {
                System.out.println("Window close event occur");
            }
            public void windowActivated(WindowEvent e) {
                System.out.println("Window Activated");
            }
            public void windowClosing(WindowEvent e) {
                System.out.println("Window Closing");
                //LoginDialog.getObj();
                ChatListDialog.getObj().dispose();
            }
            public void windowDeactivated(WindowEvent e) {
                System.out.println("Window Deactivated");
            }
            public void windowDeiconified(WindowEvent e) {
                System.out.println("Window Deiconified");
            }
            public void windowIconified(WindowEvent e) {
                System.out.println("Window Iconified");
            }
            public void windowOpened(WindowEvent e) {
                System.out.println("Window Opened");
            }
		});
	}
}
