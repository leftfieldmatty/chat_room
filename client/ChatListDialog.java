package client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;

import javax.swing.*;

public class ChatListDialog extends JFrame{
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;
    static ClientInterface clientIF;
    static JList list;
    static DefaultListModel model;


    public ChatListDialog() {
    	 createAndShowGUI();
    }
    
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
			
		/*joinButton = new JButton("Join");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 0;       //reset to default
		c.weighty = 1.0;   //request any extra vertical space
		c.anchor = GridBagConstraints.PAGE_END; //bottom of space
		c.insets = new Insets(10,0,0,0);  //top padding
		c.gridx = 1;       //aligned with button 2
		c.gridwidth = 2;   //2 columns wide
		c.gridy = 2;       //third row
		pane.add(joinButton, c);
		*/
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
        /*logoutButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

            }
        });*/

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

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private void createAndShowGUI() {
    	//Create and set up the window.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set up the content pane.
        addComponentsToPane(getContentPane());
        //Display the window.
        pack();
        setVisible(false);
    }
    
    public void addChat(String roomName)
    {
        model.addElement(roomName);

    }
    
    public void removeChat(String roomName)
    {
    	model.removeElementAt(model.indexOf(roomName));
    }
    
    public void makeVisible(boolean visible)
    {
    	setVisible(visible);
    }
    
    public void displayError(Exception e)
    {
    	JFrame frame = new JFrame("Error");
    	JOptionPane.showMessageDialog(frame,
    		    "Error with connection: " +e.getMessage());
    }
    
    public void displayError(String error)
    {
    	JFrame frame = new JFrame("Error");
    	JOptionPane.showMessageDialog(frame,
    		    error);
    }

	public void registerInterface(ClientInterface incomingIF) 
	{
		clientIF = incomingIF;
	}
}