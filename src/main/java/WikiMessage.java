package main.java;


import java.util.concurrent.atomic.AtomicInteger;

public class WikiMessage {

	private static final AtomicInteger ID_COUNTER = new AtomicInteger();

	private int id;
	private String message;

	public WikiMessage() {
		this.id = ID_COUNTER.getAndIncrement();
	}

	public WikiMessage(String message) {
		this.id = ID_COUNTER.getAndIncrement();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getId() {
		return id;
	}

}
