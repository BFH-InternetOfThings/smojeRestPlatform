package ch.bfh.iot.smoje.raspi.sensors;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;

import org.apache.commons.codec.binary.Base64;

import ch.bfh.iot.smoje.raspi.common.SensorState;

public class MockCamera implements SmojeSensor {

    @Override
    public String getId() {
        return "mockcamera";
    }

    @Override
    public String getValue() {

        String imageString = null;
        URL resource = getClass().getResource("/camera/bielersee.jpg");

        File file;
        byte[] imageBytes = null;

        try {
            file = new File(resource.toURI());
            imageBytes = Files.readAllBytes(file.toPath());

        } catch (URISyntaxException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Byte length: " + imageBytes.length);

        imageString = Base64.encodeBase64String(imageBytes);

        return imageString;
    }

    @Override
    public SensorState getStatus() {
        return SensorState.OK;
    }

    @Override
    public String getUnit() {
        return "Base64 encoded Image";
    }

	@Override
	public SensorType getSensorType() {
		return SensorType.CAMERA;
	}
}
