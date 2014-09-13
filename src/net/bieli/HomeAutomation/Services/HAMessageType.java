package net.bieli.HomeAutomation.Services;

/**
 * HAMessageType interface
 * 
 * Typical messages definition    
 * 
 * @author Marcin Bielak <marcin.bieli@gmail.com>
 */

public interface HAMessageType {
	// types
	public byte SET_OUTPUT_DIGITAL = 1;
//TODO: need implementing below types in HAServiceImpl 
	public byte SET_OUTPUT_ANALOG = 2;
	public byte GET_INPUT_DIGITAL= 3;
	public byte GET_INPUT_ANALOG = 4;

	// masks for type SET_OUTPUT_DIGITAL
	public byte DIGITAL_DEVICE_0 = 0;
	public byte DIGITAL_DEVICE_1 = 1;
	public byte DIGITAL_DEVICE_2 = 2;
	public byte DIGITAL_DEVICE_3 = 4;

	// values for type SET_OUTPUT_DIGITAL
	public byte DIGITAL_STATE_LOW = 0;
	public byte DIGITAL_STATE_HIGH = 1;
}
