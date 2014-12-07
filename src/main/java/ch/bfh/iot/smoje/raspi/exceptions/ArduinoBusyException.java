package ch.bfh.iot.smoje.raspi.exceptions;

/**
 * @author Matteo Morandi
 */
public class ArduinoBusyException extends Exception {


	public ArduinoBusyException() {
	}

	public ArduinoBusyException(String message) {
		super(message);
	}

	public ArduinoBusyException(Throwable cause) {
		super(cause);
	}

	public ArduinoBusyException(String message, Throwable cause) {
		super(message, cause);
	}

}
