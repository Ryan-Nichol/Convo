package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

/**
 * 
 * Represent login user interface
 * 
 * @author Ryan Nichol
 *
 */
public class LoginInterface extends JFrame implements FocusListener {

	private JTextField classifierUsernameField, classifierPasswordField;
	private JTextField understanderUsernameField, understanderPasswordField;
	private JTextField speechUsernameField, speechPasswordField;

	private Color colorText = Color.BLACK;
	private Color colorHint = Color.GRAY;

	private final int FIELD_WIDTH = 565;
	private final int FIELD_HEIGHT = 50;

	private JPanel classifierPanel, understanderPanel, speechPanel;

	public LoginInterface() {
		super("Conv-O: Login IBM Services");
		this.setPreferredSize(new Dimension(700, 700));
		this.setLayout(null);

		Font font = new Font("Verdana", Font.PLAIN, 15);

		Border blackline = BorderFactory.createLineBorder(Color.black);
		TitledBorder border = BorderFactory.createTitledBorder(blackline, "Login - IBM Natural Language Classifier");
		border.setTitleJustification(TitledBorder.LEFT);

		classifierPanel = new JPanel();
		classifierPanel.setLayout(null);
		classifierPanel.setSize(625, 180);
		classifierPanel.setLocation(30, 30);
		classifierPanel.setBorder(border);

		border.setTitle("Login - IBM Natural Language Understanding");
		understanderPanel = new JPanel();
		understanderPanel.setLayout(null);
		understanderPanel.setSize(625, 180);
		understanderPanel.setLocation(30, 230);
		understanderPanel.setBorder(border);

		border.setTitle("Login - IBM Speech To Text");
		speechPanel = new JPanel();
		speechPanel.setLayout(null);
		speechPanel.setSize(625, 180);
		speechPanel.setLocation(30, 430);
		speechPanel.setBorder(border);

		classifierUsernameField = new JTextField("Username");
		classifierUsernameField.setLocation(30, 40);
		classifierUsernameField.setSize(FIELD_WIDTH, FIELD_HEIGHT);
		classifierUsernameField.addFocusListener(this);
		classifierUsernameField.setForeground(colorHint);
		classifierUsernameField.setFont(font);
		classifierPanel.add(classifierUsernameField);
		classifierPasswordField = new JTextField("Password");
		classifierPasswordField.setLocation(30, 100);
		classifierPasswordField.setSize(FIELD_WIDTH, FIELD_HEIGHT);
		classifierPasswordField.addFocusListener(this);
		classifierPasswordField.setForeground(colorHint);
		classifierPasswordField.setFont(font);
		classifierPanel.add(classifierPasswordField);

		understanderUsernameField = new JTextField("Username");
		understanderUsernameField.setLocation(30, 40);
		understanderUsernameField.setSize(FIELD_WIDTH, FIELD_HEIGHT);
		understanderUsernameField.addFocusListener(this);
		understanderUsernameField.setForeground(colorHint);
		understanderUsernameField.setFont(font);
		understanderPanel.add(understanderUsernameField);
		understanderPasswordField = new JTextField("Password");
		understanderPasswordField.setLocation(30, 100);
		understanderPasswordField.setSize(FIELD_WIDTH, FIELD_HEIGHT);
		understanderPasswordField.addFocusListener(this);
		understanderPasswordField.setForeground(colorHint);
		understanderPasswordField.setFont(font);
		understanderPanel.add(understanderPasswordField);

		speechUsernameField = new JTextField("Username");
		speechUsernameField.setLocation(30, 40);
		speechUsernameField.setSize(FIELD_WIDTH, FIELD_HEIGHT);
		speechUsernameField.addFocusListener(this);
		speechUsernameField.setForeground(colorHint);
		speechUsernameField.setFont(font);
		speechPanel.add(speechUsernameField);
		speechPasswordField = new JTextField("Password");
		speechPasswordField.setLocation(30, 100);
		speechPasswordField.setSize(FIELD_WIDTH, FIELD_HEIGHT);
		speechPasswordField.addFocusListener(this);
		speechPasswordField.setForeground(colorHint);
		speechPasswordField.setFont(font);
		speechPanel.add(speechPasswordField);

		add(classifierPanel);
		add(understanderPanel);
		add(speechPanel);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);

	}

	public void focusGained(FocusEvent o) {
		JTextField focusedField = (JTextField) o.getSource();
		focusOnField(focusedField);
	}

	public void focusLost(FocusEvent o) {
		JTextField focusedField = (JTextField) o.getSource();
		String hint = "";
		if (focusedField == classifierUsernameField || focusedField == understanderUsernameField
				|| focusedField == speechUsernameField) {
			hint = "Username";
		} else {
			hint = "Password";
		}
		loseFocusOnField(focusedField, hint);
	}

	public void loseFocusOnField(JTextField field, String hint) {
		if (field.getText().trim().equals("")) {
			field.setText(hint);
			field.setForeground(colorHint);
		}
	}

	public void focusOnField(JTextField field) {
		if (field.getForeground() == colorHint) {
			field.setText("");
			field.setForeground(colorText);
		}
	}
}
