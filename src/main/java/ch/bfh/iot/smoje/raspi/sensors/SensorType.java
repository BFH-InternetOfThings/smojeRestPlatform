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
	AIR_TEMPERATURE ("air.humidity"),
	ATMOSPHERIC_PRESSURE ("air.humidity"),
	GEIGER ("air.humidity"),
	ACCELERATION ("air.humidity"),
	WIND_SPEED ("air.humidity"),
	WIND_DIRECTION ("air.humidity"),
	RAIN_AMOUNT ("air.humidity"),
	UV_LIGHT ("air.humidity"),
	COMPASS ("air.humidity"),
	WATER_TEMPERATURE ("air.humidity");
	
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
