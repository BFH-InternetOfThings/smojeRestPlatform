package ch.bfh.iot.smoje.raspi;

import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ch.bfh.iot.smoje.raspi.actors.SmojeActor;
import ch.bfh.iot.smoje.raspi.sensors.SmojeSensor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/smoje")
public class SmojeService {
    ObjectMapper mapper = new ObjectMapper();

    static {
    }

    @GET
    @Path("/actors")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<String> getActors() throws JsonProcessingException {
        return Main.smoje.getActors().keySet();
    }

    @GET
    @Path("/actors/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SmojeActor getActorValueById(@PathParam("id") String id) throws JsonProcessingException {

        SmojeActor actor = Main.smoje.getActors().get(id);
        return actor;
    }

    @GET
    @Path("/sensors")
    @Produces(MediaType.APPLICATION_JSON)
    public Set<String> getSensors() throws JsonProcessingException {

        return Main.smoje.getSensors().keySet();
    }

    @GET
    @Path("/sensors/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public SmojeSensor getSensorValueById(@PathParam("id") String id) throws JsonProcessingException {

        SmojeSensor sensor = Main.smoje.getSensors().get(id);
        return sensor;
    }
}