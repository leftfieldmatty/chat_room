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
    
    private JTextField entry;
    private JLabel jLabel1;
    private JButton sendButton;
    private JButton leaveButton;
    private JScrollPane jScrollPane1;
    private JLabel status;
    private JTextArea textArea;
    private String roomName;
    private String myName;
    static ClientInterface clientIF;
    private ArrayList users;
    private Iterator userIterator;
    static JList userList;
    static DefaultListModel userModel;
    
    
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
        entry = new JTextField();
        textArea = new JTextArea();
        status = new JLabel();
        jLabel1 = new JLabel();
        sendButton = new JButton("Send");
        leaveButton = new JButton("Leave Room");
		userModel = new DefaultListModel();
		userList = new JList(userModel);

        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
        	public void windowClosing(WindowEvent ev) {
        		dispose();
        	}
        });
        setTitle(roomName + " - " + myName);

        textArea.setColumns(20);
        textArea.setLineWrap(true);
        textArea.setRows(5);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        jScrollPane1 = new JScrollPane(textArea);

        jLabel1.setText("Enter text to send:");

        getContentPane().setLayout(new GridBagLayout());
        
        GridBagConstraints c = new GridBagConstraints();
        
        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 0;
        getContentPane().add(entry, c);
        
        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 1;
        c.gridy = 0;
        getContentPane().add(sendButton,c);
		
        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 2;
        c.gridy = 0;
        getContentPane().add(leaveButton, c);
        
        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 100;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 2;
        getContentPane().add(textArea,c );
        
        c.weightx = 0.5;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 100;
        c.gridx = 2;
        c.gridy = 1;
        getContentPane().add(userList, c);
        
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
        	textArea.append(myName + " - " + s + "\n");
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
    	textArea.append(incomingMsg);
    }
        
    //message
    //displays a message at the bottom of the GUI
    void message(String msg) {
        status.setText(msg);
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
    //listender for the send button
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