package ch.bfh.iot.smoje.raspi;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ch.bfh.iot.smoje.raspi.actors.SmojeActor;
import ch.bfh.iot.smoje.raspi.sensors.ArduinoSensorController;
import ch.bfh.iot.smoje.raspi.sensors.SmojeSensor;
import ch.bfh.iot.smoje.raspi.sensors.MockCamera;
import ch.bfh.iot.smoje.raspi.sensors.RaspiCamera;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/smoje")
public class SmojeService {
    ObjectMapper mapper = new ObjectMapper();

    static Map<String, SmojeSensor> sensors = new HashMap<String, SmojeSensor>();
    static Map<String, SmojeActor> actors = new HashMap<String, SmojeActor>();

    static {
    	//Instantiate necessary sensors and controllers
    	
        RaspiCamera cam = new RaspiCamera();
        MockCamera cam2 = new MockCamera();
        
        //add sensors to list
        
    	for(SmojeSensor sensor : ArduinoSensorController.getInstance().getArduinoSensors()){
    		sensors.put(sensor.getId(), sensor);
    	}
        sensors.put(cam.getId(), cam);
        sensors.put(cam2.getId(), cam2);
    }

    static {
    }

    @GET
    @Path("/actors")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<String> getActors() throws JsonProcessingException {
        return actors.keySet();
    }

    @GET
    @Path("/actors/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SmojeActor getActorValueById(@PathParam("id") String id) throws JsonProcessingException {

        SmojeActor actor = actors.get(id);
        return actor;
    }

    @GET
    @Path("/sensors")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<String> getSensors() throws JsonProcessingException {

        return sensors.keySet();
    }

    @GET
    @Path("/sensors/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SmojeSensor getSensorValueById(@PathParam("id") String id) throws JsonProcessingException {

        SmojeSensor sensor = sensors.get(id);
        return sensor;
    }
}