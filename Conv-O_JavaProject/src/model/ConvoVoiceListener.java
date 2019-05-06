package model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine.Info;
import javax.swing.JOptionPane;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechRecognitionResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;

import app.TestInterface;
import testing.STTtestInterface;

/**
 * This class handles all speech to text using IBM Watson. It passes the text
 * retrieved to the ConvoUnderstander
 *
 * @author Ryan Nichol
 *
 */

public class ConvoVoiceListener extends TestInterface implements Runnable {

	private STTtestInterface sti;
	private ConvoService service;

	private static boolean connected = true;
	private static SpeechToText speechToText;
	private static RecognizeOptions micOptions;
	private static AudioInputStream audio;

	// Microphone Settings
	private static int sampleRate = 16000;
	private static int sampleSizeinBits = 16;
	private static int channels = 1;
	private static boolean signed = true;
	private static boolean bigEndian = false;

	// IBM Watson Inactivity Timeout
	private static int timeout = -1;

	private ArrayList<Message> outgoing = new ArrayList<Message>();

	public ConvoVoiceListener(ConvoService service) {
		speechToText = new SpeechToText();
		sti = new STTtestInterface(this);
		this.service = service;
	}

	public void login(String username, String password) {
		speechToText.setUsernameAndPassword(username, password);
		System.out.println("Logged in");
		// speechToText.setSkipAuthentication(true);
	}

	public void run() {

		// Connects to a microphone on the computer
		connectToMicrophone();

		System.out.println("Running");

		speechToText.recognizeUsingWebSocket(micOptions, new BaseRecognizeCallback() {

			public void onTranscription(SpeechRecognitionResults speechResults) {

				System.out.println(speechResults.getResults().get(0).getAlternatives().get(0).getTranscript());

				if (speechResults.getResults().get(0).isFinalResults()) {
					System.out.println("Finished");
					System.out.println("RESULTS: " + speechResults.getResults().size() + " : " + "ALTERNATIVES: " + speechResults.getResults().get(0).getAlternatives().size());
					String text = speechResults.getResults().get(0).getAlternatives().get(0).getTranscript();
					Message msg = new Message(text, service.getConvoInterface().getZoomedBubble());
					outgoing.add(msg);
					sti.addText(text);
//					if (service.getMeeting() != null) {
//						try {
//							writeToTS(text);
//						} catch (IOException e) {
//							System.out.println("Transcript write failed.\n" + e.getMessage());
//						}
//					}
				}
			}

			@Override
			public void onDisconnected() {
				System.out.println("Disconnected");
			}

			@Override
			public void onInactivityTimeout(RuntimeException e) {
				System.out.println("Connection Timeout");
			}

		});
	}

	public void addTestInput(String s) {
		Message msg = new Message(s, service.getConvoInterface().getZoomedBubble());
		addText(new Message(s, service.getConvoInterface().getZoomedBubble()));
	}

	public ArrayList<Message> getOutgoingMessages() {
		return outgoing;
	}

	public void writeToTS(String text) throws IOException {
		String path = "Meeting/" + service.getMeeting().getName() + "/Transcripts/Full_Transcript.txt";
		FileWriter writer = new FileWriter(path, true);
		PrintWriter print_line = new PrintWriter(writer);

		print_line.printf("%s" + "%n", text);
		print_line.close();
	}

	public void addTestInput(String[] testInput) {
		// Run to output text
		for (String s : testInput) {
			addText(new Message(s, service.getConvoInterface().getZoomedBubble()));
			try {
				writeToTS(s);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void addText(Message msg) {
		sti.addText(msg.getMessage());
		outgoing.add(msg);
	}

	// Connects the application to the microphone
	public void connectToMicrophone() {

		try {
			AudioFormat format = new AudioFormat(sampleRate, sampleSizeinBits, channels, signed, bigEndian);
			Info info = new Info(TargetDataLine.class, format);

			if (!AudioSystem.isLineSupported(info)) {
				System.out.println("No Audio Detected");
			}

			try {
				TargetDataLine microphone = (TargetDataLine) AudioSystem.getLine(info);
				microphone.open(format);
				microphone.start();
				audio = new AudioInputStream(microphone);
			} catch (LineUnavailableException e) {

			}

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Default Microphone not Found! \nConvo requires a microphone to run.",
					"Error Message", JOptionPane.INFORMATION_MESSAGE);
			//System.exit(-1);
		}

		micOptions = new RecognizeOptions.Builder().audio(audio).interimResults(true).smartFormatting(true)
				.wordConfidence(true).inactivityTimeout(timeout)
				.contentType(HttpMediaType.AUDIO_RAW + "; rate=" + sampleRate).model(null).build();
	}
}
