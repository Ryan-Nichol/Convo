package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import com.ibm.watson.developer_cloud.natural_language_classifier.v1.NaturalLanguageClassifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classification;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.ClassifiedClass;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.Classifier;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.ClassifierList;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.ClassifyOptions;
import com.ibm.watson.developer_cloud.natural_language_classifier.v1.model.CreateClassifierOptions;

import testing.NLCTestInterface;

/**
 *
 * This class is used to classify speech from the conversation using IBM Watson
 * to identify if the sentence is on topic
 *
 * @author Ryan Nichol
 *
 */
public class ConvoClassifier {

	// Declare variables
	private NaturalLanguageClassifier classifier;
	private final String END_POINT;
	private String classifierID;
	private int offTopicCount = 0;
	private final int OFFTOPICMAX = 3;

	private ConvoService service;

	// Tester interface used for debugging
	private NLCTestInterface nti = new NLCTestInterface();

	// Set service, end point and create json file for adding new classifiers
	public ConvoClassifier(ConvoService service) {
		this.service = service;
		END_POINT = "https://gateway.watsonplatform.net/natural-language-classifier/api";
		createJsonFile("Weather");
	}

	// Login to the IBM Classifier
	public boolean login(String username, String password) {
		classifier = new NaturalLanguageClassifier();
		classifier.setUsernameAndPassword(username, password);
		classifier.setEndPoint(END_POINT);

		return true;
	}

	// Set the id of the classifier data set
	public void setClassifierDataSetID(String id) {
		this.classifierID = id;
	}

	// Returns the IBM classifier
	public NaturalLanguageClassifier getNaturalLanguageClassifier() {
		return classifier;
	}

	// Returns a list of all the classifer data sets
	public ArrayList<String> getClassifiers() {
		ArrayList<String> classifierNames = new ArrayList<String>();
		ClassifierList list = classifier.getClassifiers().execute();
		for (Classifier c : list.getClassifiers()) {
			classifierNames.add(c.getName());
		}
		return classifierNames;
	}

	// classify specified text and return the results
	public Classification classifyText(String text) {
		if (nti.getActive()) {
			System.out.println("CLASSIFY");
			ClassifyOptions classifyOptions = new ClassifyOptions.Builder().classifierId(classifierID).text(text)
					.build();
			Classification classification = classifier.classify(classifyOptions).execute();
			ClassifiedClass topClass = classification.getClasses().get(0);
			double confidence = Double.parseDouble(new DecimalFormat("#.##").format(topClass.getConfidence()));
			nti.addText(text);

			// Check if message was on topic
			if (confidence >= 0.97f) {
				nti.addText(confidence + " ON TOPIC");
				bubbleOnTopic();
			} else {
				offTopicCount++;
				if (offTopicCount >= OFFTOPICMAX) {
					deductBubbleColor();
				}
				nti.addText(confidence + " OFF TOPIC");
			}
			return classification;
		} else {
			nti.addText("Received Message, Classifier is OFF");
			return null;
		}
	}

	// Add a new classifier data set to the IBM classifier
	public void addClassifier(String name, File trainingData) {
		createJsonFile(name);

		File f = new File("metadata.json");
		try {
			CreateClassifierOptions createOptions = new CreateClassifierOptions.Builder()
					.metadataFilename("metadata.json").metadata(f).trainingData(trainingData).build();
			classifier.createClassifier(createOptions).execute();
		} catch (FileNotFoundException e) {
			System.err.println("Error Adding Classifier: File Not Found");
		}
	}

	// Creates a JSON file used for adding new classifier data sets
	public void createJsonFile(String trainingDataName) {

		try {
			FileWriter fw = new FileWriter("metadata.json", false);
			fw.write("{\n\"language\":\"en\",\n\"name\":\"" + trainingDataName + "\"\n}");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteClassifier(String name) {
		// TODO delete classifier
	}

	public void bubbleOnTopic() {
		Bubble b = service.getConvoInterface().getZoomedBubble();
		Meeting m = service.getConvoInterface().getMeeting();
		m.onTopic();
		if (b != null) {
			b.onTopic();
		}
		offTopicCount = 0;
	}

	public void deductBubbleColor() {
		Bubble b = service.getConvoInterface().getZoomedBubble();
		Meeting m = service.getConvoInterface().getMeeting();
		offTopicCount++;
		m.offTopic();
		if (offTopicCount >= OFFTOPICMAX) {
			b.offTopic();
		}
	}
}
