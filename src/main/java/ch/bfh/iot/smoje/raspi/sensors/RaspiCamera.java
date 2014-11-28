package ch.bfh.iot.smoje.raspi.sensors;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.codec.binary.Base64;

import ch.bfh.iot.smoje.raspi.common.SensorState;

public class RaspiCamera implements ISensor {

	private SensorState state = SensorState.OK;
	private final String destDir = "/home/pi/smoje/cam/";
	private final String imgName = "temp.jpg";
	
	private final String imgCaptureInstr = "/usr/bin/raspistill -o " + destDir+ imgName + " -t 1 -q 75";
	
    @Override
    public String getId() {
        return "camera";
    }

    @Override
    public String getValue() {
    	captureImage();
    	
        String imageString = null;
        try {
            
            File file = new File(destDir+imgName);
            byte[] imageBytes = Files.readAllBytes(file.toPath());

            imageString = Base64.encodeBase64String(imageBytes);

        } catch (IOException e) {
        	state = SensorState.TEMPORARY_NOK;
        }

        return imageString;
    }
    
    private void captureImage(){
//    	IrLed ir = new IrLed();
//    	ir.start();
    	executeCommand(this.imgCaptureInstr);
//    	ir.stop();
    }
 
	private void executeCommand(String cmd) {
		Runtime r = Runtime.getRuntime();
		try {
			Process p = r.exec(cmd);
			p.waitFor();
		} catch (IOException | InterruptedException e) {
			state = SensorState.NOK;
		}
	}
    
    @Override
    public SensorState getStatus() {
        return state;
    }

    @Override
    public String getUnit() {
        return "Base64 encoded Image";
    }

}
