package net.bieli.HomeAutomation.Services.HaHttp;

import net.bieli.HomeAutomation.Services.HAMessageType;

/**
 * HAMessage class
 * 
 * Data set for webservice message    
 * 
 * @author Marcin Bielak <marcin.bieli@gmail.com>
 */
public class HAMessage implements HAMessageType {
	// optional mask for digital logic, controllers pins or things specific for hardware
	private byte mask = 0;
	// default int values else value is real if realValue = true
	private int value = 0;
	// if true maximum two position after semicolon, when false value it's int
	private boolean realValue = false;
	// specific for data node message type
	private byte messageType = 0;

	public HAMessage() { }

	public HAMessage(int value, byte mask, byte messageType, boolean realValue) {
		this.value = value;
		this.mask = mask;
		this.realValue = realValue; 
		this.messageType = messageType;
	}

	public byte getMask() {
		return mask;
	}

	public void setMask(byte mask) {
		this.mask = mask;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean isRealValue() {
		return realValue;
	}

	public void setRealValue(boolean realValue) {
		this.realValue = realValue;
	}

	public byte getMessageType() {
		return messageType;
	}

	public void setMessageType(byte messageType) {
		this.messageType = messageType;
	}
}
