package ch.bfh.iot.smoje.raspi.sensors;

import org.apache.logging.log4j.Logger;

import ch.bfh.iot.smoje.raspi.Main;
import ch.bfh.iot.smoje.raspi.common.SensorState;
import ch.bfh.iot.smoje.raspi.exceptions.ArduinoBusyException;

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
	private 		Logger 				logger 				= Main.logger;
	private			double				value;

	/*
	 * Constructor
	 */
	public Sensor (SensorType sensorType, String unit, String sensorId) {
		super();
		this.sensorType = sensorType;
		this.unit = unit;
		this.sensorId = sensorId;
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
		if(this.sensorId == null) return "Sensor does not exist on this smoje";
		
		boolean success = false;
		try {
			success = Main.arduinoController.getValueOverSerialConnection(this);
		} catch (ArduinoBusyException e) {
			logger.warn(e.getMessage(), e);
		}
		
		if(success) return String.valueOf(value);
		else return "failed";
		//TODO introduce mechanism to make sure value is plausible

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
	
	/**
	 * @return {@link SensorType}
	 */
	@Override
	public void setSensorValue(double value) {
		this.value = value;
	}
}
