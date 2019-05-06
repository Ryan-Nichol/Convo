package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.ClassifierList;
import model.ConvoService;
import model.Meeting;
import model.Time;

/**
 * 
 * This class represents the new meeting dialog, and is used to create a new
 * meeting
 * 
 * @author William Xu
 */
public class NewMeetingDialog extends JDialog implements ActionListener {

	// Java Swing Components
	private JLabel lblName, lblDataSet;
	private JTextField meetingName;
	private JLabel hrLabel, mLabel, sLabel;
	private JComboBox idBox, hoursBox, minutesBox, secondsBox;
	private JButton acceptBtn, cancelBtn;

	// Declare variables
	private ConvoService service;
	private ClassifierList serviceList;
	private List<Classifier> classifierList;

	// Setup dialog
	public NewMeetingDialog(ConvoService service) {
		this.setTitle("New Meeting");
		this.setModal(true);
		this.setLayout(null);
		this.setPreferredSize(new Dimension(600, 220));
		this.setLocation(200, 100);
		this.service = service;

		lblName = new JLabel("Meeting Name");
		lblName.setLocation(10, 5);
		lblName.setSize(100, 15);
		add(lblName);

		meetingName = new JTextField("");
		meetingName.setLocation(10, 25);
		meetingName.setSize(260, 35);
		this.add(meetingName);

		serviceList = service.getConvoClassifier().getNaturalLanguageClassifier().getClassifiers().execute();
		classifierList = serviceList.getClassifiers();
		String[] classifierNames = new String[classifierList.size()];
		for (int i = 0; i < classifierList.size(); i++) {
			classifierNames[i] = classifierList.get(i).getName();
		}

		lblDataSet = new JLabel("Meeting Topic");
		lblDataSet.setLocation(280, 5);
		lblDataSet.setSize(100, 15);
		add(lblDataSet);

		idBox = new JComboBox(classifierNames);
		idBox.setLocation(280, 25);
		idBox.setSize(260, 35);
		this.add(idBox);

		hrLabel = new JLabel("Hours");
		hrLabel.setHorizontalAlignment(SwingConstants.CENTER);
		hrLabel.setLocation(10, 65);
		hrLabel.setSize(80, 10);
		this.add(hrLabel);

		mLabel = new JLabel("Minutes");
		mLabel.setHorizontalAlignment(SwingConstants.CENTER);
		mLabel.setLocation(100, 65);
		mLabel.setSize(80, 10);
		this.add(mLabel);

		sLabel = new JLabel("Seconds");
		sLabel.setHorizontalAlignment(SwingConstants.CENTER);
		sLabel.setLocation(190, 65);
		sLabel.setSize(80, 10);
		this.add(sLabel);

		hoursBox = new JComboBox<Integer>(setValue(24));
		hoursBox.setSelectedIndex(1);
		hoursBox.setLocation(10, 80);
		hoursBox.setSize(80, 30);
		this.add(hoursBox);

		minutesBox = new JComboBox<Integer>(setValue(60));
		minutesBox.setSelectedIndex(0);
		minutesBox.setLocation(100, 80);
		minutesBox.setSize(80, 30);
		this.add(minutesBox);

		secondsBox = new JComboBox<Integer>(setValue(60));
		secondsBox.setSelectedIndex(0);
		secondsBox.setLocation(190, 80);
		secondsBox.setSize(80, 30);
		this.add(secondsBox);

		acceptBtn = new JButton("Create");
		acceptBtn.setLocation(310, 120);
		acceptBtn.setSize(200, 40);
		acceptBtn.addActionListener(this);
		this.add(acceptBtn);

		cancelBtn = new JButton("Cancel");
		cancelBtn.setLocation(40, 120);
		cancelBtn.setSize(200, 40);
		cancelBtn.addActionListener(this);
		this.add(cancelBtn);

		this.pack();
		this.setVisible(true);
	}

	// Return an array of integers
	private Integer[] setValue(int i) {
		Integer[] list = new Integer[i + 1];
		for (int n = 0; n <= i; n++) {
			list[n] = n;
		}
		return list;
	}

	public void createMeeting(ArrayList<String> attendees, ArrayList<String> absentees){
		int index = idBox.getSelectedIndex();
		String classifierID = classifierList.get(index).getClassifierId();
		Meeting meeting = new Meeting(meetingName.getText(), classifierID);
		Time time = new Time(meetingTime());
		meeting.setTime(time);
		meeting.setAttendance(attendees, absentees);
		service.getConvoClassifier().setClassifierDataSetID(classifierList.get(index).getClassifierId());
		service.getConvoInterface().setMeeting(meeting);
		this.dispose();
	}
	
	private void openAttendeeDialog(){
		AttendeeDialog attendeeDialog = new AttendeeDialog(this);
	}
	
	// return the meeting time
	private int[] meetingTime() {
		int hours = hoursBox.getSelectedIndex();
		int minutes = minutesBox.getSelectedIndex();
		int seconds = secondsBox.getSelectedIndex();
		return new int[] { hours, minutes, seconds };
	}

	// Handle user actions
	public void actionPerformed(ActionEvent o) {
		JButton btn = (JButton) o.getSource();
		if (btn == acceptBtn) {
			if (meetingName.getText().length() > 12) {
				JOptionPane.showMessageDialog(null, "Meeting name must be less than 13 characters", "Error Message",
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			if (!meetingName.getText().equals("") && !meetingName.getText().equals(" ")) {
				openAttendeeDialog();	
			} else {
				JOptionPane.showMessageDialog(null, "Meeting name can not start with a space or be blank",
						"Error Message", JOptionPane.INFORMATION_MESSAGE);
			}
		} else if (btn == cancelBtn) {
			this.dispose();
		}
	}
}