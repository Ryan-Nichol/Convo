
package model;

import java.awt.Color;
import java.util.List;
import com.ibm.watson.developer_cloud.natural_language_understanding.v1.model.KeywordsResult;
import processing.core.*;
import toxi.geom.Vec2D;
import toxi.physics2d.VerletParticle2D;
import toxi.physics2d.behaviors.AttractionBehavior2D;

/**
 * Represent a single bubble/topic on the convo interface graph.
 * 
 * @author Ryan Nichol
 *
 */
public class Bubble extends VerletParticle2D {

	private final float MAX_DIAMETER = 140;
	private final float DIAMETER_INCREASE = 5;
	private final float MAX_HUE = 100f / 360;
	private final float MIN_HUE = 0;
	private final float DIFF_HUE = MAX_HUE / 20;

	private float diameter, red, blue, green;
	private float hue = 0;
	private float sat = 1;
	private float brightness = 1;

	private PFont font;
	private AttractionBehavior2D attractionBehaviour;
	private Time bubbleTime;
	private BubbleResults results;

	private String bubbleTopic = "";

	private boolean isZoomed = false;
	private int seconds = 0;
	private int minutes = 5;

	private PApplet convoInterface;

	// Setup bubble
	public Bubble(Vec2D p, float diameter, String name, PApplet convoInterface) {
		super(p);

		results = new BubbleResults();
		this.convoInterface = convoInterface;
		this.diameter = diameter;
		bubbleTopic = name;

		red = 0;
		green = 200;
		blue = 250;
	}

	public Bubble(){
		super(new Vec2D());
		
		results = new BubbleResults();
		
		this.diameter = 6;
		bubbleTopic = "TestBubble";
		
		red = 0;
		green = 200;
		blue = 250;
	}
	
	// add keywords to the bubble results
	public void addKeywords(List<KeywordsResult> keywords) {
		results.addKeywords(keywords);
	}

	// Returns bubbles results
	public BubbleResults getResults() {
		return results;
	}

	// Decreases the bubbles colour
	public void decreaseColor() {
		if (hue >= MIN_HUE) {
			hue -= DIFF_HUE;
			Color hsb = Color.getHSBColor(hue, sat, brightness);
			red = hsb.getRed();
			green = hsb.getGreen();
		} else {
			hue = MIN_HUE;
		}
	}

	// Increases the bubbles color
	public void increaseColor() {
		if (hue <= MAX_HUE) {
			hue += DIFF_HUE;
			Color hsb = Color.getHSBColor(hue, sat, brightness);
			red = hsb.getRed();
			green = hsb.getGreen();
		} else {
			hue = MAX_HUE;
		}
	}

	// Sets color of bubble
	public void setActiveColor() {
		if (blue != 0) {
			red = 64;
			green = 255;
			blue = 0;
			hue = MAX_HUE;
		}
	}

	public void offTopic() {
		decreaseColor();
		results.decreaseRelevancy();
	}

	public void onTopic() {
		increaseColor();
		increaseDiamater();
		results.increaseRelevancy();
	}

	public void setTime(Time time) {
		bubbleTime = time;
	}

	public AttractionBehavior2D getBehavior() {
		return attractionBehaviour;
	}

	public Time getTime() {
		return bubbleTime;
	}

	// Displays the bubble on the convo interface graph
	public void display() {
		// Sets ellipse to nostroke and centered
		convoInterface.noStroke();
		convoInterface.ellipseMode(3);

		// Draw two shadows of bubble
		convoInterface.fill(180, 180, 180);
		convoInterface.ellipse(x + 0.7f, y + 0.9f, diameter + 1f, diameter + 1f);
		convoInterface.fill(120, 120, 120);
		convoInterface.ellipse(x + 0.5f, y + 0.7f, diameter + 0.4f, diameter + 0.4f);

		// Draw bubble above shadows to graph
		font = convoInterface.createFont("Arial Bold", 20);
		convoInterface.fill(red, green, blue);
		convoInterface.ellipse(x, y, diameter, diameter);
		convoInterface.fill(0);
		convoInterface.textAlign(PConstants.CENTER);
		convoInterface.textFont(font);
		convoInterface.text(bubbleTopic, x, y + 5);
	}

	public float getDiameter() {
		return diameter;
	}

	public void setDiameter(float inD) {
		diameter = inD;
	}

	public void increaseDiamater() {
		if (diameter < MAX_DIAMETER)
			diameter += DIAMETER_INCREASE;
	}

	public void decreaseDiamater() {
		diameter -= DIAMETER_INCREASE;
	}

	public void setBehavior(AttractionBehavior2D attractionBehaviour) {
		this.attractionBehaviour = attractionBehaviour;
	}

	public void setBubbleName(String name) {
		bubbleTopic = name;
	}

	public boolean isZoomed() {
		return isZoomed;
	}

	public void setZoom(boolean newZoomBoolean) {
		isZoomed = newZoomBoolean;
	}

	public String getName() {
		return bubbleTopic;
	}
}
