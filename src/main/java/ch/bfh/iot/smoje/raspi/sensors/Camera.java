package ch.bfh.iot.smoje.raspi.sensors;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

import ch.bfh.iot.smoje.raspi.actors.IrLed;
import ch.bfh.iot.smoje.raspi.common.SensorState;

public class Camera implements ISensor {

	private SensorState state = SensorState.OK;
	private final String destDir = "/home/smoje/cam/";
	private final String imgName = "temp.jpg";
	
	private final String imgCaptureInstr = "/usr/bin/raspistill -o" + destDir+ imgName;
	
    @Override
    public String getId() {
        return "camera";
    }

    @Override
    public String getValue() {
    	captureImage();
    	
        String imageString = null;
        try {
            URL resource = getClass().getResource("destDir+imgName");
            BufferedImage image = ImageIO.read(new File(resource.getFile()));

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
