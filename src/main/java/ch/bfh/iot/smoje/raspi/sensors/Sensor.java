package ch.bfh.iot.smoje.raspi.sensors;

import org.apache.logging.log4j.Logger;
import org.zu.ardulink.RawDataListener;

import ch.bfh.iot.smoje.raspi.SmojeServer;
import ch.bfh.iot.smoje.raspi.common.SensorState;

/**
 * Represents a sensor
 * Contains methods for retrieving the value, unit and state of a sensor
 * @author Matteo Morandi
 */
public class Sensor implements SmojeSensor {
	
	private			SensorType			sensorType;
	private 		SensorState 		state 				= SensorState.OK;
	private final 	String 				unit;
	private final 	String 				sensorId;
	private	 		double				temporaryValue;
	private 		Logger 				logger 				= SmojeServer.logger;
	private			ArduinoSensorController	arduinoController;
//	private			String				value;

	/*
	 * Constructor
	 */
	public Sensor (SensorType sensorType, String unit, String sensorId) {
		super();
		this.sensorType = sensorType;
		this.unit = unit;
		this.sensorId = sensorId;
		this.arduinoController = ArduinoSensorController.getInstance();
		logger.info("instantiated new sensor. SensorType: " + sensorType.name() + ", ID: " + sensorId);
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
		
		return arduinoController.getValueOverSerialConnection(sensorId);
		
//		Thread t = new Thread() {
//			public void run() {
//				//the following line will keep this app alive for 2 seconds,
//				//waiting for events to occur and responding to them (printing incoming messages to console).
//				try {
//					Thread.sleep(2000);
//					value = arduinoConnector.getValueOverSerialConnection(sensorId);
//				} catch (InterruptedException ie) {
//					value = null;
//					//TODO log
//				}
//			}
//		};
//		t.start();
//		
//		//TODO introduce mechanism to make sure value is plausible
//		return value;
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
