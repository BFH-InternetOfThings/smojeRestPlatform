package ch.bfh.iot.smoje.raspi;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ch.bfh.iot.smoje.raspi.actors.IActor;
import ch.bfh.iot.smoje.raspi.sensors.ISensor;
import ch.bfh.iot.smoje.raspi.sensors.MockCamera;
import ch.bfh.iot.smoje.raspi.sensors.RaspiCamera;
import ch.bfh.iot.smoje.raspi.sensors.Temperatur;
import ch.bfh.iot.smoje.raspi.sensors.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/smoje")
public class SmojeService {
    ObjectMapper mapper = new ObjectMapper();

    static Map<String, ISensor> sensors = new HashMap<String, ISensor>();
    static Map<String, IActor> actors = new HashMap<String, IActor>();

    static {
        RaspiCamera cam = new RaspiCamera();
        MockCamera cam2 = new MockCamera();
        Temperatur temp = new Temperatur();
        Test test = new Test();

        sensors.put(cam.getId(), cam);
        sensors.put(cam2.getId(), cam2);
        sensors.put(temp.getId(), temp);
        sensors.put(test.getId(), test);
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
    public IActor getActorValueById(@PathParam("id") String id) throws JsonProcessingException {

        IActor actor = actors.get(id);
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
    public ISensor getSensorValueById(@PathParam("id") String id) throws JsonProcessingException {

        ISensor sensor = sensors.get(id);
        return sensor;
    }
}