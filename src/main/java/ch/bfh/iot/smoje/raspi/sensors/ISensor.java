package ch.bfh.iot.smoje.raspi.sensors;

public interface ISensor {

	public abstract String getId();

	public abstract String getValue();
	
	public abstract String getUnit();

	public abstract String getStatus();

}