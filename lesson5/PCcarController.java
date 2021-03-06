package lesson5;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.event.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import lejos.pc.comm.*;
import java.io.*;

/**
 * A GUI that makes it possible to establish a Bluetooth connection
 * to an NXT controlled car. The car is then controlled by a power
 * and duration value sent to the car. The tacho counter value is
 * received and displayed after each command to the car has been performed.
 * 
 * @author Ole Caprani 24.2.12
 *
 * 
 */
public class PCcarController extends JFrame implements ActionListener
{
	private TextField nameField = new TextField(12);
	private TextField addressField = new TextField(20);

	private String name = "Carpet"; 
	private String address = "00165317F1D0";
	
	private TextField pfield = new TextField(10);
	private TextField ifield = new TextField(10);
	private TextField dfield = new TextField(10);
	private TextField tpfield = new TextField(10);
	  
	private NXTComm nxtComm;
	private NXTInfo nxtInfo;
	private InputStream is;
	private OutputStream os;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	private JButton connectButton = new JButton("Connect");	
	private JButton goButton = new JButton("Go");

	/**
	 * Constructor builds GUI
	 */
	public PCcarController() 
	{		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Control NXT");
		setSize(500,300);
		
		// holds labels and text fields
		JPanel p1 = new JPanel();  
		p1.add(new JLabel("Name:"));
		p1.add(nameField);
		nameField.setText(name);
		p1.add(new JLabel("Address:"));
		p1.add(addressField);
		addressField.setText(address);
	  
		try
		{
			nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		}
		catch (NXTCommException nce) {
		}
		nxtInfo = new NXTInfo();
	  
		// holds connect button
		JPanel p2 = new JPanel();
		p2.add(connectButton);
		connectButton.addActionListener(this);

		// holds labels and text fields
		JPanel p3 = new JPanel();  
		p3.add(new JLabel("P:"));
		p3.add(pfield);
		p3.add(new JLabel("I:"));
		p3.add(ifield);
		p3.add(new JLabel("D:"));
		p3.add(dfield);
		
		JPanel p5 = new JPanel();  
		p5.add(new JLabel("TP:"));
		p5.add(tpfield);

		pfield.setText("28");
		ifield.setText("4");
		dfield.setText("33");
		tpfield.setText("45");
		
		// holds go button
		JPanel p4 = new JPanel();
		p4.add(goButton);
		goButton.addActionListener(this);
		
		// North area of the frame
		JPanel panel = new JPanel();  
		panel.setLayout(new GridLayout(5,1));
		panel.add(p1);
		panel.add(p2);
		panel.add(p3);
		panel.add(p4);
		panel.add(p5);
		
		this.add(panel, BorderLayout.NORTH);
		this.pack();
		this.repaint();
	}

	/**
	 * Required by action listener; 
	 * only action is generated by the get Length button
	 */	
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()== connectButton)
		{
			String name = nameField.getText();
			String address = addressField.getText();
			nxtInfo.name = name;
			nxtInfo.deviceAddress = address;
			try
			{
				nxtComm.open(nxtInfo);
				is = nxtComm.getInputStream();
				os = nxtComm.getOutputStream();
				 dis = new DataInputStream(is);
				 dos = new DataOutputStream(os);
			}
			catch (Exception ex) {
			}
		}	  
		
		if(e.getSource()== goButton)
		{
	 	 try
			{
	 		 String pstring = pfield.getText();
	 		 int pvalue = new Integer(pstring).intValue();		  
				dos.writeInt(pvalue);
				dos.flush();

			 String istring = ifield.getText();
			 int ivalue = new Integer(istring).intValue();		  
			 dos.writeInt(ivalue);
			 dos.flush();

			 String dstring = dfield.getText();
			 int dvalue = new Integer(dstring).intValue();		  
			 dos.writeInt(dvalue);
			 dos.flush();
			 
			 String tpstring = tpfield.getText();
			 int tpvalue = new Integer(tpstring).intValue();		  
				dos.writeInt(tpvalue);
				dos.flush();
				 }
			catch (Exception ex) {
			}			  
		}
	}
	
	/**
	 * Initialize the display Frame
	 */		
	public static void main(String[] args)
	{
		PCcarController frame = new PCcarController();
		frame.setVisible(true);
	}
}