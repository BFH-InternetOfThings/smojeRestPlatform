package ch.bfh.iot.smoje.raspi.actors;

import java.util.List;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class IrLed implements IActor {
	
	List<GpioPinDigitalOutput> irLeds;

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
        final GpioController gpio = GpioFactory.getInstance();
    	final GpioPinDigitalOutput irLed1 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "irLed1", PinState.HIGH);
    	irLeds.add(irLed1);
    }
    
    public void stop(){
	    for(GpioPinDigitalOutput led : irLeds){
	    	led.isLow();
	    }
    }

}
