package interfaceFuntions;

import controlP5.CColor;
import controlP5.ControlP5;
import processing.core.PApplet;
import processing.core.PFont;

/**
 * This class is used by the convoInterface class to fetch setup functions of
 * its UI It was separated from the ConvoInterface class due to their being 400
 * lines of setup code
 * 
 * @author Ryan Nichol
 *
 */
public class SetupFunctions {

	// Size of interface
	private int width = 1280;
	private int height = 700;

	// Declare variables
	private PApplet convoInterface;
	private CColor buttonColor;
	private PFont menuFont;
	private PFont buttonFont;

	// Setups of fonts, colors etc
	public SetupFunctions(PApplet convoInterface) {
		this.convoInterface = convoInterface;
		buttonColor = new CColor().setBackground(convoInterface.color(0, 45, 90, 255))
				.setActive(convoInterface.color(115, 194, 251, 255))
				.setForeground(convoInterface.color(0, 128, 255, 255));
		menuFont = convoInterface.createFont("Helvetica", 24);
		buttonFont = convoInterface.createFont("Helvetica", 26);
	}

	// Calculates position of menu item
	public int calculatePositionY(int buttonPosition, int baseY, int paddingY, int btnHeight) {
		return (baseY) + (btnHeight * buttonPosition) + (paddingY * buttonPosition);
	}

	// Creates and returns minimize control
	public ControlP5 createControlMinimize() {
		ControlP5 controlMinimize = new ControlP5(convoInterface);

		controlMinimize.addButton("maximizeMenu").setCaptionLabel("+").setFont(menuFont).setColor(buttonColor)
				.setPosition(0, 0).setSize(39, 39);
		controlMinimize.hide();

		return controlMinimize;
	}

	// Creates and returns player control
	public ControlP5 createControlPlayer() {
		ControlP5 controlPlayer = new ControlP5(convoInterface);

		controlPlayer.addButton("pauseMeeting").setCaptionLabel("PAUSE").setPosition(width - 100, 0)
				.setColor(buttonColor).setSize(100, 39).hide();
		controlPlayer.addButton("resumeMeeting").setCaptionLabel("Resume").setPosition(width - 100, 0)
				.setColor(buttonColor).setSize(100, 39).hide();

		controlPlayer.addBackground("          ").setPosition(0, 0)
				.setBackgroundColor(convoInterface.color(0, 0, 0, 40)).setSize(1280, 40);

		controlPlayer.addBackground("bubbleTimeBG").setPosition(width / 2 - 80, 150)
				.setBackgroundColor(convoInterface.color(0, 0, 0, 20)).setSize(170, 50).hide();

		controlPlayer.addTextlabel("bubbleTime").setCaptionLabel("00:00:00").setPosition(width / 2 - 75, 150)
				.setFont(convoInterface.createFont("CaviarDreams.tff", 40)).setColor(convoInterface.color(0, 0, 100))
				.setText("00:00:00").setSize(100, 50).hide();

		controlPlayer.addButton("nextBubble").setCaptionLabel(">")
				.setFont(convoInterface.createFont("CaviarDreams.tff", 30)).setPosition(1180, 660).setSize(100, 40)
				.hide();
		controlPlayer.addButton("previousBubble").setCaptionLabel("<")
				.setFont(convoInterface.createFont("CaviarDreams.tff", 30)).setPosition(1080, 660).setSize(100, 40)
				.hide();

		controlPlayer.getController("resumeMeeting").hide();

		controlPlayer.addTextlabel("meetingLabel").setPosition(width / 2 - 100, 0)
				.setFont(convoInterface.createFont("Arial.tff", 28)).setColor(convoInterface.color(1, 0, 0))
				.setColorBackground(convoInterface.color(255, 255, 255)).setSize(200, 50).setText("").show();
		controlPlayer.getController("meetingLabel").getValueLabel().align(3, 1);
		controlPlayer.addTextlabel("timeLabel").setCaptionLabel("00:00:00").setPosition(width - 240, 0)
				.setFont(buttonFont).setColor(convoInterface.color(0, 0, 100)).setText("00:00:00").setSize(100, 50)
				.hide();

		return controlPlayer;
	}

