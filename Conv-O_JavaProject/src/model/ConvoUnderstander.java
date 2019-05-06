package model;

import java.util.ArrayList;
import java.util.List;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalysisResults;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.AnalyzeOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.CategoriesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.Features;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsResult;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.SemanticRolesOptions;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.SentimentOptions;

import testing.NLUTestInterface;

/**
 * This class handles or natural language understanding via IBM Watson. If a
 * keyword threshold is satisfied the text will be passed to the
 * ConvoClassifier.
 *
 * @author Ryan Nichol
 *
 */
public class ConvoUnderstander implements Runnable {

	// Declare variables
	private NaturalLanguageUnderstanding nlu = null;
	private ConvoService service = null;
	private boolean active = true;
	private NLUTestInterface nti = new NLUTestInterface();
	private int keywordsRequired = 0;

	private final String USERNAME;
	private final String PASSWORD;
	private final String VERSION;

	// Setup natural language classifier
	public ConvoUnderstander(ConvoService service) {

		USERNAME = "encrypted";
		PASSWORD = "encrypted";
		VERSION = "2017-02-27";

		this.nlu = new NaturalLanguageUnderstanding(VERSION);
		this.service = service;
	}

	// Login to natural language classifier
	public void login(String username, String password) {
		nlu.setUsernameAndPassword(username, password);
	}

	// Runs message text through nlu and returns the results
	// if message text returns with enough keywords, message will be passed to
	// classifier
	public AnalysisResults analyzeMessage(Message msg) {

		if (service.getMeeting() != null) {
			if (service.getMeeting().isRunning()) {
				try {
					CategoriesOptions categories = new CategoriesOptions();
					SemanticRolesOptions semanticRoles = new SemanticRolesOptions.Builder().build();
					KeywordsOptions keywords = new KeywordsOptions.Builder().sentiment(true).emotion(true).limit(30)
							.build();
					Features features = new Features.Builder().keywords(keywords).build();
					AnalyzeOptions parameters = new AnalyzeOptions.Builder().text(msg.getMessage()).features(features)
							.build();
					AnalysisResults response = nlu.analyze(parameters).execute();
					if (response.getKeywords().size() >= keywordsRequired) {
						nti.addText(response.getKeywords().size() + ": " + msg.getMessage());
						service.getConvoClassifier().classifyText(msg.getMessage());

						if (msg.getBubble() != null) {
							msg.getBubble().addKeywords(response.getKeywords());
						}
						service.getMeeting().addKeywords(response.getKeywords());

					} else {
						nti.addErrorText("NOT ENOUGH KEYWORDS " + msg.getMessage());
					}
					return response;
				} catch (Exception e) {
					nti.addErrorText("NOT ENOUGH TEXT " + msg.getMessage());
				}
			}
		}

		return null;
	}

	// Returns list of keywords from Analysis results
	public void getKeywords(AnalysisResults response) {
		for (KeywordsResult kr : response.getKeywords()) {
			nti.addText(kr.getText());
		}
	}

	// Runs text through nlu and returns string of the average category
	public String getAverageCategory(String text) {

		try {
			CategoriesOptions categories = new CategoriesOptions();
			Features features = new Features.Builder().categories(categories).build();
			AnalyzeOptions parameters = new AnalyzeOptions.Builder().text(text).features(features).build();
			AnalysisResults response = nlu.analyze(parameters).execute();

			String category = response.getCategories().get(0).getLabel();
			String avgCategory[] = category.substring(1).split("/");
			return avgCategory[avgCategory.length - 1];
		} catch (Exception e) {
			return "Not Found";
		}
	}

	// Runs text and keywords through nlu and returns a string of the average
	// sentiment value
	public String getAverageSentiment(String text, List<String> keywords) {
		try {
			SentimentOptions sentimentOptions = new SentimentOptions.Builder().targets(keywords).build();
			Features features = new Features.Builder().sentiment(sentimentOptions).build();
			AnalyzeOptions parameters = new AnalyzeOptions.Builder().text(text).features(features).build();
			AnalysisResults response = nlu.analyze(parameters).execute();

			String sentimentValue = response.getSentiment().getTargets().get(0).getScore().toString();
			return sentimentValue;
		} catch (Exception e) {
			return "Not Found";
		}
	}

	// A thread that constantly listens and retrieves for incoming messages from
	// the voice listener
	public void run() {
		// TODO Auto-generated method stub
		while (active) {
			if (nti.isActive()) {
				ArrayList<Message> messages = service.getConvoVoiceListener().getOutgoingMessages();
				if (!messages.isEmpty()) {
					analyzeMessage(messages.get(0));
					messages.remove(0);
				}
			}
			try {
				Thread.sleep(1);// Required or will not work
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
