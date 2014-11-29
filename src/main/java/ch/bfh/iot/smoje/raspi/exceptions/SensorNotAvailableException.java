package ch.bfh.iot.smoje.raspi.exceptions;

/**
 * @author Matteo Morandi
 */
public class SensorNotAvailableException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4786602249548993758L;

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