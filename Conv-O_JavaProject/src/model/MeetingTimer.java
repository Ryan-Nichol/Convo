package model;

import java.util.Timer;
import java.util.TimerTask;

import controlP5.ControlP5;

/**
 * This class represents the meetings timer, it handles time for bubbles and the
 * meeting so all timers follow same tick time.
 * 
 * @author Ryan Nichol
 *
 */
public class MeetingTimer {

	// Declare variables
	private ControlP5 player, stats;
	private Bubble zoomedBubble;
	private Meeting meeting;
	private Timer timer;

	// Setup the meeting timer
	public MeetingTimer(ControlP5 player, ControlP5 stats, Meeting meeting) {
		this.player = player;
		this.stats = stats;
		this.meeting = meeting;
		timer = new Timer();
		timer.scheduleAtFixedRate(new Countdown(), 0, 1000);
	}

	public void setMeeting(Meeting meeting) {
		this.meeting = meeting;
	}

	public void setBubble(Bubble bubble) {
		String label = "";
		setMeetingTime(meeting.getTime());
		if (bubble != null) {
			setBubbleTime(bubble.getTime());
		}
		zoomedBubble = bubble;
	}

	private class Countdown extends TimerTask {

		@Override
		public void run() {
			if (!meeting.isPaused() && meeting.isRunning()) {
				meeting.tickTimer();
				setMeetingTime(meeting.getTime());
				if (zoomedBubble != null) {
					// zoomedBubble.setTime(tick(zoomedBubble.getTime()));
					zoomedBubble.getTime().tick();
					setBubbleTime(zoomedBubble.getTime());
				}

				setStatistics();
			}
		}
	}

	public void setStatistics() {
		stats.getController("statsLabelKeywordsCount")
				.setValueLabel(String.valueOf("     Count: " + meeting.getKeywordCount()));
		stats.getController("statsLabelKeywordsTop")
				.setValueLabel("     Top: " + meeting.getAverageKeywordsList().get(0));
		stats.getController("statsLabelDuration")
				.setValueLabel("Duration: " + meeting.getTime().englishDurationToString());
		stats.getController("statsLabelOverall").setValueLabel("Overall: " + meeting.getMeetingStatus());
		stats.getController("statsLabelCurrently").setValueLabel("Currently: " + meeting.getIsOnTopic());
		stats.getController("statsLabelRelevance")
				.setValueLabel("Relevance: " + meeting.getAverageRelevance() * 100 + "%");

	}

	public void setBubbleTime(Time time) {
		String bubbleTime = "";
		if (time.isOvertime()) {
			player.getController("bubbleTime").getValueLabel().setColor(-65536);// .setColor(-65536);
		} else {
			player.getController("bubbleTime").getValueLabel().setColor(-16777116);
		}
		bubbleTime += time.durationToString();
		player.getController("bubbleTime").setValueLabel(bubbleTime);
	}

	public void setMeetingTime(Time time) {
		String timeLabel = "";
		if (time.isOvertime()) {
			player.getController("timeLabel").getValueLabel().setColor(-65536);// .setColor(-65536);
		} else {
			player.getController("timeLabel").getValueLabel().setColor(-16777116);
		}
		timeLabel += time.durationToString();
		player.getController("timeLabel").setValueLabel(timeLabel);
	}

	public void stop() {
		timer.cancel();
	}

}
