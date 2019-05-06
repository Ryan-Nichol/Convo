package unitTests;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import model.Bubble;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.runners.Parameterized.*;

@RunWith(Parameterized.class)
public class BubbleTest {

	private Bubble bubble;
	@Before
	public void Bubbleb(){
		//bubble=new Bubble(null, 6, "BubbleName", null);
	}
	
	// fields used together with @Parameter must be public
    @Parameter(0)
    public int m1;
    
    // creates the test data
    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] { { -1 }, { 0 }, { 1 } };
        return Arrays.asList(data);
    }


    @Test
    public void testBubbleOffTopicException() {
    	Bubble bubble=new Bubble();
    	assertEquals(bubble.getName(), "TestBubble");
    }

}
