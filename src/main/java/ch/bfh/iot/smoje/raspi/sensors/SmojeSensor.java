package ch.bfh.iot.smoje.raspi.sensors;

import ch.bfh.iot.smoje.raspi.common.SensorState;

public interface SmojeSensor {

	public String getId();

	public String getValue();
	
	public String getUnit();

	public SensorState getStatus();

	public SensorType getSensorType();

	public void setSensorValue(double value);

}