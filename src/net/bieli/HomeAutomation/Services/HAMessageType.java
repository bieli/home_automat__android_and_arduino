package net.bieli.HomeAutomation.Services;

/**
 * HAMessageType interface
 * 
 * Typical messages definition    
 * 
 * @author Marcin Bielak <marcin.bieli@gmail.com>
 */

public interface HAMessageType {
	public byte SET_OUTPUT_DIGITAL = 1; 
	public byte SET_OUTPUT_ANALOG = 2;
	public byte GET_INPUT_DIGITAL= 4;
	public byte GET_INPUT_ANALOG = 8;
}
