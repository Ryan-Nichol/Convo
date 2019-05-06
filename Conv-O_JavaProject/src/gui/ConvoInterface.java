package gui;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import controlP5.ControlP5;
import interfaceFuntions.SetupFunctions;
import model.ConvoService;
import model.Meeting;
import model.MeetingTimer;
import model.Time;
import model.Bubble;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import toxi.geom.Rect;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.VerletPhysics2D;
import toxi.physics2d.behaviors.AttractionBehavior2D;

import javax.swing.*;

/**
 * This class displays the Convo real-time graph, the menus, and creates the
 * ConvoService class. Handles methods for creating and editing bubble topics,
 * zooming in on bubbles and displaying real-time statistics.
 * 
 * @author Ryan Nichol
 */
public class ConvoInterface extends PApplet {

	// Declare control menus
	private ControlP5 controlMenu;
	private ControlP5 controlMinimize;
	private ControlP5 controlPlayer;
	private ControlP5 controlStats;

	// Interface Background
	private static PImage backgroundImage;

	// Declare world variables
	private boolean worldIsZoomed = false;
	private boolean bubblePressed = false;
	private float zoomFactor = 1;
	private float newMouseX, newMouseY;

	// Declare physics and interface variables
	private VerletPhysics2D physics2D;
	private PVector zoomCenter = new PVector(0, 0);
	private Bubble zoomedBubble = null;
	private Vec2D mousePosition;
	private AttractionBehavior2D mouseAttractor;

	// Reference to current meeting and its timer
	private Meeting meeting = null;
	private MeetingTimer meetingTimer = null;

	// Declare service and interface setup functions
	private ConvoService service;
	private SetupFunctions setup;

	// Setup interface and login to IBM services
	public ConvoInterface() {
		service = new ConvoService(this);
		service.loginDefaultServices();
		service.startServices();
	}

	public void showAddBubbleDialog() {
		if (isMeetingCreated()) {
			AddBubbleDialog frame = new AddBubbleDialog(this, meeting.getAllBubbleNames());
		}
	}

