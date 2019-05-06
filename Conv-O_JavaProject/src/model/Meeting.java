package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsResult;

import processing.core.PApplet;
import toxi.geom.Vec2D;

/**
 * Represents a Convo Meeting and stores relevant data like all keywords said,
 * total relevance, total time etc.
 * 
 * @author Ryan Nichol
 *
 */
public class Meeting {

	// Declare variables
	private String name;
	private String classifierID;
	private HashMap<String, Integer> map = new HashMap();
	private ArrayList<String> attendees = new ArrayList<String>();
	private ArrayList<String> absentees = new ArrayList<String>();
	private ArrayList<Bubble> topics = new ArrayList<Bubble>();
	private ArrayList<String> topicNames = new ArrayList<String>();
	private ArrayList<Float> relevancyPoints = new ArrayList<Float>();
	private float relevancyRecordInterval = 5;
	private float onTopic = 0, offTopic = 1, keywordCount = 0;
	private boolean isOnTopic = true;
	private Time time;

	private boolean paused;
	private boolean running;

	// Setup the meeting
	public Meeting(String name, String id) {
		this.name = name;
		this.classifierID = id;
		relevancyPoints.add(onTopic / (onTopic + offTopic));
	}

	// Add a bubble to the meeting
	public Bubble addTopic(String name, PApplet parent) {
		Bubble bubble = new Bubble(Vec2D.randomVector().scale(5).addSelf(0, -320), 40, name, parent);
		topics.add(bubble);
		topicNames.add(name);

		return bubble;
	}

	// Remove a bubble from the meeting
	public void removeTopic(String name) {
		int index = topicNames.indexOf(name);
		topicNames.remove(name);
		topics.remove(index);
	}

	// Get the total number of keywords
	public int getKeywordCount() {
		return (int) keywordCount;
	}

	// Returns if the meeting is currently on topic
	public String getIsOnTopic() {
		if (isOnTopic) {
			return "On Topic";
		} else {
			return "Off Topic";
		}
	}
	
	// Adds keywords to the keyword Hashmap
	public void addKeywords(List<KeywordsResult> keywords) {
		for (KeywordsResult kr : keywords) {
			String keyword = kr.getText();
			keywordCount++;
			if (map.containsKey(keyword)) {
				map.put(keyword, map.get(keyword) + 1);
			} else {
				map.put(keyword, 1);
			}
		}
		keywordCount++;
	}

	public void onTopic() {
		onTopic++;
		isOnTopic = true;
	}

	public void offTopic() {
		offTopic++;
		isOnTopic = false;
	}
	
	// -----------------------------------------
	// Getters and Setters
	// -----------------------------------------
	public void setAttendance(ArrayList<String> attendees, ArrayList<String> absentees){
		this.attendees=attendees;
		this.absentees=absentees;
	}

	public ArrayList<String> getAttendees(){
		return attendees;
	}
	
	public ArrayList<String> getAbsentees(){
		return absentees;
	}
	
	public Bubble getBubble(int index) {
		return topics.get(index);
	}

	public ArrayList<Bubble> getAllTopics() {
		return topics;
	}

	public ArrayList<Bubble> getBubbles() {
		return topics;
	}

	public ArrayList<String> getAllBubbleNames() {
		return topicNames;
	}

	public String getBubbleName(int index) {
		return topicNames.get(index);
	}

	public void loadDefaults() {
		time = new Time(new int[] { 0, 20, 0 });
	}

	public String getName() {
		return name;
	}

	public void start() {
		paused = false;
		running = true;
	}

	public void pauseMeeting() {
		paused = true;
	}

	public void resumeMeeting() {
		paused = false;
	}

	public void endMeeting() {
		running = false;
	}

	public boolean isRunning() {
		return running;
	}

	public boolean isPaused() {
		return paused;
	}

	public float getAverageRelevance() {
		return onTopic / (onTopic + offTopic);
	}

	public ArrayList<Float> getRelevancePoints() {
		return relevancyPoints;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	// Meeting timer to tick for relevancy points
	public void tickTimer() {
		time.tick();
		relevancyRecordInterval--;
		if (relevancyRecordInterval == 0) {
			relevancyPoints.add(onTopic / (onTopic + offTopic));
			relevancyRecordInterval = 5;
		}
	}

	// Gets the current status of the meeting
	public String getMeetingStatus() {
		float rel = getAverageRelevance();
		String status = "";

		if (rel >= 0.6f) {
			status += "Very Good";
		} else if (rel >= 0.4f) {
			status += "Good";
		} else if (rel >= 0.3f) {
			status += "Average";
		} else if (rel >= 0.2f) {
			status += "Below Average";
		} else {
			status += "Bad";
		}

		return status;
	}

	// Gets the top three keywords spoken in the meeting
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
}
