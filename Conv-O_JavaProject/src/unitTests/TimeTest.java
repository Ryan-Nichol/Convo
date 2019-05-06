package unitTests;

import static org.junit.Assert.*;

import org.junit.Test;

import model.Time;

public class TimeTest {

	@Test
	public void testTickSeconds() {
		Time time = new Time(new int[]{0,0,30});
		int seconds = time.getSeconds();
		time.tick();
		assertEquals(time.getSeconds(), seconds-1);
	}

	@Test
	public void testTickMinutes() {
		Time time = new Time(new int[]{0,1,0});
		int minutes = time.getMinutes();
		time.tick();
		assertEquals(time.getMinutes(), minutes-1);
	}
	
	@Test
	public void testTickHours() {
		Time time = new Time(new int[]{1,0,0});
		int hours = time.getHours();
		time.tick();
		assertEquals(time.getHours(), hours-1);
	}
	
	@Test
	public void testOvertime(){
		Time time = new Time(new int[]{0,0,0});
		int seconds = time.getSeconds();
		time.tick();
		assertEquals(time.getSeconds(), seconds+1);
	}
}
