package results;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import model.Bubble;
import model.ConvoService;
import model.Meeting;

public class ConvoResults {

	private ConvoPDF convoPDF;
	private ConvoService service;
	private Meeting meeting;

	public ConvoResults(ConvoService service) {
		this.service = service;
		this.convoPDF = new ConvoPDF(this);
	}

	public Meeting getMeeting() {
		return meeting;
	}

	public ArrayList<Bubble> getListBubbles() {
		ArrayList<Bubble> topics = meeting.getAllTopics();
		return topics;
	}

	public void computeAveragesAllBubbles() {
		TextReader tr = new TextReader();

		for (Bubble b : meeting.getBubbles()) {
			String transcript = tr.readTopicFile(meeting, b);
			if (transcript.length() > 30) {
				String avgCategory = service.getConvoUnderstander().getAverageCategory(transcript);
				String avgSentiment = service.getConvoUnderstander().getAverageSentiment(transcript,
						b.getResults().getAverageKeywordsList());
				b.getResults().setAverageCategory(avgCategory);
				b.getResults().setAverageSentiment(avgSentiment);
			}
		}
	}

	public void createBubbleTranscriptFiles() {

		ArrayList<Bubble> bubbles = meeting.getBubbles();
		for (Bubble b : bubbles) {

			File f = new File("Meeting/" + meeting.getName() + "/Transcripts/" + b.getName() + ".txt");
			try {
				if (f.exists()) {
					f.delete();
				}
				f.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			FileReader fr = new FileReader("Meeting/" + meeting.getName() + "/Transcripts/Full_Transcript.txt");
			BufferedReader br = new BufferedReader(fr);
			Bubble writeToBubble = null;
			boolean writingToBubble = false;

			String line = br.readLine();
			while (line != null) {
				System.out.println(line);

				if (!writingToBubble) {
					for (Bubble b : bubbles) {
						if (line.equals("\\" + b.getName().toUpperCase() + " IN.\\")) {
							writeToBubble = b;
							writingToBubble = true;
							break;
						}
					}
				} else {
					if (line.equals("\\" + writeToBubble.getName().toUpperCase() + " OUT.\\")) {
						writingToBubble = false;
					} else {
						writeToTS(writeToBubble.getName() + ".txt", line);
					}
				}

				line = br.readLine();
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void writeToTS(String filename, String text) throws IOException {
		String path = "Meeting/" + service.getMeeting().getName() + "/Transcripts/" + filename;
		FileWriter writer = new FileWriter(path, true);
		PrintWriter print_line = new PrintWriter(writer);

		print_line.printf("%s" + "%n", text);
		print_line.close();
	}

	public void printToPDF() {
		meeting = service.getConvoInterface().getMeeting();

		createBubbleTranscriptFiles();
		computeAveragesAllBubbles();

		convoPDF.createPDF();
		openPDF();

	}

	public void openPDF() {
		if (Desktop.isDesktopSupported()) {
			try {
				File f = new File("Meeting/" + meeting.getName() + "/Convo_" + meeting.getName() + ".pdf");
				Desktop.getDesktop().open(f);
			} catch (Exception ex) {
				// no application registered for PDFs
			}
		}
	}

}
