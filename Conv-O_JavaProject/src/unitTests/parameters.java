package unitTests;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ibm.watson.developer_cloud.speech_to_text.v1.model.KeywordResult;

import model.Bubble;

public class parameters {
	
	private final float MAX_DIAMETER = 140;
	private final float DIAMETER_INCREASE = 5;
	private final float MAX_HUE = 100f / 360;
	private final float MIN_HUE = 0;
	private final float DIFF_HUE = MAX_HUE / 20;
	
	@Test
    public void testBubbleName() {
    	Bubble bubble=new Bubble();
    	assertEquals(bubble.getName(), "TestBubble");
    }
	
	@Test
    public void testBubbleIncreaseDiameter() {
    	Bubble bubble=new Bubble();
    	Float d = bubble.getDiameter();
    	bubble.increaseDiamater();
    	assertEquals(bubble.getDiameter(), d+DIAMETER_INCREASE, 0.1f);
    }
	
	@Test
    public void testBubbleDecreaseDiameter() {
    	Bubble bubble=new Bubble();
    	Float d = bubble.getDiameter();
    	bubble.decreaseDiamater();
    	assertEquals(bubble.getDiameter(), d-DIAMETER_INCREASE, 0.1f);
    }
	
	@Test
    public void testMaxDiameter() {
    	Bubble bubble=new Bubble();
    	bubble.setDiameter(140);
    	Float d = bubble.getDiameter();
    	bubble.increaseDiamater();
    	assertEquals(bubble.getDiameter(), 140, 0.1f);
    }
}
