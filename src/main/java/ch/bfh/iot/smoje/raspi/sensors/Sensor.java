package ch.bfh.iot.smoje.raspi.sensors;

import org.zu.ardulink.RawDataListener;

import ch.bfh.iot.smoje.raspi.common.SensorState;

/**
 * Represents a sensor
 * Contains methods for retrieving the value, unit and state of a sensor
 * @author Matteo Morandi
 */
public class Sensor implements SmojeSensor {
	
	private			SensorType		sensorType;
	private 		SensorState 	state 		= SensorState.OK;
	private final 	String 			unit;
	private final 	String 			sensorId;
	private	 		double			temporaryValue;

	/*
	 * Constructor
	 */
	public Sensor (SensorType sensorType, String unit, String sensorId) {
		super();
		this.sensorType = sensorType;
		this.unit = unit;
		this.sensorId = sensorId;
	}

	@Override
	public String getId() {
		return this.sensorId;
	}

	/**
	 * Sends sensorId to Arduino and waits for result.
	 * The result is parsed and returned in an appropriate form
	 * @return {@link String} with value
	 */
	@Override
	public String getValue() {
		final ArduinoConnector arduinoConnector = new ArduinoConnector();
		arduinoConnector.initialize();
		Thread t=new Thread() {
			public void run() {
				//the following line will keep this app alive for 10 seconds,
				//waiting for events to occur and responding to them (printing incoming messages to console).
				try {
					Thread.sleep(2500);
					arduinoConnector.writeData("3");
				} catch (InterruptedException ie) {}
			}
		};
		t.start();
		
		//TODO introduce mechanism to make sure value is plausible
		return String.valueOf(arduinoConnector.getIncomingData());
	}

	/**
	 * 
	 * @return {@link String} representation of unit (SI and NonSI)
	 */
	@Override
	public String getUnit() {
		return this.unit;
	}

	/**
	 * Gets the state of the sensor
	 * @return {@link SensorState}
	 */
	@Override
	public SensorState getStatus() {
		return this.state;
	}
	
	/**
	 * @return {@link SensorType}
	 */
	@Override
	public SensorType getSensorType() {
		return this.sensorType;
	}
}
