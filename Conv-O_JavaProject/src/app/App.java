package app;

import java.io.FileNotFoundException;

import gui.AttendeeDialog;
import gui.NewMeetingDialog;
import processing.core.PApplet;

/**
 * Runs the main application and the test application
 * 
 * @author Ryan Nichol
 */
public class App {
	public static void main(String[] args) throws FileNotFoundException {
		
		//NewMeetingDialog d = new NewMeetingDialog(null);
		//AttendeeDialog ad = new AttendeeDialog(null);
		
		PApplet.main("gui.ConvoInterface");
		//PApplet.main("app.TestInterface");
	}
}