package com.aiscad.agora.objects;

public class Twiit {

	private int id;
	private String text;
	private long timestamp;
	
	public Twiit(int id, String text, long timestamp) {
		super();
		this.id = id;
		this.text = text;
		this.timestamp = timestamp;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	 
}
