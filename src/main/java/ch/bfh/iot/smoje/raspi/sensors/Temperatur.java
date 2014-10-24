package ch.bfh.iot.smoje.raspi.sensors;

import java.util.Random;

import ch.bfh.iot.smoje.raspi.common.SensorState;

public class Temperatur implements ISensor {

	SensorState state = SensorState.OK;
	
	@Override
	public String getId() {
		return "tempAir";
	}

	@Override
	public String getValue() {
	int min = -5;
	int max = 20;
	
	Integer temp = new Random().nextInt((max - min) + 1) + min;
	
	return 	temp.toString();
	}

	@Override
	public SensorState getStatus() {
		return state;
	}

	@Override
	public String getUnit() {
		return "Celsius";
	}

}
