package model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import gui.ConvoInterface;
import processing.core.PApplet;
import results.ConvoResults;

/**
 * This class is used as a container to store all the IBM services and give easy
 * access to any service This class also handles logging in, and starting up the
 * services in their own threads
 *
 * @author Ryan Nichol
 *
 */
public class ConvoService {

	private static ConvoVoiceListener convoVoiceListener;
	private ConvoUnderstander convoUnderstander;
	private ConvoClassifier convoClassifier;
	private ConvoInterface convoInterface;
	private ConvoResults convoResults;
	private Meeting meeting;

	public ConvoService() {

	}

	public ConvoService(PApplet convoInterface) {
		this.convoClassifier = new ConvoClassifier(this);
		this.convoUnderstander = new ConvoUnderstander(this);
		this.convoVoiceListener = new ConvoVoiceListener(this);
		this.convoInterface = (ConvoInterface) convoInterface;
		this.convoResults = new ConvoResults(this);
	}

	public ConvoService(ConvoVoiceListener vl, ConvoUnderstander cu, ConvoClassifier cc) {
		this.convoClassifier = cc;
		this.convoUnderstander = cu;
		this.convoVoiceListener = vl;
	}

	// Login to all IBM services
	public void loginDefaultServices() {
		convoClassifier.login("encrypted", "encrypted");
		convoVoiceListener.login("encrypted", "encrypted");
		convoUnderstander.login("encrypted", "encrypted");

		convoClassifier.setClassifierDataSetID("687c74x560-nlc-977");
	}

	// Creates a PDF of meeting results
	public void createPDF() {
		Runnable run = new Runnable() {

			public void run() {
				convoResults.printToPDF();
			}
		};
		Thread thread = new Thread(run);
		thread.start();
	}

	// Start all IBM/Convo services
	public void startServices() {
		Thread understanderThread = new Thread(convoUnderstander);
		Thread voiceThread = new Thread(convoVoiceListener);
		voiceThread.start();
		understanderThread.start();
		System.out.println("Services Started");
	}

	// Write text to the main transcript
	public void writeToTS(String text) throws IOException {
		String path = "Meeting/" + meeting.getName() + "/Transcripts/Full_Transcript.txt";
		FileWriter writer = new FileWriter(path, true);
		PrintWriter print_line = new PrintWriter(writer);

		print_line.printf("%S" + "%n", text);
		print_line.close();
	}

	// Write text to specified bubble transcript
	public void changeBubbleToTS(boolean in, String name) {
		try {
			if (in)
				writeToTS("\\" + name + " in.\\");
			else
				writeToTS("\\" + name + " out.\\");
		} catch (IOException e) {
			System.out.println("Transcript write failed.\n" + e.getMessage());
		}
	}

	// --------------------------------
	// Getter and Setters
	// --------------------------------
	public ConvoInterface getConvoInterface() {
		return convoInterface;
	}

	public void setConvoInterface(ConvoInterface convoInterface) {
		this.convoInterface = convoInterface;
	}

	public ConvoVoiceListener getConvoVoiceListener() {
		return convoVoiceListener;
	}

	public ConvoUnderstander getConvoUnderstander() {
		return convoUnderstander;
	}

	public ConvoClassifier getConvoClassifier() {
		return convoClassifier;
	}

	public void setConvoUnderstander(ConvoUnderstander convoUnderstander) {
		this.convoUnderstander = convoUnderstander;
	}

	public void setConvoClassifier(ConvoClassifier convoClassifier) {
		this.convoClassifier = convoClassifier;
	}

	public void setConvoVoiceListener(ConvoVoiceListener convoVoiceListener) {
		this.convoVoiceListener = convoVoiceListener;
	}

	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}

	public Meeting getMeeting() {
		return meeting;
	}

}
