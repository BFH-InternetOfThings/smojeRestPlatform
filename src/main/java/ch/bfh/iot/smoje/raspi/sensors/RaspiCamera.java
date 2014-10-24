package ch.bfh.iot.smoje.raspi.sensors;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import ch.bfh.iot.smoje.raspi.actors.IrLed;
import ch.bfh.iot.smoje.raspi.common.SensorState;

public class RaspiCamera implements ISensor {

	private SensorState state = SensorState.OK;
	private final String destDir = "/home/pi/smoje/cam/";
	private final String imgName = "temp.jpg";
	
	private final String imgCaptureInstr = "/usr/bin/raspistill -o " + destDir+ imgName + " -t 1";
	
    @Override
    public String getId() {
        return "camera";
    }

    @Override
    public String getValue() {
    	captureImage();
    	
        String imageString = null;
        try {
            BufferedImage image = ImageIO.read(new File(destDir+imgName));

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            ImageIO.write(image, "jpg", bos);
            byte[] imageBytes = bos.toByteArray();

            imageString = Base64.encodeBase64String(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageString;
    }
    
    private void captureImage(){
    	IrLed ir = new IrLed();
    	ir.start();
    	executeCommand(this.imgCaptureInstr);
    	
    	// TODO lants1 evil thing, improve it later 
    	// maybe raspistill or another solution give a feedback about 
    	// the state of the image capture process
    	// do it synchron or check the file state with > ~
    	try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	ir.stop();
    }
 
	private void executeCommand(String cmd) {
		Runtime r = Runtime.getRuntime();
		try {
			r.exec(cmd);
		} catch (IOException e) {
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
