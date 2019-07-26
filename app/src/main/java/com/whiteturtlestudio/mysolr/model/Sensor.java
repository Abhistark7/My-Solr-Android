package com.whiteturtlestudio.mysolr.model;

import java.util.List;

/**
 * @author Abhishek Sahu, created at 12/05/19
 **/
public class Sensor {

	private List<TimestampValue> timestampValueList;

	public Sensor(List<TimestampValue> timestampValueList) {
		this.timestampValueList = timestampValueList;
	}

	public List<TimestampValue> getTimestampValueList() {
		return timestampValueList;
	}

	public void setTimestampValueList(List<TimestampValue> timestampValueList) {
		this.timestampValueList = timestampValueList;
	}
}
