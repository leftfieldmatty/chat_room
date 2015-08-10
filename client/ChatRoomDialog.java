package client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;
import javax.swing.GroupLayout.*;

//class ChatRoomDialog
//This class is the GUI that is the actual chatroom
public class ChatRoomDialog extends JFrame
                           implements DocumentListener {
    final static boolean shouldFill = true;
    final static boolean shouldWeightX = true;
    final static boolean RIGHT_TO_LEFT = false;
    
    private JTextField entry;
    private JButton sendButton;
    private JButton leaveButton;
    private JLabel status;
//    private JTextArea textArea;
    private String roomName;
    private String myName;
    static ClientInterface clientIF;
    private ArrayList users;
    private Iterator userIterator;
    static JList userList; // users
    static DefaultListModel userModel;
    static JList chatlogList; // chat_log
    static DefaultListModel chatlogModel;
  
    final static Color  ERROR_COLOR = Color.PINK;
    final static String CANCEL_ACTION = "cancel-search";
    final static String SEND_ACTION = "send-message";
    
    final Color entryBg;    

    //Constructor
    //creates the dialog
    public ChatRoomDialog(String incomingName, String incomingUserName) {
        roomName = incomingName;
        myName = incomingUserName;
        initComponents();
        
        users = new ArrayList();

        entryBg = entry.getBackground();
        entry.getDocument().addDocumentListener(this);
        
        InputMap im = entry.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = entry.getActionMap();
        im.put(KeyStroke.getKeyStroke("ENTER"), SEND_ACTION);
        am.put(SEND_ACTION, new SendAction());
    }

    //initComponents
    //initializes all the pieces of the GUI
    private void initComponents() {
    	Container pane = getContentPane();

    	JButton button;
        JTextField textfield;
        JLabel label;

    	entry = new JTextField();
//		textArea = new JTextArea();
        status = new JLabel();
        sendButton = new JButton("Send");
        leaveButton = new JButton("Leave Room");
		userModel = new DefaultListModel();
		userList = new JList(userModel);
    	chatlogModel = new DefaultListModel();
    	chatlogList = new JList(chatlogModel);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent ev) {
        		dispose();
        	}
        });
        setTitle(roomName + " - " + myName);

        getContentPane().setLayout(new GridBagLayout());        
        GridBagConstraints c = new GridBagConstraints();
		if (shouldFill) {
    		//natural height, maximum width
	    	c.fill = GridBagConstraints.HORIZONTAL;
		}
        
/*        textArea.setColumns(20);
        textArea.setLineWrap(true);
        textArea.setRows(5);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        jScrollPane1 = new JScrollPane(textArea);
*/
		// Messages Label
    	label = new JLabel("Messages:");
    	if (shouldWeightX) {
    	c.weightx = 0.5;
    	}
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.weightx = 0.5;
    	c.gridx = 0;
    	c.gridy = 0;
    	pane.add(label, c);
       
    	// Leave Button
        c.weightx = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        pane.add(leaveButton, c);
        		
        // Members Label
    	label = new JLabel("Members:");
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.weightx = 0.5;
    	c.gridx = 2;
    	c.gridy = 0;
    	pane.add(label, c);

        // Chat Log List
        chatlogList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane jScrollPane2 = new JScrollPane();
		jScrollPane2.setViewportView(chatlogList);
        c.weightx = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 300;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        getContentPane().add(jScrollPane2,c );
		// scrolls to bottom of list	
		int lastIndex = chatlogList.getModel().getSize() - 1;
		if (lastIndex >= 0) {
			chatlogList.ensureIndexIsVisible(lastIndex);
		}
        
        // User List
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane jScrollPane1 = new JScrollPane();
		jScrollPane1.setViewportView(userList);
        c.weightx = 0.0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 300;
        c.gridx = 2;
        c.gridy = 1;
        getContentPane().add(jScrollPane1, c);
    	
    	// Entry Textbox
    	c.fill = GridBagConstraints.HORIZONTAL;
    	c.ipady = 0;       //reset to default
    	//c.weighty = 1.0;   //request any extra vertical space
    	c.anchor = GridBagConstraints.PAGE_END; //bottom of space
    	//c.insets = new Insets(10,0,0,0);  //top padding
    	c.gridx = 0;       //aligned with button 2
    	c.gridwidth = 2;   //2 columns wide
    	c.gridy = 2;       //third row
    	pane.add(entry, c);
        
    	// Send Button
        c.fill = GridBagConstraints.HORIZONTAL;
    	c.ipady = 0;       //reset to default
    	//c.weighty = 1.0;   //request any extra vertical space
    	c.anchor = GridBagConstraints.PAGE_END; //bottom of space
    	//c.insets = new Insets(10,0,0,0);  //top padding
    	c.gridx = 2;       //aligned with button 2
