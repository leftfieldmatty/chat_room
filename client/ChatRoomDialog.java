package client;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.rmi.RemoteException;

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
    private JButton jButton1;
    private JScrollPane jScrollPane1;
    private JLabel status;
    private JTextArea textArea;
    private String roomName;
    private String myName;
    static ClientInterface clientIF;

    
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

        /*InputStream in = getClass().getResourceAsStream("context.txt");
        try {
            textArea.read(new InputStreamReader(in), null);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        
        entryBg = entry.getBackground();
        entry.getDocument().addDocumentListener(this);
        
        InputMap im = entry.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = entry.getActionMap();
        im.put(KeyStroke.getKeyStroke("ENTER"), SEND_ACTION);
        am.put(SEND_ACTION, new SendAction());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     */

    //initComponents
    //initializes all the pieces of the GUI
    private void initComponents() {
        entry = new JTextField();
        textArea = new JTextArea();
        status = new JLabel();
        jLabel1 = new JLabel();
        jButton1 = new JButton("Send");

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

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        
		//Create a parallel group for the horizontal axis
		ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		
		//Create a sequential and a parallel groups
		SequentialGroup h1 = layout.createSequentialGroup();
		ParallelGroup h2 = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);
		
		//Add a container gap to the sequential group h1
		h1.addContainerGap();
		
		//Add a scroll pane and a label to the parallel group h2
		h2.addComponent(jScrollPane1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE);
		h2.addComponent(status, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE);
		
		//Create a sequential group h3
		SequentialGroup h3 = layout.createSequentialGroup();
	//	h3.addComponent(jLabel1);
	//	h3.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		h3.addComponent(entry, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE);
		h3.addGap(5);
		h3.addComponent(jButton1);
		//h3.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		//h3.addComponent(jButton1);
		
		//Add the group h3 to the group h2
		h2.addGroup(h3);
		//Add the group h2 to the group h1
		h1.addGroup(h2);
	
		h1.addContainerGap();
		
		//Add the group h1 to the hGroup
		hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);
		//Create the horizontal group
		layout.setHorizontalGroup(hGroup);
		
	        
		//Create a parallel group for the vertical axis
		ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		//Create a sequential group v1
		SequentialGroup v1 = layout.createSequentialGroup();
		//Add a container gap to the sequential group v1
		v1.addContainerGap();
		//Create a parallel group v2
		ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
	//	v2.addComponent(jLabel1);
		v2.addComponent(jButton1);
		v2.addComponent(entry, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
		//Add the group v2 tp the group v1
		v1.addGroup(v2);
		v1.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED);
		v1.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE);
		v1.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED);
		v1.addComponent(status);
		v1.addContainerGap();
		
		//Add the group v1 to the group vGroup
		vGroup.addGroup(v1);
		//Create the vertical group
		layout.setVerticalGroup(vGroup);
		pack();
		setVisible(true);
		
        // Process apply button
        jButton1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	send();
            }
        });
    }

/*
    public void search() {
        
        String s = entry.getText();
        if (s.length() <= 0) {
            message("Nothing to search");
            return;
        }
        
        String content = textArea.getText();
        int index = content.indexOf(s, 0);
        if (index >= 0) {   // match found
            try {
                int end = index + s.length();
                textArea.setCaretPosition(end);
                entry.setBackground(entryBg);
                message("'" + s + "' found. Press ESC to end search");
            } catch (BadLocationException e) {
                e.printStackTrace();
            }
        } else {
            entry.setBackground(ERROR_COLOR);
            message("'" + s + "' not found. Press ESC to start a new search");
        }
    }
*/
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