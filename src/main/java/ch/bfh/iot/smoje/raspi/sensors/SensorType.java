package ch.bfh.iot.smoje.raspi.sensors;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains available sensor types and configuration file key
 * Reverse lookup is possible
 * @author Matteo Morandi
 */
public enum SensorType {
	CAMERA ("air.camera"),
	HUMIDITY ("air.humidity"),
	AIR_TEMPERATURE ("air.temperature"),
	ATMOSPHERIC_PRESSURE ("air.athmosphericpressure"),
	GEIGER ("air.geiger"),
	ACCELERATION ("air.acceleration"),
	WIND_SPEED ("air.windspeed"),
	WIND_DIRECTION ("air.winddirection"),
	RAIN_AMOUNT ("air.rainamount"),
	UV_LIGHT ("air.uvlight"),
	COMPASS ("air.compass"),
	WATER_TEMPERATURE ("water.temperature"),
	BATTERY_VOLTAGE("smoje.battery");
	
    private final String key;
	
    // Reverse-lookup map for getting a SensorType by key
    private static final Map<String, SensorType> lookup = new HashMap<String, SensorType>();
    
    static {
        for (SensorType sensorType : SensorType.values())
            lookup.put(sensorType.getKey(), sensorType);
    }

    private SensorType(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public static SensorType get(String key) {
        return lookup.get(key);
    }
}
