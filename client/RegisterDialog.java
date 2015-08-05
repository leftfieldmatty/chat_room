package client;
 
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class RegisterDialog extends JFrame {
	
	private static RegisterDialog instance_ =null;
	
    final static int maxGap = 100;
    JButton applyButton = new JButton("Register");
    JButton skipButton = new JButton("Back to Login");
    JTextField userField = new JTextField();
    JPasswordField passField = new JPasswordField();
    GridLayout experimentLayout = new GridLayout(0,2);
    ClientInterface clientIF;

	RegisterDialog()	{
		setSize(400,400);
		setLocation(600,300);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
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
        compsToExperiment.add(new JLabel("User Name"));
        compsToExperiment.add(userField);
        compsToExperiment.add(new JLabel("Password"));
        compsToExperiment.add(passField);
        compsToExperiment.add(new JLabel(""));
        compsToExperiment.add(new JLabel(""));
        compsToExperiment.add(applyButton);
        compsToExperiment.add(skipButton);

        // Process apply button
        applyButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //Process apply
            	//setVisible(true);
            	clientIF.addNewUser(userField.getText(), new String(passField.getPassword()));
            }
        });

        // Process skip button
        skipButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //Process skip
            	clientIF.showLoginGUI();
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
        System.out.println("RegisterDialog.createAndShowGUI");
        //Create and set up the window.
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //Set up the content pane.
        addComponentsToPane(getContentPane());
        //Display the window.
        pack();
        setVisible(false);
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