	public void showRemoveBubbleDialog() {
		if (isMeetingCreated()) {
			if (meeting.getAllBubbleNames().size() != 0) {
				Object[] bubbles = meeting.getAllBubbleNames().toArray();
				RemoveBubbleDialog frame = new RemoveBubbleDialog(bubbles, this);
			} else {
				JOptionPane.showMessageDialog(null, "No bubbles to remove", "Error Message",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}

	public Bubble getZoomedParticle() {
		return zoomedBubble;
	}

	// ---------------------------------------
	// Interface Button Functionality
	// ---------------------------------------
	public void addBubble(String name, int[] time) {
		Bubble particle = meeting.addTopic(name, this);
		Time bubbleTime = new Time(time);
		particle.setTime(bubbleTime);
		physics2D.addParticle(particle);
		AttractionBehavior2D ab = new AttractionBehavior2D(particle, particle.getDiameter() * 4, -0.1f, 0.1f);
		particle.setBehavior(ab);
		physics2D.addBehavior(ab);
	}

	public void removeBubble(String name) {
		for (VerletParticle2D verletParticle : physics2D.particles) {
			Bubble p = (Bubble) verletParticle;
			if (p.getName().equals(name)) {
				physics2D.removeBehavior(p.getBehavior());
				physics2D.removeParticle(p);
				meeting.removeTopic(name);
				break;
			}
		}
	}

	public void removeAll() {
		physics2D.clear();
	}

	public void minimizeMenu() {
		controlMenu.hide();
		controlMinimize.show();
		if (controlStats != null) {
			controlStats.hide();
		}
		physics2D.setWorldBounds(new Rect((-width / 2) + 50, (-height / 2) + 50, (width) - 120, height - 120));
	}

	public void maximizeMenu() {
		controlMenu.show();
		controlMinimize.hide();
		if (controlStats != null) {
			controlStats.show();
		}
		physics2D.setWorldBounds(new Rect((-width / 2) + 300, (-height / 2) + 50, (width) - 420, height - 120));
	}

	public void playMeeting() {
		if (isMeetingCreated()) {
			controlMenu.get("playMeeting").hide();
			controlMenu.getController("endMeeting").show();
			controlPlayer.get("pauseMeeting").show();
			controlPlayer.get("timeLabel").show();
			controlPlayer.get("meetingLabel").show();
			controlMinimize.show();
			meeting.start();
		}
	}

	public void resumeMeeting() {
		controlPlayer.get("pauseMeeting").show();
		controlPlayer.get("resumeMeeting").hide();
		meeting.resumeMeeting();
	}

	public void pauseMeeting() {
		controlPlayer.get("pauseMeeting").hide();
		controlPlayer.get("resumeMeeting").show();
		meeting.pauseMeeting();
	}

	public void exitProgram() {
		System.exit(-1);
	}

	public void createTestMeeting() {
		Meeting meeting = new Meeting("Todays Forecast", "6f5217x399-nlc-395");
		meeting.loadDefaults();
		setMeeting(meeting);
		addBubble("Cold", new int[] { 0, 2, 30 });
	}

	public void takeScreenshot() {

		if (this.isMeetingCreated()) {
			File f = new File("Meeting/" + meeting.getName() + "/Screenshots");
			DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
			Date date = new Date();
			String s = meeting.getTime().durationToString();
			saveFrame("Meeting/" + meeting.getName() + "/Screenshots/" + s.replaceAll(":", "-") + ".png");
		}
	}

	public void takeFinalScreenshot() {
		saveFrame("Meeting/" + meeting.getName() + "/Screenshots/endOfMeeting.png");
	}

	public void newMeeting() {
		NewMeetingDialog newMeetingDialog = new NewMeetingDialog(service);
	}

	public void endMeeting() {
		resultsToPDF();
		meeting.endMeeting();
		controlMenu.getController("newMeeting").show();
		controlMenu.getController("endMeeting").hide();
	}

	public void incBubbleSize() {
		if (getZoomedParticle() != null) {
			getZoomedParticle().increaseDiamater();
			// below used for testing
			zoomedBubble.increaseColor();
			zoomedBubble.onTopic();
			meeting.onTopic();
		}
	}

	public void decBubbleSize() {
		if (getZoomedParticle() != null) {
			// below used for testing
			zoomedBubble.decreaseColor();
			zoomedBubble.offTopic();
			meeting.offTopic();
		}
	}

	public void nextBubble() {
		if (getZoomedParticle() != null) {
			int iteration = 0;
			for (Bubble b : meeting.getBubbles()) {
				if (b == zoomedBubble) {
					if (iteration + 1 != meeting.getBubbles().size()) {
						zoomBubbleOut(zoomedBubble);
						zoomedBubble = meeting.getBubbles().get(iteration + 1);
						zoomBubble(zoomedBubble);
					}
					break;
				}
				iteration++;
			}
		}
	}

	public void previousBubble() {
		if (getZoomedParticle() != null) {
			int iteration = 0;
			for (Bubble b : meeting.getBubbles()) {
				if (b == zoomedBubble) {
					if (iteration != 0) {
						zoomBubbleOut(zoomedBubble);
						zoomedBubble = meeting.getBubbles().get(iteration - 1);
						zoomBubble(zoomedBubble);

					}
					break;
				}
				iteration++;
			}
		}
	}

	public void addClassifier() {
		AddClassifierDialog acd = new AddClassifierDialog(service);
	}

	public void resultsToPDF() {
		if (isMeetingCreated()) {
			if (zoomedBubble != null) {
				zoomBubbleOut(zoomedBubble);
			}
			takeFinalScreenshot();
			final ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
			exec.schedule(new Runnable() {
				@Override
				public void run() {
					service.createPDF();
				}
			}, (long) 1f, TimeUnit.SECONDS);
		}
	}

	// ---------------------------------------
	// End of Interface Button Functionality
	// ---------------------------------------

	// ---------------------------------------
	// Setup Interface
	// ---------------------------------------
	public void settings() {
		size(1280, 700);
	}

	// Is called during class creation
	public void setup() {
		surface.setTitle("Conv-O");

		// Setup controllers
		setup = new SetupFunctions(this);
		controlPlayer = setup.createControlPlayer();
		controlMinimize = setup.createControlMinimize();
		controlMenu = setup.createControlMenu(this);

		backgroundImage = loadImage("Data/Images/ll.jpg");

		// Setup physics
		physics2D = new VerletPhysics2D();
		physics2D.setDrag(0.05f);
		physics2D.setWorldBounds(new Rect((-width / 2) + 300, (-height / 2) + 50, width - 420, height - 120));
	}

	// ---------------------------------------
	// Functionality to handle user input
	// ---------------------------------------
	public void mousePressed() {
		mousePosition = new Vec2D(newMouseX, newMouseY);
		contains(mousePosition);
		if (zoomedBubble == null) {
			mouseAttractor = new AttractionBehavior2D(mousePosition, 300, -0.1f);
			physics2D.addBehavior(mouseAttractor);
		}
	}

	public void mouseReleased() {
		if (bubblePressed && zoomedBubble != null && !zoomedBubble.isZoomed() && !worldIsZoomed
				&& !zoomedBubble.isLocked()) {
			zoomBubble(zoomedBubble);
		} else if (bubblePressed && zoomedBubble != null && zoomedBubble.isZoomed()) {
			zoomBubbleOut(zoomedBubble);
			zoomedBubble = null;
		}
		bubblePressed = false;
		physics2D.removeBehavior(mouseAttractor);
	}

	// Check if mouse click is on top of bubble
	private void contains(Vec2D posIn) {
		bubblePressed = false;

		for (int i = 0; i < physics2D.particles.size(); i++) {
			Bubble thisParticle = (Bubble) physics2D.particles.get(i);
			if (thisParticle.getPreviousPosition().distanceTo(posIn) < thisParticle.getDiameter() / 2) {
				bubblePressed = true;
				zoomedBubble = thisParticle;
			}
		}
	}

	// returns current zoomed bubble
	public Bubble getZoomedBubble() {
		return zoomedBubble;
	}

	// Zooms in on specified bubble
	private void zoomBubble(VerletParticle2D particleIn) {
		if (particleIn != null) {
			worldIsZoomed = true;
			particleIn.lock();
			zoomedBubble.setZoom(true);
			zoomedBubble.setActiveColor();
			meetingTimer.setBubble(zoomedBubble);
			zoomCenter.set(particleIn.x, particleIn.y);
			zoomFactor = 3;
			service.changeBubbleToTS(true, zoomedBubble.getName());
			controlPlayer.get("nextBubble").show();
			controlPlayer.get("previousBubble").show();
			controlPlayer.getController("bubbleTime").setValueLabel(zoomedBubble.getTime().durationToString());
			controlPlayer.get("bubbleTime").show();
			controlPlayer.get("bubbleTimeBG").show();
		}
	}

	// Zooms out of specified bubble
	private void zoomBubbleOut(VerletParticle2D particleIn) {
		if (particleIn != null && particleIn.isLocked()) {
			worldIsZoomed = false;
			particleIn.unlock();
			zoomedBubble.setZoom(false);
			meetingTimer.setBubble(null);
			zoomCenter.set(0, 0);
			zoomFactor = 1;
			service.changeBubbleToTS(false, zoomedBubble.getName());
			controlPlayer.get("nextBubble").hide();
			controlPlayer.get("bubbleTime").hide();
			controlPlayer.get("bubbleTimeBG").hide();
			controlPlayer.get("previousBubble").hide();
		}
	}

	// ---------------------------------------
	// Functionality to create and edit meetings
	// ---------------------------------------
	public void changeMeetingNamelabel(String text) {
		controlPlayer.getController("meetingLabel").setValueLabel(text);
	}

	public Meeting getMeeting() {
		return meeting;
	}

	public void setMeeting(Meeting meeting) {
		if (this.meeting != null) {
			resetMeeting();
		}
		if (controlStats == null) {
			controlStats = setup.createControlStats(this);
		} else {
			controlStats.dispose();
			controlStats = setup.createControlStats(this);
		}

		this.meeting = meeting;
		service.setMeeting(meeting);
		meetingTimer = new MeetingTimer(controlPlayer, controlStats, meeting);
		changeMeetingNamelabel(meeting.getName());
		controlMenu.getController("newMeeting").hide();
		controlMenu.getController("playMeeting").show();

		controlStats.getController("statsLabelMeetingName").setValueLabel(meeting.getName());

		new File("Meeting/" + meeting.getName()).mkdirs();
		new File("Meeting/" + meeting.getName() + "/Transcripts").mkdirs();
		new File("Meeting/" + meeting.getName() + "/Screenshots").mkdirs();

		try {
			File file = new File("Meeting/" + meeting.getName() + "/Transcripts/Full_Transcript.txt");
			if (file.exists()) {
				file.delete();
			}
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void resetMeeting() {
		if (zoomedBubble != null) {
			zoomBubbleOut(zoomedBubble);
		}
		controlPlayer.get("pauseMeeting").hide();
		controlPlayer.get("resumeMeeting").hide();
		controlPlayer.get("timeLabel").hide();
		meetingTimer.stop();
		physics2D.clear();
	}

	public boolean isMeetingCreated() {
		if (meeting == null) {
			JOptionPane.showMessageDialog(null, "You must create a meeting first", "Error Message",
					JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		return true;
	}

	// Draw the convo real-time graph
	public void draw() {

		fill(120);
		ellipse(width / 2, height / 2, 50, 50);
		newMouseX = ((mouseX - (width / 2)) / zoomFactor) + zoomCenter.x;
		newMouseY = ((mouseY - (height / 2)) / zoomFactor) + zoomCenter.y;

		background(backgroundImage);
		stroke(175);
		fill(175);
		rectMode(CENTER);

		physics2D.update();
		pushMatrix();

		translate(width / 2, height / 2);
		scale(zoomFactor);
		translate(-zoomCenter.x, -zoomCenter.y);

		for (int i = 0; i < physics2D.particles.size(); i++) {
			if (i > 0) {
				VerletParticle2D previous = physics2D.particles.get(i - 1);
				stroke(123, 123, 123);
				line(physics2D.particles.get(i).x, physics2D.particles.get(i).y, previous.x, previous.y);
			}
		}

		for (int i = 0; i < physics2D.particles.size(); i++) {
			Bubble p = (Bubble) physics2D.particles.get(i);
			try {
				p.display();
			} catch (NullPointerException e) {
				System.out.println("error at " + e.getMessage());
				e.printStackTrace();
			}
		}
		popMatrix();
	}
}
