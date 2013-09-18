package net.bieli.HomeAutomation;

/**
 * HAMessage class
 * 
 * Data set for webservice message    
 * 
 * @author Marcin Bielak <marcin.bieli@gmail.com>
 */
public class HAMessage {
	private byte bit = 0;
	private boolean state = false;

	public HAMessage() { }

	public HAMessage(byte bit, boolean state) {
		this.bit = bit;
		this.state = state;
	}

	public byte getBit() {
		return bit;
	}
	public void setBit(byte bit) {
		this.bit = bit;
	}
	public boolean getState() {
		return state;
	}
	public void setState(boolean state) {
		this.state = state;
	}
}
