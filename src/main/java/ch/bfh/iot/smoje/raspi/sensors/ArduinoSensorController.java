package ch.bfh.iot.smoje.raspi.sensors;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.logging.log4j.Logger;
import org.zu.ardulink.Link;

import ch.bfh.iot.smoje.raspi.SmojeServer;
import ch.bfh.iot.smoje.raspi.exceptions.SensorNotAvailableException;

/**
 * Class handles reading of sensor data, creates connection to Arduino board and gets values for sensors
 * @author Matteo Morandi
 */
public class ArduinoSensorController {
	
    private static final 	String 					CONNECTION_PORT 		= "/dev/ttyS80";
    private static final 	int 					BAUD_RATE 				= 9600;
    public 	static 			Link 					arduinoLink 			= Link.getDefaultInstance();
    private 				Logger 					logger 					= SmojeServer.logger;
    private 				boolean					isConnected				= false;
    private 				ArrayList<SmojeSensor> 	arduinoSensorsOnSmoje 	= new ArrayList<>();

    /*
     * Constructor
     */
	public ArduinoSensorController(){
		createSensors();
		//isConnected = connectToArduino();
		
		if(isConnected){
			logger.info("Arduino connected :)");
		}
		else{
			logger.info("Arduino not connected :'(");
		}
	}
	
	/**
	 * Creates all instances of available sensors and adds them to {@link ArrayList}
	 */
	private void createSensors(){
		//goes trough all sensor keys that are enables in config file
		for(String key : SmojeServer.config.getAvailableSensorList()){
			//gather information about sensor
			SensorType sensorType = SensorType.get(key);
			String unit = SmojeServer.config.getSensorUnit(key);
			String id = SmojeServer.config.getSensorId(key);
		
			//instantiate new sensor with information
			arduinoSensorsOnSmoje.add(new Sensor(sensorType, unit, id));
		}
	}
	
	/**
	 * Gathers the value of the sensor given in parameter
	 * @param sensorType {@link SensorType} of requested sensor
	 * @return {@link String} with value
	 * @throws SensorNotAvailableException if sensor is not available
	 */
	public String getSensorData(SensorType sensorType) throws SensorNotAvailableException{
		SmojeSensor requestedSensor = null;
		
		for(SmojeSensor sensor : arduinoSensorsOnSmoje){
			if(sensor.getSensorType().equals(sensorType)){
				requestedSensor = sensor; 
			}
		}
		
		if(requestedSensor != null){
			return requestedSensor.getValue();
		}
		else{
			throw new SensorNotAvailableException(sensorType.name() + " is not available on this smoje");
		}
	}
	
	/**
	 * Gathers values of all sensors available on smoje
	 * @return {@link HashMap<@link SensorType, @link String} containing type of sensor and value
	 */
	public HashMap<SensorType, String> getAllSensorData(){
		HashMap<SensorType, String> sensorData = new HashMap<SensorType, String>();
		
		for(SmojeSensor sensor : arduinoSensorsOnSmoje){
			sensorData.put(sensor.getSensorType(), sensor.getValue());
		}
		return sensorData;
	}
	
	/**
	 * Establishes connection to Arduino
	 * @return {@link Boolean} whether successful or not
	 */
	private boolean connectToArduino(){
        boolean connected = arduinoLink.connect(CONNECTION_PORT, BAUD_RATE);
		return connected;
	}
	
	/**
	 * Getter for sensors connected to Arduino
	 * @return {@link ArrayList<>} with sensors
	 */
	public ArrayList<SmojeSensor> getArduinoSensors(){
		return arduinoSensorsOnSmoje;
	}
	
	/**
	 * Sends the message in parameter to Arduino over serial connection
	 * @param message {@link String} to send
	 */
	public void sendToArduino(String message){
		logger.info("Writing message to Arduino: " + message);
        arduinoLink.writeSerial(message);
	}
}
