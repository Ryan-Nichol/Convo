package testing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

public class NLUTestInterface extends JFrame {

	private JTextArea textBox = new JTextArea();
	private JScrollPane scrollPane = null;
	private JButton activeButton = new JButton("Pause");
	private boolean active = true;

	public NLUTestInterface() {
		this.setLayout(null);
		this.setPreferredSize(new Dimension(500, 500));
		this.setLocation(500, 0);

		activeButton.setLocation(0, 0);
		activeButton.setSize(500, 30);
		activeButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if (active) {
					active = false;
					activeButton.setText("Resume");
				} else {
					active = true;
					activeButton.setText("Pause");
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

	public void addErrorText(String text) {
		if (active) {
			textBox.append("\n" + text);
			textBox.setCaretPosition(textBox.getDocument().getLength());
		}
	}

	public boolean isActive() {
		return active;
	}

	public void addText(String text) {
		if (active) {
			textBox.append("\n" + text);
			textBox.setCaretPosition(textBox.getDocument().getLength());
		}
	}

}
