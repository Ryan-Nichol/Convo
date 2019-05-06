package model;

/**
 * This class represents a Message to store text and the bubble the text was
 * spoken in. Used to send detailed messages from ConvoListener to
 * ConvoUnderstander to ConvoClassifier
 * 
 * @author rnich
 *
 */
public class Message {

	private String message;
	private Bubble bubble;

	public Message(String message, Bubble bubble) {
		this.message = message;
		this.bubble = bubble;
	}

	public String getMessage() {
		return message;
	}

	public Bubble getBubble() {
		return bubble;
	}
}
