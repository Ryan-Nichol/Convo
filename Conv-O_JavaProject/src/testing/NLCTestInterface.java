package testing;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class NLCTestInterface extends JFrame {

	private JTextArea textBox = new JTextArea();
	private JScrollPane scrollPane = null;
	private JButton activeButton = new JButton("Turn On Classifier");
	private boolean active = false;

	public NLCTestInterface() {
		this.setLayout(null);
		this.setPreferredSize(new Dimension(500, 500));
		this.setLocation(1000, 0);

		activeButton.setLocation(0, 0);
		activeButton.setSize(500, 30);
		activeButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (active) {
					active = false;
					activeButton.setText("Turn On Classifier");
				} else {
					active = true;
					activeButton.setText("Turn Off Classifier");
				}
			}

		});
		add(activeButton);

		textBox.setLineWrap(true);
		scrollPane = new JScrollPane(textBox);
		scrollPane.setSize(475, 420);
		scrollPane.setLocation(0, 30);

		this.add(scrollPane);
		this.pack();
		this.setVisible(true);
	}

	public boolean getActive() {
		return active;
	}

	public void addText(String text) {
		textBox.append("\n" + text);
		textBox.setCaretPosition(textBox.getDocument().getLength());
	}

}