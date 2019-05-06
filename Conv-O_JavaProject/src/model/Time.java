package model;

/**
 * Time class used by any object that requires to record its own time Meeting
 * timer will retrieve this class to tick the timer
 * 
 * @author Ryan Nichol
 *
 */
public class Time {

	private int seconds, minutes, hours;
	private final int[] INITIAL_TIME;
	private int[] timeLeft;
	private int[] duration;
	private boolean overtime = false;

	public Time(int[] time) {
		INITIAL_TIME = time;
		timeLeft = time;
		hours = time[0];
		minutes = time[1];
		seconds = time[2];
		duration = new int[] { 0, 0, 0 };
	}

	public int getSeconds() {
		return seconds;
	}

	public int getMinutes() {
		return minutes;
	}

	public int getHours() {
		return hours;
	}

	public void tick() {
		tickDuration();
		if (overtime) {
			tickOvertime();
		} else {
			if (seconds <= 0) {
				if (minutes <= 0) {
					if (hours == 0) {
						overtime = true;
						tickOvertime();
					} else {
						minutes = 59;
						hours--;
						seconds = 59;
					}
				} else {
					seconds = 59;
					minutes--;
				}
			} else {
				seconds--;
			}
		}
	}

	public boolean isOvertime() {
		return overtime;
	}

	// Add one second to times total duration
	private void tickDuration() {
		int hours = duration[0];
		int min = duration[1];
		int sec = duration[2];

		sec++;
		if (sec == 60) {
			sec = 0;
			min++;
			if (min == 60) {
				min = 0;
				hours++;
			}
		}

		duration[0] = hours;
		duration[1] = min;
		duration[2] = sec;
	}

	// Add one second to times overtime
	private void tickOvertime() {
		if (seconds >= 59) {
			if (minutes >= 59) {
				hours++;
				minutes = 0;
			} else {
				minutes++;
				seconds = 0;
			}
		} else {
			seconds++;
		}
	}

	public int[] getTimeLeft() {
		return timeLeft;
	}

	// Return time left as a string
	public String englishDurationToString() {
		String sHours = String.valueOf(duration[0]);
		String sMinutes = String.valueOf(duration[1]);
		String dur = "";
		if (duration[0] != 0) {
			dur += sHours + " Hours " + sMinutes + " Minutes";
		} else {
			dur += sMinutes + " Minutes";
		}

		return dur;
	}

	// Return duration as a string
	public String durationToString() {
		String sHours = String.valueOf(duration[0]);
		String sMinutes = String.valueOf(duration[1]);
		String sSeconds = String.valueOf(duration[2]);
		if (sHours.length() == 1) {
			sHours = "0" + sHours;
		}
		if (sMinutes.length() == 1) {
			sMinutes = "0" + sMinutes;
		}
		if (sSeconds.length() == 1) {
			sSeconds = "0" + sSeconds;
		}
		return (sHours + ":" + sMinutes + ":" + sSeconds);
	}

	// Return total time as a string
	public String timeToString() {
		String sHours = String.valueOf(hours);
		String sMinutes = String.valueOf(minutes);
		String sSeconds = String.valueOf(seconds);
		if (sHours.length() == 1) {
			sHours = "0" + sHours;
		}
		if (sMinutes.length() == 1) {
			sMinutes = "0" + sMinutes;
		}
		if (sSeconds.length() == 1) {
			sSeconds = "0" + sSeconds;
		}
		return (sHours + ":" + sMinutes + ":" + sSeconds);
	}

}
