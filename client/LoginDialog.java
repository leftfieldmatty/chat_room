package client;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
 
public class LoginDialog extends JFrame {
	
	private static LoginDialog instance_ =null;
	
    final static int maxGap = 100;
    static String commandParam = "default";
    JButton signupButton = new JButton("Register new user");
    JButton loginButton = new JButton("Login");
    JTextField hostField = new JTextField();
    JTextField userField = new JTextField();
    JPasswordField passField = new JPasswordField();
    String errorString = "Error message here";
    JLabel errorLabel = new JLabel(errorString);
    GridLayout experimentLayout = new GridLayout(0,2);
    ClientInterface clientIF;
     
    LoginDialog() {
		setSize(400,400);
		setLocation(600,300);
        setResizable(false);
        createAndShowGUI();
        
    }
	
	public void registerInterface(ClientInterface incomingIF)
	{
		clientIF = incomingIF;
	}
     
    public void addComponentsToPane(final Container pane) {

        final JPanel compsToExperiment = new JPanel();
        compsToExperiment.setLayout(experimentLayout);
        JPanel controls = new JPanel();
        controls.setLayout(new GridLayout(2,7));
        
        //Set up components preferred size
        JButton b = new JButton("Just fake button");
        Dimension buttonSize = b.getPreferredSize();
        compsToExperiment.setPreferredSize(new Dimension((int)(buttonSize.getWidth() * 2.5)+maxGap,
                (int)(buttonSize.getHeight() * 3.5)+maxGap * 2));
         
        //Add buttons to experiment with Grid Layout
        compsToExperiment.add(new JLabel(""));
        compsToExperiment.add(new JLabel(""));
        compsToExperiment.add(new JLabel("Welcome to Messenger!"));
        compsToExperiment.add(new JLabel(""));
        compsToExperiment.add(new JLabel("Host IP Address"));
        compsToExperiment.add(hostField);
        compsToExperiment.add(new JLabel("Username"));
        compsToExperiment.add(userField);
        compsToExperiment.add(new JLabel("Password"));
        compsToExperiment.add(passField);
        compsToExperiment.add(new JLabel(""));
        compsToExperiment.add(new JLabel(""));
        compsToExperiment.add(signupButton);
        compsToExperiment.add(loginButton);

        // Process signup button
        signupButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
            	clientIF.showRegisterGUI();
            }
        });

        // Process login button
        loginButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

            	clientIF.login(hostField.getText(), userField.getText(),new String(passField.getPassword()));
            }
        });
        
        pane.add(compsToExperiment, BorderLayout.NORTH);
        pane.add(new JSeparator(), BorderLayout.CENTER);
        pane.add(controls, BorderLayout.SOUTH);
        experimentLayout.setHgap(15);
        //experimentLayout.setVgap(15);
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method is invoked from the
     * event dispatch thread.
     */
    private void createAndShowGUI() {
        //Create and set up the window.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Set up the content pane.
        addComponentsToPane(getContentPane());
        //Display the window.
        pack();
        setVisible(true);
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
}
