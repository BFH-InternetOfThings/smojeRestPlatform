package ch.bfh.iot.smoje.raspi.exceptions;

/**
 * @author Matteo Morandi
 */
public class ArduinoNotReachableException extends Exception {

	public ArduinoNotReachableException() {
	}

	public ArduinoNotReachableException(String message) {
		super(message);
	}

	public ArduinoNotReachableException(Throwable cause) {
		super(cause);
	}

	public ArduinoNotReachableException(String message, Throwable cause) {
		super(message, cause);
	}

}
