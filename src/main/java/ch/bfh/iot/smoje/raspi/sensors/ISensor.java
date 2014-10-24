package ch.bfh.iot.smoje.raspi.sensors;

import ch.bfh.iot.smoje.raspi.common.SensorState;

public interface ISensor {

	public abstract String getId();

	public abstract String getValue();
	
	public abstract String getUnit();

	public abstract SensorState getStatus();

}