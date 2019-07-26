package com.whiteturtlestudio.mysolr.model;

/**
 * @author Abhishek Sahu, created at 12/05/19
 **/
public class Data {

	private Sensor current;

	private Sensor humidity;

	private Sensor intensity;

	private Sensor temperature;

	private Sensor voltage;

	public Sensor getCurrent() {
		return current;
	}

	public void setCurrent(Sensor current) {
		this.current = current;
	}

	public Sensor getHumidity() {
		return humidity;
	}

	public void setHumidity(Sensor humidity) {
		this.humidity = humidity;
	}

	public Sensor getIntensity() {
		return intensity;
	}

	public void setIntensity(Sensor intensity) {
		this.intensity = intensity;
	}

	public Sensor getTemperature() {
		return temperature;
	}

	public void setTemperature(Sensor temperature) {
		this.temperature = temperature;
	}

	public Sensor getVoltage() {
		return voltage;
	}

	public void setVoltage(Sensor voltage) {
		this.voltage = voltage;
	}
}
