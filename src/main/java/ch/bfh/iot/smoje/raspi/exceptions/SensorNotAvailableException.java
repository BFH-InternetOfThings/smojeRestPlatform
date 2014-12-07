package ch.bfh.iot.smoje.raspi.exceptions;

/**
 * @author Matteo Morandi
 */
public class SensorNotAvailableException extends Exception {

	public SensorNotAvailableException() {
    }

    public SensorNotAvailableException(String message) {
        super(message);
    }

    public SensorNotAvailableException(Throwable cause) {
        super(cause);
    }

    public SensorNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }

}