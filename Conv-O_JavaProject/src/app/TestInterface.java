package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import controlP5.*;

import controlP5.CColor;
import controlP5.ControlP5;
import model.ConvoService;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

/**
 * 
 * This class represents the main testing interface for adding test input into
 * the main application
 * 
 * @author Ryan Nichol
 *
 */
public class TestInterface extends PApplet {

	private ControlP5 control;
	private PImage backgroundImage;

	private ArrayList<String> testInput = new ArrayList<String>();

	private long speedOfConversation = 1000;

	private Textarea textAreaConversation;
	private Textfield textFieldSpeed;

	private boolean stop = false;
	private ConvoService service;

	public void settings() {
		size(300, 700);
	}

	// Add all components to UI
	public void setup() {

		service = new ConvoService();

		backgroundImage = loadImage("Data/Images/ll.jpg");
		backgroundImage.pixelHeight = 700;
		backgroundImage.pixelWidth = 300;

		control = new ControlP5(this);

		CColor ccolor = new CColor().setBackground(color(0, 45, 90, 255)).setActive(color(115, 194, 251, 255))
				.setForeground(color(0, 128, 255, 255));
		PFont menuFont = createFont("Helvetica", 18);
		PFont btnFont = createFont("Helvetica", 26);

		control.addTextlabel("labelRealTimeStats").setPosition(0, 105).setFont(createFont("Arial.tff", 23))
				.setColor(color(1, 0, 0)).setSize(255, 50).setText("            Conversation").show();

		control.addBackground("topBG").setSize(300, 145).setBackgroundColor(color(0, 0, 0, 20)).setPosition(0, 0);

		control.addBackground("midBG").setSize(300, 400).setBackgroundColor(color(0, 0, 0, 5)).setPosition(0, 145);

		control.addButton("startTestWeatherMeeting").setCaptionLabel("          Test Weather").setFont(btnFont)
				.setColor(ccolor).setPosition(0, 0).setSize(300, 50).getCaptionLabel().toUpperCase(false).alignX(0);

		control.addButton("startTestTransportMeeting").setCaptionLabel("         Test Transport").setFont(btnFont)
				.setColor(ccolor).setPosition(0, 51).setSize(300, 50).getCaptionLabel().toUpperCase(false).alignX(0);

		control.addButton("scrollTop").setCaptionLabel("           top").setFont(menuFont).setColor(ccolor)
				.setPosition(151, 550).setSize(140, 30).getCaptionLabel().toUpperCase(false).alignX(0);

		control.addButton("scrollBottom").setCaptionLabel("         bottom").setFont(menuFont).setColor(ccolor)
				.setPosition(10, 550).setSize(139, 30).getCaptionLabel().toUpperCase(false).alignX(0);

		control.addButton("setSpeed").setCaptionLabel("           set speed").setFont(menuFont).setColor(ccolor)
				.setPosition(10, 581).setSize(190, 30).getCaptionLabel().toUpperCase(false).alignX(0);

		control.addButton("stopPressed").setCaptionLabel("                         stop").setFont(menuFont)
				.setColor(ccolor).setPosition(10, 650).setSize(280, 40).getCaptionLabel().toUpperCase(false).alignX(0);

		textAreaConversation = control.addTextarea("conversationTextArea").setColor(color(0))
				.setColorBackground(color(255, 255, 255, 29)).setFont(menuFont).setText("Hello").setPosition(5, 150)
				.setSize(290, 390);

		textFieldSpeed = control.addTextfield("speedField").setColorBackground(color(255, 255, 255)).setColor(color(0))
				.setText("1000").setPosition(202, 581).setSize(88, 30);
	}

	// Scrolls to the bottom of the coversation
	public void scrollBottom() {
		textAreaConversation.scroll(1);
	}

	// Scrolls to the top of the conversation
	public void scrollTop() {
		textAreaConversation.scroll(0);
	}

	public void stopPressed() {
		stop = true;
	}

	// Sets the speed of the conversation
	public void setSpeed() {
		try {
			speedOfConversation = Long.parseLong(textFieldSpeed.getText());
		} catch (Exception e) {

		}
	}

	// Runs a thread to slowly input lines from a text file as test input
	public void startTestWeatherMeeting() {

		textAreaConversation.setText("");

		if (service.getConvoVoiceListener() == null) {
			System.out.println("IS NULL");
		}

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					File file = new File("Data/transcript_modified.txt");

					FileReader fr = new FileReader(file);
					BufferedReader br = new BufferedReader(fr);

					String line = br.readLine();

					while (line != null && !stop) {
						testInput.add(line);
						addLineTextArea(line);
						service.getConvoVoiceListener().addTestInput(line);
						line = br.readLine();
						textAreaConversation.scroll(1);
						try {
							Thread.sleep(speedOfConversation);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					stop = false;
				} catch (IOException io) {
					System.out.println("Failed to read test data");
				}
			}
		});
		t.start();
	}

	// Adds line of text to conversation
	public void addLineTextArea(String line) {
		String x = textAreaConversation.getText();
		textAreaConversation.setText(x + "\n\n" + line);
	}

	public void draw() {

		fill(120);
		ellipse(width / 2, height / 2, 50, 50);

		// background(255, 255, 255, 20);
		background(backgroundImage);
		stroke(175);
		fill(175);
		rectMode(CENTER);

		pushMatrix();

		translate(width / 2, height / 2);

		popMatrix();
	}
}
