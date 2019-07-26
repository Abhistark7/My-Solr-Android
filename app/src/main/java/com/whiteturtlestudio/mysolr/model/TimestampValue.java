package com.whiteturtlestudio.mysolr.model;

/**
 * @author Abhishek Sahu, created at 12/05/19
 **/
public class TimestampValue {

	private String timestamp;

	private String value;

	public TimestampValue(String timestamp, String value) {
		this.timestamp = timestamp;
		this.value = value;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