	// Creates and returns menu control
	public ControlP5 createControlMenu(PApplet p) {

		ControlP5 controlMenu = new ControlP5(p);

		int baseY = 0;
		int paddingX = 0;
		int paddingY = 1;

		int btnWidth = width / 5 - (paddingX * 2);
		int btnHeight = 55;

		controlMenu.addBackground("   ").setSize(width / 5, 308).setBackgroundColor(p.color(0, 0, 0, 5)).setPosition(0,
				281);

		controlMenu.addBackground("    ").setSize(width / 5, 50).setBackgroundColor(p.color(0, 0, 0, 20)).setPosition(0,
				281);

		controlMenu.addBackground(" ").setSize(width / 5, 242).setBackgroundColor(p.color(0, 0, 0, 40)).setPosition(0,
				40);

		controlMenu.addBackground("              ").setSize(width / 5, 242).setBackgroundColor(p.color(0, 0, 0, 40))
				.setPosition(0, 40 + 242 + 306);

		// controlMenu.addBackground("statBG")
		// .setSize(width/5, 308)
		// .setBackgroundColor(p.color(0,0,0,40))
		// .setPosition(0,281);

		controlMenu.addBackground("topBG").setSize(width / 5, 281).setBackgroundColor(p.color(0, 0, 0, 40))
				.setPosition(0, 0);

		controlMenu.addBackground("botBG").setSize(width / 5, 100).setBackgroundColor(p.color(0, 0, 0, 40))
				.setPosition(0, height - 100);
		// use true/false for smooth/no-smooth

		controlMenu.addButton("newMeeting").setCaptionLabel("   New Meeting").setFont(buttonFont).setColor(buttonColor)
				.setPosition(paddingX, calculatePositionY(0, baseY, paddingY, btnHeight)).setSize(btnWidth, btnHeight)
				.getCaptionLabel().toUpperCase(false).alignX(0);

		controlMenu.addButton("playMeeting").setCaptionLabel("   Start Meeting").setFont(buttonFont)
				.setColor(buttonColor).setPosition(paddingX, calculatePositionY(0, baseY, paddingY, btnHeight))
				.setSize(btnWidth, btnHeight).getCaptionLabel().toUpperCase(false).alignX(0);

		controlMenu.addButton("endMeeting").setCaptionLabel("   End Meeting").setFont(buttonFont).setColor(buttonColor)
				.setPosition(paddingX, calculatePositionY(0, baseY, paddingY, btnHeight)).setSize(btnWidth, btnHeight)
				.getCaptionLabel().toUpperCase(false).alignX(0);

		controlMenu.getController("endMeeting").hide();
		controlMenu.getController("playMeeting").hide();

		controlMenu.addButton("addClassifier").setCaptionLabel("   Add Data Set").setFont(buttonFont)
				.setColor(buttonColor).setPosition(paddingX, calculatePositionY(3, baseY, paddingY, btnHeight))
				.setSize(btnWidth, btnHeight).getCaptionLabel().toUpperCase(false).alignX(0);

		controlMenu.addButton("showAddBubbleDialog").setCaptionLabel("   Add Bubble").setFont(buttonFont)
				.setColor(buttonColor).setPosition(paddingX, calculatePositionY(1, baseY, paddingY, btnHeight))
				.setSize(btnWidth, btnHeight).getCaptionLabel().toUpperCase(false).alignX(0);

		controlMenu.addButton("takeScreenshot").setCaptionLabel("   Screenshot").setFont(buttonFont)
				.setColor(buttonColor).setPosition(paddingX, calculatePositionY(4, baseY, paddingY, btnHeight))
				.setSize(btnWidth, btnHeight).getCaptionLabel().toUpperCase(false).alignX(0);

		controlMenu.addButton("showRemoveBubbleDialog").setCaptionLabel("   Remove Bubble").setFont(buttonFont)
				.setColor(buttonColor).setPosition(paddingX, calculatePositionY(2, baseY, paddingY, btnHeight))
				.setSize(btnWidth, btnHeight).getCaptionLabel().toUpperCase(false).alignX(0);

		controlMenu.addButton("resultsToPDF").setCaptionLabel("   Results").setFont(buttonFont).setColor(buttonColor)
				.setPosition(paddingX, height - (btnHeight * 2) - baseY - paddingY).setSize(btnWidth, btnHeight)
				.getCaptionLabel().toUpperCase(false).alignX(0);

		controlMenu.addButton("exitProgram").setCaptionLabel("   Exit").setFont(buttonFont).setColor(buttonColor)
				.setPosition(paddingX, height - btnHeight - baseY).setSize(btnWidth, btnHeight).getCaptionLabel()
				.toUpperCase(false).alignX(0);

		controlMenu.addButton("minimizeMenu").setCaptionLabel("-").setFont(buttonFont).setColor(buttonColor)
				.setPosition(256, 0).setSize(39, 39);

		// //TEST BUTTONS
		// controlMenu.addButton("incBubbleSize")
		// .setCaptionLabel("Bubble Size +")
		// .setFont(menuFont)
		// .setPosition(960,580)
		// .setSize(width / 4, 40);
		//
		// controlMenu.addButton("decBubbleSize")
		// .setCaptionLabel("Bubble Size -")
		// .setFont(menuFont)
		// .setPosition(960,620)
		// .setSize(width / 4, 40);
		//
		// controlMenu.addButton("createTestMeeting")
		// .setCaptionLabel("Test Meeting")
		// .setFont(menuFont)
		// .setPosition(960,660)
		// .setSize(width / 4, 40);

		return controlMenu;
	}

