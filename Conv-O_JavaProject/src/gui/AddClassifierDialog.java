package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.ConvoService;

/**
 * This class represents a dialog where the user can add new classifiers to IBM
 * 
 * @author Ryan Nichol
 */
public class AddClassifierDialog extends JDialog implements ActionListener {

	// Java Swing Components
	private JButton btnAddClassifier, btnCancel, btnSelectFile;
	private JTextField textFileName, textClassifierName;
	private JLabel lblName, lblLocation;

	// Declare Variables
	private File file;
	private ConvoService service;

	// Setup dialog
	public AddClassifierDialog(ConvoService service) {

		this.setLayout(null);
		this.service = service;

		lblName = new JLabel("Training Data Name");
		lblName.setLocation(10, 5);
		lblName.setSize(400, 10);
		add(lblName);

		lblLocation = new JLabel("Training Data Location");
		lblLocation.setLocation(10, 65);
		lblLocation.setSize(400, 10);
		add(lblLocation);

		textClassifierName = new JTextField();
		textClassifierName.setLocation(10, 20);
		textClassifierName.setSize(550, 40);
		add(textClassifierName);

		btnSelectFile = new JButton("...");
		btnSelectFile.setSize(50, 40);
		btnSelectFile.setLocation(510, 80);
		btnSelectFile.addActionListener(this);
		add(btnSelectFile);

		textFileName = new JTextField();
		textFileName.setEditable(false);
		textFileName.setLocation(10, 80);
		textFileName.setSize(500, 40);
		add(textFileName);

		btnAddClassifier = new JButton("Add");
		btnAddClassifier.setSize(150, 50);
		btnAddClassifier.setLocation(300, 130);
		btnAddClassifier.addActionListener(this);
		add(btnAddClassifier);

		btnCancel = new JButton("Cancel");
		btnCancel.setSize(150, 50);
		btnCancel.setLocation(140, 130);
		btnCancel.addActionListener(this);
		add(btnCancel);

		this.setPreferredSize(new Dimension(600, 225));
		this.setTitle("Add Training Data");
		this.setModal(true);
		this.pack();
		this.setVisible(true);

	}

	// Add classifier to IBM
	public void addClassifier() {
		service.getConvoClassifier().addClassifier(textClassifierName.getText(), file);
	}

	// Open system file search, and choose .csv file
	public void searchForClassifier() {
		final JFileChooser fc = new JFileChooser();
		fc.setName("Select a CSV File");
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".csv", "csv", "csv");
		fc.setFileFilter(filter);
		int returnVal = fc.showOpenDialog(this);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			file = fc.getSelectedFile();
			// This is where a real application would open the file.
			textFileName.setText(file.getPath());
			System.out.println(file.getPath());
		}
	}

	// Check if file chosen is valid .csv file
	public boolean validFile() {

		if (file != null) {
			if (!file.exists()) {
				return false;
			}

			System.out.println(file.getName());
			String[] name = file.getName().split("\\.");
			if (name.length > 0) {
				String ext = name[name.length - 1];
				if (!ext.toLowerCase().equals("csv")) {
					return false;
				}
			} else {
				return false;
			}

		} else {
			return false;
		}

		return true;
	}

	// Handle user actions
	public void actionPerformed(ActionEvent o) {
		if (btnSelectFile == o.getSource()) {
			searchForClassifier();
		} else if (btnAddClassifier == o.getSource()) {
			if (!textClassifierName.getText().equals("") && !textClassifierName.getText().equals(" ")) {
				if (validFile()) {
					addClassifier();
					JOptionPane.showMessageDialog(null, textClassifierName.getText() + " added to data sets",
							"Information", JOptionPane.INFORMATION_MESSAGE);
					this.dispose();
				} else {
					JOptionPane.showMessageDialog(null, "Invalid File", "Error Message",
							JOptionPane.INFORMATION_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(null, "Invalid Training Data Name", "Error Message",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} else if (o.getSource() == btnCancel) {
			this.dispose();
		}

	}
}
