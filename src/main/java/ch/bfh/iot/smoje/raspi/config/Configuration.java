package ch.bfh.iot.smoje.raspi.config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;

import ch.bfh.iot.smoje.raspi.Main;

/**
 * Configuration of Smoje sensor system
 * @author Matteo Morandi
 */
public class Configuration {
	
	private 		Logger 		logger 				= Main.logger;
	private final 	Properties 	properties 			= new Properties();
	private final 	String		propertiesFileName	= "smoje.sensors.properties";
	
	/*
	 * Constructor
	 */
	public Configuration(){
    	InputStream propertiesInput = null;
    	propertiesInput = Configuration.class.getClassLoader().getResourceAsStream(propertiesFileName);
    	try{
    		properties.load(propertiesInput);
    	}
    	catch(Exception e){
    		logger.error("could not load properties file");
    	}
	}
	
	/**
	 * Gets the unit for sensor specified in parameter
	 * @param sensorKey {@link String} with key in properties file
	 * @return {@link String} with unit
	 */
	public String getSensorUnit(String sensorKey){
		StringBuffer stringBuffer = new StringBuffer(sensorKey);
		stringBuffer.append(".unit");
		return (String) properties.get(stringBuffer.toString());
	}
	
	/**
	 * Gets the id for sensor specified in parameter
	 * @param sensorKey {@link String} with key in properties file
	 * @return {@link String} with id
	 */
	public String getSensorId(String sensorKey){
		StringBuffer stringBuffer = new StringBuffer(sensorKey);
		stringBuffer.append(".id");
		return (String) properties.get(stringBuffer.toString());
	}
	
	/**
	 * Gets all the available sensors that are enables in properties file
	 * @return {@link ArrayList} with {@link String} of sensor keys
	 */
    public ArrayList<String> getAvailableSensorList(){
    	ArrayList<String> sensors = new ArrayList<>();
    	
        for(Entry<Object, Object> e : properties.entrySet()) {
        	String key = (String) e.getKey();
        	
        	if(StringUtils.startsWith(key, "sensor")){
        		if(properties.getProperty(key).equals("yes")){
        			sensors.add(StringUtils.replace(key, "sensor.", ""));
        		}
        	}
        }
    	return sensors;
    }

}
