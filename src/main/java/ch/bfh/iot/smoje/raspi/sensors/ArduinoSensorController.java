package ch.bfh.iot.smoje.raspi.sensors;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.TooManyListenersException;

import org.apache.logging.log4j.Logger;
import org.zu.ardulink.Link;

import ch.bfh.iot.smoje.raspi.SmojeServer;
import ch.bfh.iot.smoje.raspi.exceptions.SensorNotAvailableException;

/**
 * Class handles reading of sensor data, creates connection to Arduino board and gets values for sensors
 * @author Matteo Morandi
 */
public class ArduinoSensorController {
	
    public 	static 			Link 					arduinoLink 			= Link.getDefaultInstance();
    private static 				Logger 					logger 					= SmojeServer.logger;
    private 				boolean					isConnected				= false;
    private 				ArrayList<SmojeSensor> 	arduinoSensorsOnSmoje 	= new ArrayList<>();

    
	private static ArduinoSensorController instance;
	private SerialPort serialPort;
	private String temporaryResult;
	
	private static final String PORT_NAMES[] = { 
		"/dev/ttyACM0", // Raspberry Pi
	};
	/**
	* A BufferedReader which will be fed by a InputStreamReader 
	* converting the bytes into characters 
	* making the displayed results codepage independent
	*/
	private BufferedReader input;
	private static OutputStream output; /// The output stream to the port */
	private static final int TIME_OUT = 2000; // Milliseconds to block while waiting for port open */
	private static final int DATA_RATE = 9600; // Default bits per second for COM port
	
    /*
     * Constructor
     */
	private ArduinoSensorController(){
		createSensors();
		initialize();
	}
	
	/**
	 * Creates all instances of available sensors and adds them to {@link ArrayList}
	 */
	private void createSensors(){
		//goes trough all sensor keys that are enables in config file
		for(String key : SmojeServer.config.getAvailableSensorList()){
			System.out.println("key is: " + key);
			//gather information about sensor
			SensorType sensorType = SensorType.get(key);
			
			if (sensorType == null){
				System.out.println("sensor type is null");
			}
			String unit = SmojeServer.config.getSensorUnit(key);
			String id = SmojeServer.config.getSensorId(key);
		
			//instantiate new sensor with information
			arduinoSensorsOnSmoje.add(new Sensor(sensorType, unit, id));
		}
	}
	
	public void initialize() {
		logger.info("initialize-method ArduinoSensorConctroller");
		// the next line is for Raspberry Pi and 
        // gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
        System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");

		CommPortIdentifier portId = null;
		Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();

		//First, Find an instance of serial port as set in PORT_NAMES.
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					portId = currPortId;
					break;
				}
			}
		}
		if (portId == null) {
			System.out.println("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(),
					TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();

			serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
	}
	
	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}
	
	/**
	 * Getter for sensors connected to Arduino
	 * @return {@link ArrayList<>} with sensors
	 */
	public ArrayList<SmojeSensor> getArduinoSensors(){
		return arduinoSensorsOnSmoje;
	}
	
	/**
	 * Sends the sensorId in parameter to arduino over serial connection
	 * gets the result that comes back over Serial 
	 * @param sensorId
	 * @return
	 */
	public String getValueOverSerialConnection(String sensorId) {
		
		try {
			serialPort.addEventListener(new SerialPortEventListener() {
				
				@Override
				public void serialEvent(SerialPortEvent serialEvent) {
					if (serialEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
						try {
							temporaryResult = input.readLine();
							logger.info("got result from arduino");
							logger.info("temporary result: " + temporaryResult);
						} catch (Exception e) {
							temporaryResult = null;
							logger.info("exception: " + e.getMessage());
							//TODO log error
						}
					}
				}
			});
		} catch (TooManyListenersException e1) {
			temporaryResult = null;
			return temporaryResult;
			//TODO log
		}
		
		 System.out.println("Sent: " + sensorId);
		 try {
			 output.write(sensorId.getBytes());
			 output.flush();
		 } catch (Exception e) {
			 System.out.println("could not write to port");
			 //TODO log
		 }
		 
		 logger.info("return temporary result: " + temporaryResult);
		 return temporaryResult;
	}
	
	public static ArduinoSensorController getInstance(){
		if (instance == null) {
			logger.info("create new Arduino instance");
			instance = new ArduinoSensorController();
		}
		logger.info("return Arduino instance");
		return instance;
	}
	
}
