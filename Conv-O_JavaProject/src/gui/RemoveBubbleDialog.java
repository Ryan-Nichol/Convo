package gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 * 
 * This dialog is used for removing bubbles from the convo interface graph
 * 
 * @author Ryan Nichol
 *
 */
public class RemoveBubbleDialog extends JDialog implements ActionListener {

	// Java Swing Components
	private JComboBox itemBox;
	private JButton removeBtn, cancelBtn;
	private JLabel title;

	// declare graph
	ConvoInterface graph;

	// Setup dialog
	public RemoveBubbleDialog(Object[] items, ConvoInterface graph) {
		this.graph = graph;
		this.setModal(true);
		this.setLayout(null);
		this.setPreferredSize(new Dimension(300, 150));
		this.setLocation(200, 100);
		this.setTitle("Remove Bubble");

		itemBox = new JComboBox(items);
		itemBox.setLocation(10, 10);
		itemBox.setSize(260, 40);
		this.add(itemBox);

		removeBtn = new JButton("Remove");
		removeBtn.setLocation(150, 60);
		removeBtn.setSize(120, 40);
		removeBtn.addActionListener(this);
		this.add(removeBtn);

		cancelBtn = new JButton("Cancel");
		cancelBtn.setLocation(10, 60);
		cancelBtn.setSize(120, 40);
		cancelBtn.addActionListener(this);
		this.add(cancelBtn);

		this.pack();
		this.setVisible(true);
	}

	// Handle user actions
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

		if (arg0.getSource() == removeBtn) {
			graph.removeBubble(itemBox.getSelectedItem().toString());
			this.dispose();
		} else if (arg0.getSource() == cancelBtn) {
			this.dispose();
		}

	}

}
