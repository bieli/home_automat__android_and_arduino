package net.bieli.HomeAutomation.Services.HaWebservice.model;

import java.util.Date;

public class UserData {
	private Date datetime;
	private String key;
	private String value;

	public UserData(Date datetime, String key, String value) {
		this.datetime = datetime;
		this.key = key;
		this.value = value;
	}
	public UserData(String key, String value) {
		this.key = key;
		this.value = value;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
}
