package ch.bfh.iot.smoje.raspi.sensors;

import java.util.Random;

public class TemperaturSensor implements ISensor {

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
	public String getStatus() {
		return "OK";
	}

	@Override
	public String getUnit() {
		return "Celsius";
	}

}
