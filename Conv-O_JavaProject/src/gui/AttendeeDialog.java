package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * This class represents a dialog for adding attendees and absentees to Convo. There must be at least one
 * attendee. 
 * @author Ryan Nichol
 *
 */
public class AttendeeDialog extends JDialog implements FocusListener, ActionListener, ListSelectionListener{

	private JLabel labelAttendees, labelAbsentees;
	private JTextField textAttendee, textAbsentee;
	private JButton btnConfirm, btnAddAttendee, btnAddAbsentee, btnRemove;
	private JList listOfAttendees, listOfAbsentees;
	
	private NewMeetingDialog newMeetingDialog;
	private ArrayList<String> attendees = new ArrayList<String>();
	private ArrayList<String> absentees = new ArrayList<String>();
	
	private Color colorText = Color.BLACK;
	private Color colorHint = Color.GRAY;
	
	private boolean attendeeSelected=false;
	private boolean absenteeSelected=false;
	
	public AttendeeDialog(NewMeetingDialog newMeetingDialog){
		super();
		
		this.setTitle("Attendance");
		this.setModal(true);
		this.setLayout(null);
		this.setPreferredSize(new Dimension(500, 400));
		this.setLocation(200, 100);
		
		this.newMeetingDialog = newMeetingDialog;
		
		labelAttendees = new JLabel("Attendees List");
		labelAttendees.setSize(250, 20);
		labelAttendees.setLocation(6,10);
		add(labelAttendees);
		
		labelAbsentees = new JLabel("Absentees List");
		labelAbsentees.setSize(250, 20);
		labelAbsentees.setLocation(251,10);
		add(labelAbsentees);
		
		textAttendee = new JTextField("Members Name");
		textAttendee.setLocation(5,30);
		textAttendee.setSize(200,30);
		textAttendee.addFocusListener(this);
		textAttendee.setForeground(colorHint);
		textAttendee.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode()==KeyEvent.VK_ENTER){
					addAttendee();
					textAttendee.setText("");
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		this.add(textAttendee);
		
		textAbsentee = new JTextField("Members Name");
		textAbsentee.setLocation(250,30);
		textAbsentee.setSize(200,30);
		textAbsentee.addFocusListener(this);
		textAbsentee.setForeground(colorHint);
		textAbsentee.addKeyListener(new KeyListener(){

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					addAbsentee();
					textAbsentee.setText("");
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		this.add(textAbsentee);
		
		btnAddAttendee = new JButton("+");
		btnAddAttendee.setFont(new Font("Courier New", Font.BOLD, 10));
		btnAddAttendee.setLocation(205, 30);
		btnAddAttendee.setSize(30, 30);
		btnAddAttendee.setMargin(new Insets(0, 0, 0, 0));
		btnAddAttendee.addActionListener(this);
		this.add(btnAddAttendee);
		
		btnAddAbsentee = new JButton("+");
		btnAddAbsentee.setFont(new Font("Courier New", Font.BOLD, 10));
		btnAddAbsentee.setLocation(450, 30);
		btnAddAbsentee.setSize(30, 30);
		btnAddAbsentee.setMargin(new Insets(0, 0, 0, 0));
		btnAddAbsentee.addActionListener(this);
		this.add(btnAddAbsentee);
		
		btnRemove = new JButton("Remove Selected");
		btnRemove.setLocation(37,315);
		btnRemove.setSize(200,40);
		btnRemove.addActionListener(this);
		add(btnRemove);
		
		btnConfirm = new JButton("Confirm");
		btnConfirm.setLocation(250,315);
		btnConfirm.setSize(200,40);
		btnConfirm.addActionListener(this);
		add(btnConfirm);
		
		listOfAttendees = new JList();
		listOfAttendees.setSize(230, 250);
		listOfAttendees.setLocation(5,60);
		listOfAttendees.addListSelectionListener(this);
		this.add(listOfAttendees);
		
		listOfAbsentees = new JList();
		listOfAbsentees.setSize(230, 250);
		listOfAbsentees.setLocation(250,60);
		listOfAbsentees.addListSelectionListener(this);
		this.add(listOfAbsentees);
		
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void focusGained(FocusEvent o) {
		JTextField focusedField = (JTextField) o.getSource();
		if (focusedField==textAttendee){
			if (textAttendee.getForeground()==colorHint){
				focusedField.setText("");
				focusedField.setForeground(colorText);
			}
		}else if (focusedField==textAbsentee){
			if (textAbsentee.getForeground()==colorHint){
				focusedField.setText("");
				focusedField.setForeground(colorText);
			}
		}
	}

	@Override
	public void focusLost(FocusEvent o) {
		JTextField focusedField = (JTextField) o.getSource();
		if (focusedField==textAttendee){
			if (textAttendee.getText().equals("") || textAttendee.getText().startsWith(" ")){
				focusedField.setText("Members Name");
				focusedField.setForeground(colorHint);
			}
		}else if (focusedField==textAbsentee){
			if (textAbsentee.getText().equals("") || textAbsentee.getText().startsWith(" ")){
				focusedField.setText("Members Name");
				focusedField.setForeground(colorHint);
			}
		}
	}

	public void addAttendee(){
		if (attendees.size()<16 && textAttendee.getText().length()<18){
			attendees.add(textAttendee.getText());
			listOfAttendees.setListData(attendees.toArray());	
		}else{
			JOptionPane.showMessageDialog(null, "A Convo meeting can only have 16 Attendees\nAn attendee "
					+ "name must be less than 18 characters", "Error Message",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	public void addAbsentee(){
		if (absentees.size() < 8 && textAbsentee.getText().length()<18){
			absentees.add(textAbsentee.getText());
			listOfAbsentees.setListData(absentees.toArray());
		}else{
			JOptionPane.showMessageDialog(null, "A Convo meeting can only have 8 Absentees\nAn absentee "
					+ "name must less than 18 characters", "Error Message",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	public void removeAbsentee(String name){
		absentees.remove(name);
		listOfAbsentees.setListData(absentees.toArray());
	}
	public void removeAttendee(String name){
		attendees.remove(name);
		listOfAttendees.setListData(attendees.toArray());
	}
	
	
	@Override
	public void actionPerformed(ActionEvent o) {
		JButton buttonPressed = (JButton) o.getSource();
		if (buttonPressed==btnAddAttendee){
			if (textAttendee.getForeground()==colorText){
				addAttendee();
				textAttendee.setText("Members Name");
				textAttendee.setForeground(colorHint);
			}
		}
		else if (buttonPressed==btnAddAbsentee){
			if (textAbsentee.getForeground()==colorText){
				addAbsentee();
				textAbsentee.setText("Members Name");
				textAbsentee.setForeground(colorHint);
			}
		}else if (buttonPressed==btnRemove){
			try{
				if (attendeeSelected){
					String name = (String)listOfAttendees.getSelectedValue();
					removeAttendee(name);
				}
				else if (absenteeSelected){
					String name = (String)listOfAbsentees.getSelectedValue();
					removeAbsentee(name);
				}
			}catch(Exception e){
				System.out.println("Dead");
			}
		}else if (buttonPressed==btnConfirm){
			if (attendees.size()>0){
				newMeetingDialog.createMeeting(attendees, absentees);
				this.dispose();	
			}else{
				JOptionPane.showMessageDialog(null, "There must be atleast one Attendee", "Error Message",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent o) {
		JList list = (JList)o.getSource();
		if (list==listOfAttendees){
			attendeeSelected = true;
			absenteeSelected=false;
		}else if (list == listOfAbsentees){
			absenteeSelected = true;
			attendeeSelected=false;
		}
	}
	
	
	
}
