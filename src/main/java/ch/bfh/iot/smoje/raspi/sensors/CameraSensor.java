package ch.bfh.iot.smoje.raspi.sensors;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;

public class CameraSensor implements ISensor {

    @Override
    public String getId() {
        return "camera";
    }

    @Override
    public String getValue() {
        // TODO: LED Steuerung http://pi4j.com/usage.html

        String imageString = null;
        try {
            URL resource = getClass().getResource("/camera/bielersee.jpg");
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

    @Override
    public String getStatus() {
        return "OK";
    }

    @Override
    public String getUnit() {
        return "Base64 encoded Image";
    }

}
