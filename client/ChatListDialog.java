package client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

import javax.swing.*;

//class ChatListDialog
//This class is the dialog GUI that contains all available chatroom
//that can be joined
public class ChatListDialog extends JFrame{
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;
    static ClientInterface clientIF;
    static JList list;
    static DefaultListModel model;

    //Constructor
    //calls the GUI creation
    public ChatListDialog() {
    	 createAndShowGUI();

    }
    
    //addComponentsToPane
    //sets up the components of the GUI
    public static void addComponentsToPane(Container pane) {
        if (RIGHT_TO_LEFT) {
            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        }

        JLabel label;
        JButton logoutButton, joinButton, createButton;
		pane.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		if (shouldFill) {
		//natural height, maximum width
		c.fill = GridBagConstraints.HORIZONTAL;
		}
		
		label = new JLabel("Chatroom List:");
		if (shouldWeightX) {
		c.weightx = 0.5;
		}
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		pane.add(label, c);
		
		createButton = new JButton("Create");
	    c.fill = GridBagConstraints.HORIZONTAL;
	    c.weightx = 0.5;
	    c.gridx = 1;
	    c.gridy = 0;
	    pane.add(createButton, c);
		
		joinButton = new JButton("Join");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 2;
		c.gridy = 0;
		pane.add(joinButton, c);
		
		logoutButton = new JButton("Logout");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 0.5;
		c.gridx = 3;
		c.gridy = 0;
		pane.add(logoutButton,c);
	
		// fill data with chatrooms

		model = new DefaultListModel();
		list = new JList(model);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 40;      //make this component tall
		c.weightx = 0.0;
		c.gridwidth = 3;
		c.gridx = 0;
		c.gridy = 1;
		pane.add(list, c);
		
        // Process join button
        joinButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	
            	try {
					clientIF.joinRoom(model.getElementAt(list.getSelectedIndex()).toString());
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });

        // Process logout button
        logoutButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	int retval = clientIF.logoff();
            }
        });

        // Process create button
        createButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	JFrame frame = new JFrame("New Chatroom");
            	String name = JOptionPane.showInputDialog(frame, "New Chat name:");
            	try {
					clientIF.createRoom(name);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
    }
    
    
    //createAndShowGUI
    //creates and shows the GUI
    private void createAndShowGUI() {
    	//Create and set up the window.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set up the content pane.
        addComponentsToPane(getContentPane());
        //Display the window.
        setPreferredSize(new Dimension(400,200));
        pack();
        setVisible(false);
    }
    
    //addChat
    //adds a chatroom name to the list
    public void addChat(String roomName)
    {
        model.addElement(roomName);
    }
    
    //removeChat
    //removes a chatroom name from the list
    public void removeChat(String roomName)
    {
    	model.removeElementAt(model.indexOf(roomName));
    }
    
    //makeVisible
    //sets the visibility of the dialog to on or off
    public void makeVisible(boolean visible)
    {
    	setVisible(visible);
    }
    
    //removeAllRooms
    //removes all the rooms from the list
    public void removeAllRooms()
    {
    	model.removeAllElements();
    }
    
    //displayError
    //Displays any incoming errors
    public void displayError(Exception e)
    {
    	JFrame frame = new JFrame("Error");
    	JOptionPane.showMessageDialog(frame,
    		    "Error with connection: " +e.getMessage());
    }
    
    //displayError
    //Displays any incoming errors
    public void displayError(String error)
    {
    	JFrame frame = new JFrame("Error");
    	JOptionPane.showMessageDialog(frame,
    		    error);
    }

    //registerInterface
    //saves off the associated ClientInterface class instance
	public void registerInterface(ClientInterface incomingIF) 
	{
		clientIF = incomingIF;
	}
}