	// Creates and returns statistics control
	public ControlP5 createControlStats(PApplet p) {
		ControlP5 controlStats = new ControlP5(p);
		int x = 20;

		controlStats.addTextlabel("labelRealTimeStats").setPosition(20, 290).setFont(p.createFont("Arial.tff", 23))
				.setColor(p.color(1, 0, 0)).setSize(255, 50).setText("Meeting Statistics").show();

		controlStats.addTextlabel("statsLabelMeetingName").setPosition(10, 325 + x)
				.setFont(p.createFont("Arial.tff", 15)).setColor(p.color(1, 0, 0)).setSize(200, 50)
				.setText("Meeting Name").show();

		controlStats.addTextlabel("statsLabelKeywords").setPosition(10, 425 + x).setFont(p.createFont("Arial.tff", 15))
				.setColor(p.color(1, 0, 0)).setSize(200, 50).setText("Keywords").show();

		controlStats.addTextlabel("statsLabelKeywordsCount").setPosition(10, 445 + x)
				.setFont(p.createFont("Arial.tff", 15)).setColor(p.color(1, 0, 0)).setSize(200, 50)
				.setText("     Count: ").show();

		controlStats.addTextlabel("statsLabelKeywordsTop").setPosition(10, 465 + x)
				.setFont(p.createFont("Arial.tff", 15)).setColor(p.color(1, 0, 0)).setSize(200, 50)
				.setText("     Top: ").show();

		controlStats.addTextlabel("statsLabelRelevance").setPosition(10, 505 + x).setFont(p.createFont("Arial.tff", 15))
				.setColor(p.color(1, 0, 0)).setSize(200, 50).setText("Relevance: ").show();

		controlStats.addTextlabel("statsLabelOverall").setPosition(10, 365 + x).setFont(p.createFont("Arial.tff", 15))
				.setColor(p.color(1, 0, 0)).setSize(200, 50).setText("Overall: ").show();

		controlStats.addTextlabel("statsLabelCurrently").setPosition(10, 345 + x).setFont(p.createFont("Arial.tff", 15))
				.setColor(p.color(1, 0, 0)).setSize(200, 50).setText("Currently: ").show();

		controlStats.addTextlabel("statsLabelDuration").setPosition(10, 385 + x).setFont(p.createFont("Arial.tff", 15))
				.setColor(p.color(1, 0, 0)).setSize(200, 50).setText("Duration: ").show();

		controlStats.getController("labelRealTimeStats").getValueLabel().align(3, 1);

		return controlStats;
	}

}
