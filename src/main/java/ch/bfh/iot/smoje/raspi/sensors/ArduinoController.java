package ch.bfh.iot.smoje.raspi.sensors;

import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.Logger;

import ch.bfh.iot.smoje.raspi.Main;
import ch.bfh.iot.smoje.raspi.exceptions.ArduinoBusyException;

/**
 * Class handles reading of sensor data, creates connection to Arduino board and gets values for sensors
 * TODO check if serial connection could successfully be established, else throw exception or notify somehow
 * @author Matteo Morandi, Joel Holzer
 */
public class ArduinoController implements SerialPortEventListener {

	private static final 	int 			PORT_TIME_OUT 			= 2000; // Milliseconds to block while waiting for port open
	private static final 	int 			DATA_RATE 				= 9600; // Default bits per second for COM port
	private final 			int				ARDUINO_TIME_OUT 		= 10000;
	private 				Logger 			logger 					= Main.logger;
	private 				SerialPort 		serialPort;
	private 				BufferedReader 	input;	//Will be fed by an InputStreamReader, converting the bytes into characters making the displayed results codepage independent
	private static 			OutputStream 	output; //Output stream to the port
	private					boolean 		arduinoDataReceived 	= false;
	private 				SmojeSensor		activeSensor;
	private 				Thread 			sleepThread;

	private static final String PORT_NAMES[] = { 
		"/dev/ttyACM0", // Raspberry Pi
	};

	/*
	 * Constructor
	 */
	public ArduinoController(){
		initialize();
	}

	/**
	 * Tries to open serial connection and initializes for data transmission
	 * Registers SerialPortEventListener
	 */
	public void initialize() {
		logger.info("initializing ArduinoSensorConctroller");
		// the next line is for Raspberry Pi and 
		// gets us into the while loop and was suggested here was suggested http://www.raspberrypi.org/phpBB3/viewtopic.php?f=81&t=32186
		System.setProperty("gnu.io.rxtx.SerialPorts", "/dev/ttyACM0");

		CommPortIdentifier portId = null;
		Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();

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
			logger.fatal("Could not find COM port.");
			return;
		}

		try {
			// open serial port, and use class name for the appName.
			serialPort = (SerialPort) portId.open(this.getClass().getName(), PORT_TIME_OUT);

			// set port parameters
			serialPort.setSerialPortParams(
					DATA_RATE,
					SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE
					);

			// open the streams
			input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
			output = serialPort.getOutputStream();
			serialPort.addEventListener(this);
			serialPort.notifyOnDataAvailable(true);

		} catch (Exception e) {
			logger.fatal("could not open serial port!", e);
		}
	}

	/**
	 * This should be called when you stop using the port.
	 * This will prevent port locking on platforms like Linux.
	 */
	public synchronized void close() {
		logger.info("SERIAL: closing port");
		if (serialPort != null) {
			serialPort.removeEventListener();
			serialPort.close();
		}
	}

	/**
	 * Method tries to get value from Arduino and sets it on sensor given in parameter
	 * @param requestedSensor
	 * @return {@link Boolean} if successful or not
	 * @throws ArduinoBusyException
	 */
	public synchronized boolean getValueOverSerialConnection(SmojeSensor requestedSensor) throws ArduinoBusyException {
		if(activeSensor != null){
			throw new ArduinoBusyException("another sensor is already pending");
		}
		else{
			activeSensor = requestedSensor;
			logger.info("Sensor " + activeSensor.getSensorType().name() + " is now active. ID: " + activeSensor.getId());
			arduinoDataReceived = false;
		}

		try {
			logger.info("RASPBERRY-->ARDUINO: " + activeSensor.getId());
			output.write(activeSensor.getId().getBytes());
			output.flush();
		} catch (Exception e) {
			logger.error("ERROR: RASPBERRY-->ARDUINO: Could not write to serial port", e);
			return false;
		}

		sleepThread = Thread.currentThread();
		try {
			Thread.sleep(ARDUINO_TIME_OUT);
		} catch (InterruptedException e) {
			logger.info("thread interrupted");
		}

		activeSensor = null;
		return arduinoDataReceived;
	}

	/**
	 * Handles serial port events, sets booleans and result
	 */
	@Override
	public void serialEvent(SerialPortEvent serialEvent) {
		logger.info("Serial event! " + serialEvent.getEventType());

		switch (serialEvent.getEventType()) {
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.RI:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;
		case SerialPortEvent.DATA_AVAILABLE:

			if(activeSensor != null){

				try { //get input string and parse to ensure correctness
					String serialData = input.readLine();
					logger.info("SERIAL RECEIVED: " + serialData);

					Pattern inputPattern = Pattern.compile("<ADUE: " + activeSensor.getId() + ": \\d+\\.*\\d*>");
					Matcher inputMatcher = inputPattern.matcher(serialData);

					if(inputMatcher.matches()){ //check for full valid input

						logger.info("received data matches complete sensor value");
						Pattern valuePattern = Pattern.compile("\\d+\\.*\\d*");
						Matcher valueMatcher = valuePattern.matcher(serialData);

						if (valueMatcher.find()){ //check for sensor value and extract it

							logger.info("serial input is valid sensor data");
							Double serialValue = Double.parseDouble(valueMatcher.group());

							Pattern sensorPattern = Pattern.compile(": (.*?):");
							Matcher sensorMatcher = sensorPattern.matcher(serialData);

							if (sensorMatcher.find()){ //check for sensor ID and set value if correct

								String receivedSensorId = sensorMatcher.group(1);
								if(activeSensor.getId().equals(receivedSensorId)){

									activeSensor.setSensorValue(serialValue);
									arduinoDataReceived = true;
								}else{
									logger.error("received sensor is not requested");
									arduinoDataReceived = false;
								}
							}
						}else{
							logger.error("input did not contain valid number");
							arduinoDataReceived = false;
						}
					}else{
						logger.error("received data was not sensor value or incomplete");
						arduinoDataReceived = false;
					}
					sleepThread.interrupt();
				}
				catch (IOException e) {
					logger.error("Could not get Serial input", e);
				}
			}else{
				logger.info("active sensor is null");
				arduinoDataReceived = false;
			}
			break;
		}
	}
}