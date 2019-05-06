package testing;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import model.ConvoVoiceListener;

public class STTtestInterface extends JFrame implements ActionListener {

	private JTextArea textBox = new JTextArea();
	private JScrollPane scrollPane = null;
	private JButton btnTestInput;
	private JButton[] testButtons = new JButton[10];
	private String[][] testInput = new String[10][];
	private ConvoVoiceListener vl;
	// private int index=-1;

	public STTtestInterface(final ConvoVoiceListener vl) {
		this.setLayout(null);
		this.setPreferredSize(new Dimension(500, 500));
		this.vl = vl;

		loadTestInput();

		int index = -1;
		for (int i = 0; i < testButtons.length; i++) {
			index++;
			testButtons[i] = new JButton(String.valueOf(i + 1));
			testButtons[i].setLocation(i * 48, 0);
			testButtons[i].setSize(48, 30);
			testButtons[i].addActionListener(this);
			add(testButtons[i]);
		}

		btnTestInput = new JButton("Test Input");
		btnTestInput.setSize(500, 30);
		btnTestInput.setLocation(0, 0);
		btnTestInput.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				// vl.addTestInput();
			}

		});

		textBox.setLineWrap(true);
		scrollPane = new JScrollPane(textBox);
		scrollPane.setSize(475, 420);
		scrollPane.setLocation(0, 30);

		// this.add(btnTestInput);
		this.add(scrollPane);
		this.pack();
		this.setVisible(true);
	}

	public void loadTestInput() {
		testInput[0] = new String[8];
		testInput[0][0] = ("So if we were to talk about the weather");
		testInput[0][1] = ("The classifier is able to tell");
		testInput[0][2] = ("If we talk about something like Movies or Games then the classifier will know we are off topic");
		testInput[0][3] = ("Once we know we are heading off topic, we can quickly get back on topic");
		testInput[0][4] = ("It is so cold in Auckland right now, my feet are freezing");
		testInput[0][5] = ("However we can trick the system, for example");
		testInput[0][6] = ("My clothes got wet and are now cold");
		testInput[0][7] = ("Nevermind it seems hard to trick the system, but I am using a threshold of 0.99");

		testInput[1] = new String[12];
		testInput[1][0] = ("It's such a nice day.");
		testInput[1][1] = ("Yes, it is.");
		testInput[1][2] = ("It looks like it may rain soon.");
		testInput[1][3] = ("Yes, and I hope that it does.");
		testInput[1][4] = ("Why is that?");
		testInput[1][5] = ("I really love how rain clears the air.");
		testInput[1][6] = ("Me too. It always smells so fresh after it rains.");
		testInput[1][7] = ("Yes, but I love the night air after it rains.");
		testInput[1][8] = ("Really? Why is it?");
		testInput[1][9] = ("Because you can see the stars perfectly.");
		testInput[1][10] = ("I really hope it rains today.");
		testInput[1][11] = ("Yeah, me too.");

		testInput[2] = new String[12];
		testInput[2][0] = ("This is an off topic sentence");
		testInput[2][1] = ("This is an off topic sentence");
		testInput[2][2] = ("This is an off topic sentence");
		testInput[2][3] = ("This is an off topic sentence");
		testInput[2][4] = ("This is an off topic sentence");
		testInput[2][5] = ("This is an off topic sentence");
		testInput[2][6] = ("This is an off topic sentence");
		testInput[2][7] = ("This is an off topic sentence");
		testInput[2][8] = ("This is an off topic sentence");
		testInput[2][9] = ("This is an off topic sentence");
		testInput[2][10] = ("This is an off topic sentence");
		testInput[2][11] = ("This is an off topic sentence");

		testInput[3] = new String[12];
		testInput[3][0] = ("It is so cold in Auckland right now, my feet are freezing");
		testInput[3][1] = ("It is so cold in Auckland right now, my feet are freezing");
		testInput[3][2] = ("It is so cold in Auckland right now, my feet are freezing");
		testInput[3][3] = ("It is so cold in Auckland right now, my feet are freezing");
		testInput[3][4] = ("It is so cold in Auckland right now, my feet are freezing");
		testInput[3][5] = ("It is so cold in Auckland right now, my feet are freezing");
		testInput[3][6] = ("It is so cold in Auckland right now, my feet are freezing");
		testInput[3][7] = ("It is so cold in Auckland right now, my feet are freezing");
		testInput[3][8] = ("It is so cold in Auckland right now, my feet are freezing");
		testInput[3][9] = ("It is so cold in Auckland right now, my feet are freezing");
		testInput[3][10] = ("It is so cold in Auckland right now, my feet are freezing");
		testInput[3][11] = ("It is so cold in Auckland right now, my feet are freezing");

	}

	public void addText(String text) {
		textBox.append("\n" + text);
		textBox.setCaretPosition(textBox.getDocument().getLength());
	}

	public void actionPerformed(ActionEvent o) {
		// TODO Auto-generated method stub
		for (int i = 0; i < testButtons.length; i++) {
			if (o.getSource() == testButtons[i]) {
				vl.addTestInput(testInput[i]);
				break;
			}
		}
	}

}
