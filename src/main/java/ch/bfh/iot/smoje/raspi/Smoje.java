package ch.bfh.iot.smoje.raspi;

import java.util.HashMap;
import java.util.Map;

import ch.bfh.iot.smoje.raspi.actors.SmojeActor;
import ch.bfh.iot.smoje.raspi.sensors.RaspiCamera;
import ch.bfh.iot.smoje.raspi.sensors.Sensor;
import ch.bfh.iot.smoje.raspi.sensors.SensorType;
import ch.bfh.iot.smoje.raspi.sensors.SmojeSensor;

/**
 * Represents Smoje as a whole including Sensors and Actors
 * @author Matteo Morandi
 */
public class Smoje {
	
    private Map<String, SmojeSensor> sensors = new HashMap<String, SmojeSensor>();
    private Map<String, SmojeActor> actors = new HashMap<String, SmojeActor>();
    
    
    /*
     * Constructor
     */
    public Smoje(){
    	createSensors();
    	createActors();
    }
    
    /**
     * Creates all necessary sensors from configuration file
     */
	private void createSensors(){
		//goes trough all sensor keys that are enables in config file
		for(String key : Main.config.getAvailableSensorList()){
			
			String unit;
			String id;
			SensorType sensorType = SensorType.get(key);;
			
			if(key.equals("air.camera")){
				RaspiCamera camera = new RaspiCamera();
				sensors.put(camera.getId(), camera);
			}
			else{ //other sensors
				if (sensorType != null){
					unit = Main.config.getSensorUnit(key);
					id = Main.config.getSensorId(key);
					
					Sensor sensor = new Sensor(sensorType, unit, id);
					sensors.put(sensor.getId(), sensor);
				}
			}
		}
	}
	
	/**
	 * Creates all necessary actors
	 */
	private void createActors(){
		//No Actors yet
	}

	/**
	 * @return the sensors
	 */
	public Map<String, SmojeSensor> getSensors() {
		return sensors;
	}

	/**
	 * @return the actors
	 */
	public Map<String, SmojeActor> getActors() {
		return actors;
	}
	
	
}
