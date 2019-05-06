package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.*;
import model.Meeting;

/**
 * This class represents a bubble dialog for adding bubbles to the main
 * application
 * 
 * @author Ryan Nichol
 *
 */
public class AddBubbleDialog extends JDialog implements ActionListener {

	// Java Swing Components
	private JTextField bubbleNameField, minutesField, secondsField;
	private JButton addBtn, cancelBtn, minUp, minDown, secUp, secDown;
	private JLabel timeLabel;

	// Declare variables
	private ArrayList<String> bubbleNames;
	private ConvoInterface graph;
	private int minutes = 5;
	private int seconds = 0;

	// Setup the dialog
	public AddBubbleDialog(final ConvoInterface graph, ArrayList<String> bubbleNames) {
		this.setTitle("Add Bubble");
		this.bubbleNames = bubbleNames;
		this.graph = graph;
		this.setModal(false);
		getContentPane().setLayout(null);
		this.setPreferredSize(new Dimension(300, 220));
		this.setLocation(200, 100);

		timeLabel = new JLabel("Min          Sec", JLabel.CENTER);
		timeLabel.setLocation(80, 60);
		timeLabel.setSize(120, 10);
		timeLabel.setBackground(Color.RED);
		timeLabel.setForeground(Color.RED);
		getContentPane().add(timeLabel);

		bubbleNameField = new JTextField();
		bubbleNameField.setLocation(10, 10);
		bubbleNameField.setSize(260, 40);
		bubbleNameField.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					createBubble();
				}
			}

			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

		});
		getContentPane().add(bubbleNameField);

		addBtn = new JButton("Add");
		addBtn.setLocation(150, 130);
		addBtn.setSize(120, 40);
		addBtn.addActionListener(this);
		getContentPane().add(addBtn);

		cancelBtn = new JButton("Cancel");
		cancelBtn.setLocation(10, 130);
		cancelBtn.setSize(120, 40);
		cancelBtn.addActionListener(this);
		getContentPane().add(cancelBtn);

		minutesField = new JTextField("5");
		minutesField.setEditable(false);
		minutesField.setHorizontalAlignment(JTextField.CENTER);
		minutesField.setLocation(80, 80);
		minutesField.setSize(40, 40);
		getContentPane().add(minutesField);

		minUp = new JButton("+");
		minUp.setFont(new Font("Courier New", Font.BOLD, 10));
		minUp.setLocation(120, 80);
		minUp.setSize(20, 20);
		minUp.setMargin(new Insets(0, 0, 0, 0));
		minUp.addActionListener(this);
		getContentPane().add(minUp);

		minDown = new JButton("-");
		minDown.setFont(new Font("Courier New", Font.BOLD, 10));
		minDown.setLocation(120, 100);
		minDown.setSize(20, 20);
		minDown.setMargin(new Insets(0, 0, 0, 0));
		minDown.addActionListener(this);
		getContentPane().add(minDown);

		secondsField = new JTextField("0");
		secondsField.setEditable(false);
		secondsField.setLocation(140, 80);
		secondsField.setSize(40, 40);
		secondsField.setHorizontalAlignment(JTextField.CENTER);
		getContentPane().add(secondsField);

		secUp = new JButton("+");
		secUp.setFont(new Font("Courier New", Font.BOLD, 10));
		secUp.setLocation(180, 80);
		secUp.setSize(20, 20);
		secUp.setMargin(new Insets(0, 0, 0, 0));
		secUp.addActionListener(this);
		getContentPane().add(secUp);

		secDown = new JButton("-");
		secDown.setFont(new Font("Courier New", Font.BOLD, 10));
		secDown.setLocation(180, 100);
		secDown.setSize(20, 20);
		secDown.setMargin(new Insets(0, 0, 0, 0));
		secDown.addActionListener(this);
		getContentPane().add(secDown);

		this.pack();
		this.setVisible(true);
	}

	// Creates and adds bubble to main application
	public void createBubble() {
		if (isValidBubble()) {
			if (graph.getMeeting().getAllTopics().size() < 9) {
				graph.addBubble(bubbleNameField.getText(), new int[] { 0, minutes, seconds });
				bubbleNameField.setText("");
			} else {
				JOptionPane.showMessageDialog(null, "Maximum Bubble Limit Reached", "Error Message",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	// Checks the bubble values are valid for creation
	public boolean isValidBubble() {
		String name = bubbleNameField.getText();
		if (name.startsWith(" ") || name.equals("") || bubbleNames.contains(name) || name.length() >= 15) {
			JOptionPane.showMessageDialog(null,
					"Invalid bubble name!\n\n    Name cannot begin with a space\n    Name cannot be left blank\n    Name must be 15 or less characters",
					"Error Message", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		return true;
	}

	// Handle user input
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == addBtn) {
			createBubble();
		} else if (e.getSource() == cancelBtn) {
			this.dispose();
		} else if (e.getSource() == minUp) {
			minutes += 1;
		} else if (e.getSource() == minDown) {
			minutes--;
			if (minutes == -1) {
				minutes = 0;
			}
		} else if (e.getSource() == secUp) {
			seconds += 10;
			if (seconds == 60) {
				seconds = 0;
			}
		} else if (e.getSource() == secDown) {
			seconds -= 10;
			if (seconds == -10) {
				seconds = 50;
			}
		}

		String sMinutes = String.valueOf(minutes);
		String sSeconds = String.valueOf(seconds);
		minutesField.setText(sMinutes);
		secondsField.setText(sSeconds);
	}
}
