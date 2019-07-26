package com.whiteturtlestudio.mysolr.model;

/**
 * @author Abhishek Sahu, created at 12/05/19
 **/
public class RegisterUser {

	private String name;

	private String email;

	private String phone;

	private String password;

	private String panelName;

	private String panelCapacity;

	private String panelUnit;

	private String panelAge;

	private Data data;

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPanelName() {
		return panelName;
	}

	public void setPanelName(String panelName) {
		this.panelName = panelName;
	}

	public String getPanelCapacity() {
		return panelCapacity;
	}

	public void setPanelCapacity(String panelCapacity) {
		this.panelCapacity = panelCapacity;
	}

	public String getPanelUnit() {
		return panelUnit;
	}

	public void setPanelUnit(String panelUnit) {
		this.panelUnit = panelUnit;
	}

	public String getPanelAge() {
		return panelAge;
	}

	public void setPanelAge(String panelAge) {
		this.panelAge = panelAge;
	}
}
