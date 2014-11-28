package ch.bfh.iot.smoje.raspi.sensors;

import org.zu.ardulink.Link;

import ch.bfh.iot.smoje.raspi.common.SensorState;

public class TestArdulink implements ISensor {

    @Override
    public String getId() {
        return "ardulink";
    }

    @Override
    public String getValue() {
        
        Link link = Link.getDefaultInstance();
        return link.getName();
    }

    @Override
    public String getUnit() {
        return null;
    }

    @Override
    public SensorState getStatus() {
        return null;
    }

}