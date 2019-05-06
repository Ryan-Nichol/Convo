package results;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;

import model.Bubble;
import model.Meeting;

public class TextReader {

	public static String readTopicFile(Meeting m, Bubble b) {

		String text = null;

		try {

			File f = new File("Meeting/" + m.getName() + "/Transcripts");

			FilenameFilter textFilter = new FilenameFilter() {
				public boolean accept(File dir, String name) {
					return name.toLowerCase().endsWith(".txt");
				}
			};

			File[] files = f.listFiles(textFilter);

			for (File file : files) {
				if (file.getName().equals(b.getName() + ".txt")) {
					text = readTextFile(file);
				}
			}

		} catch (IOException e) {

		}

		return text;
	}

	public static String readTextFile(File file) throws IOException {

		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String text = "";
		String line = br.readLine();

		while (line != null) {
			text += line;
			line = br.readLine();
		}

		return text;
	}

}
