package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsResult;

/**
 * This class represents the results of a singular bubble, containing getters
 * and setters for results.
 * 
 * @author Ryan Nichol
 *
 */
public class BubbleResults {

	// Declare variables
	private HashMap<String, Integer> map = new HashMap();
	private float onTopic = 1, offTopic = 0;
	private float totalSentiment = 0;
	private int keywordCount = 0;
	private String averageSentiment = "None";
	private String averageCategory = "None";

	// Add keywords to bubble
	public void addKeywords(List<KeywordsResult> keywords) {
		for (KeywordsResult kr : keywords) {
			String keyword = kr.getText();
			totalSentiment += kr.getSentiment().getScore();
			keywordCount++;
			if (map.containsKey(keyword)) {
				map.put(keyword, map.get(keyword) + 1);
			} else {
				map.put(keyword, 1);
			}
		}
	}

	// Increase bubble relevancy
	public void increaseRelevancy() {
		onTopic++;
		System.out.println(onTopic + " : " + offTopic);
	}

	// Decrease bubble relevancy
	public void decreaseRelevancy() {
		offTopic++;
	}

	// ------------------------------
	// Getters and Setters
	// ------------------------------
	public float getOnTopicValue() {
		return onTopic;
	}

	public float getOffTopicValue() {
		return offTopic;
	}

	public String getBubbleStatus() {
		float rel = getAverageRelevancy();
		String status = "";

		if (rel >= 0.6f) {
			status += "Very Good";
		} else if (rel >= 0.4f) {
			status += "Good";
		} else if (rel > 0.2f) {
			status += "Average";
		} else {
			status += "Bad";
		}
		return status;
	}

	public String getSentimentResult() {

		double sent = (double) getAverageSentiment();

		if (Double.isNaN(sent)) {
			return "   --";
		} else {
			
			System.out.println(sent);
			
			if (sent > 0.2f) {
				return "Very High";
			} else if (sent > 0.1f) {
				return "High";
			} else if (sent > 0f) {
				return "Low";
			} else {
				return "Very Low";
			}
		}

	}

	public float getAverageRelevancy() {
		return onTopic / (onTopic + offTopic);
	}

	public void setAverageCategory(String category) {
		this.averageCategory = category;
	}

	public void setAverageSentiment(String sentiment) {
		this.averageSentiment = sentiment;
	}

	public String getAverageCategory() {
		return averageCategory;
	}

	public float getAverageSentiment() {
		return (totalSentiment / keywordCount);
	}

	public List<String> getAverageKeywordsList() {
		ArrayList<String> topThree = new ArrayList<String>();

		String keywordOne = "";
		String keywordTwo = "";
		String keywordThree = "";

		int first = 0;
		int second = 0;
		int third = 0;

		Set<String> keySet = map.keySet();
		for (String s : keySet) {
			int keyNumber = map.get(s);
			if (keyNumber > first) {
				first = keyNumber;
				keywordOne = s;
			} else if (keyNumber > second) {
				second = keyNumber;
				keywordTwo = s;
			} else if (keyNumber > third) {
				third = keyNumber;
				keywordThree = s;
			}
		}

		topThree.add(keywordOne);
		topThree.add(keywordTwo);
		topThree.add(keywordThree);

		return topThree;
	}

	public String getAverageKeywordsString() {

		List<String> topThree = getAverageKeywordsList();

		String keywords = "";
		if (!topThree.get(0).equals("")) {
			for (int i = 0; i < topThree.size(); i++) {
				keywords += topThree.get(i);
				if (i != topThree.size() - 1 && !topThree.get(1).equals("")) {
					keywords += ", ";
				}
			}
		} else {
			keywords = "None";
		}

		return keywords;
	}
}