//    	c.gridwidth = 1;   //2 columns wide
    	c.gridy = 2;       //third row
    	pane.add(sendButton, c);

        setPreferredSize(new Dimension(400,400));
		pack();
		setVisible(true);
		
        // Process apply button
        sendButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	send();
            }
        });
        
        leaveButton.addActionListener(new ActionListener(){
        	public void actionPerformed(ActionEvent e){
        		leaveRoom();
        	}
        });
    }

    //send
    //takes the text from the message area and sends it to the interface
    //for processing.  Also takes the message and sends it right
    //to the main text field
    public void send() {
        
        String s = entry.getText();
        if (s.length() <= 0) {
            message("Nothing to send");
            return;
        }
        
        /* Send message to server */
        try {
        	//textArea.append(myName + " - " + s + "\n");
        	chatlogModel.addElement(myName + " - " + s + "\n");
			clientIF.sendMessage(myName + " - " + s + "\n", roomName);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        message("Message sent to server");

        // clear entry field
        entry.setText("");
        entry.setBackground(entryBg);
    }
    
    //leaveRoom
    //closes the current chatroom and leaves it
    public void leaveRoom()
    {
    	try {
			clientIF.leaveRoom(roomName, myName);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    //displayIncomingMsg
    //take the incoming message, and puts it in the main text area
    void displayIncomingMsg(String incomingMsg)
    {
    	//textArea.append(incomingMsg);
    	chatlogModel.addElement(incomingMsg);
    }
    
    public void addUser(String userName)
    {
    	boolean alreadyPresent = false;
    	userIterator = users.iterator();
    	while(userIterator.hasNext())
    	{
    		String user = (String)userIterator.next();
    		if(user.equals(userName))
    		{
    			alreadyPresent = true;
    		}
    	}
    	if(!alreadyPresent)
    	{
    		users.add(userName);
    		userModel.addElement(userName);
    	}
    }
    
    public void removeUser(String userName)
	{
    	userModel.removeElementAt(userModel.indexOf(userName));
    	users.remove(users.indexOf(userName));
	}
        
    //message
    //displays a message at the bottom of the GUI
    void message(String msg) {
        status.setText(msg);
    }

    // DocumentListener methods
    
    public void insertUpdate(DocumentEvent ev) {
        //search();
    }
    
    public void removeUpdate(DocumentEvent ev) {
        //search();
    }
    
    public void changedUpdate(DocumentEvent ev) {
    }
    
    //getName
    //returns the chatroom name
    public String getName()
    {
    	return roomName;
    }
    
    //CancelAction
    //listener for the cancel button
    class CancelAction extends AbstractAction {
        public void actionPerformed(ActionEvent ev) {
            entry.setText("");
            entry.setBackground(entryBg);
        }
    }
    
    //SendAction
    //listener for the send button
    class SendAction extends AbstractAction {
        public void actionPerformed(ActionEvent ev) {
        	send();
        }
    }   
    
    //registerInterface
    //registers the associated ClientInterface object
	public void registerInterface(ClientInterface incomingIF) 
	{
		clientIF = incomingIF;
	}

}