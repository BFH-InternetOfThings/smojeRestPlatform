package ch.bfh.iot.smoje.raspi.actors;

import java.util.ArrayList;
import java.util.List;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class IrLed implements IActor {
	
	List<GpioPinDigitalOutput> irLeds = new ArrayList<GpioPinDigitalOutput>();
	final GpioController gpio = GpioFactory.getInstance();
	
    @Override
    public String getId() {
        return "irLedGroup";
    }

    @Override
    public String getStatus() {
    	StringBuffer strBuffer = new StringBuffer();
    	  for(GpioPinDigitalOutput led : irLeds){
  	    	strBuffer.append(led.getPin().getName());
  	    	strBuffer.append(":");
  	    	strBuffer.append(led.getState().getName());
  	    	strBuffer.append("|");
  	    }
    	  return strBuffer.toString();
    }
    
    public void start(){
    	reset();
    	final GpioPinDigitalOutput irLed1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "irLed1", PinState.HIGH);
    	irLeds.add(irLed1);
    }
     
    public void reset(){
    		// TODO lants1 reset only the ir led pins and not all pins bohnp would be happy about it
    		for(GpioPin pin : gpio.getProvisionedPins()){
    			gpio.unprovisionPin(pin);	
    		}
    }
    
    public void stop(){
	    for(GpioPinDigitalOutput led : irLeds){
	    	led.isLow();
		    gpio.unprovisionPin(led);
	    }
    }

}
