package ch.bfh.iot.smoje.raspi;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ch.bfh.iot.smoje.raspi.sensors.CameraSensor;
import ch.bfh.iot.smoje.raspi.sensors.ISensor;
import ch.bfh.iot.smoje.raspi.sensors.TemperaturSensor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/smoje")
public class Service {
	ObjectMapper mapper = new ObjectMapper();
	
	static Map<String, ISensor> sensors = new HashMap<String, ISensor>();
	
	static {
		CameraSensor cam = new CameraSensor();
		TemperaturSensor temp = new TemperaturSensor();
		sensors.put(cam.getId(), cam);
		sensors.put(temp.getId(), temp);
	}
	
	@GET
	@Path("/sensors")
	@Produces(MediaType.APPLICATION_JSON)
	public Set<String> getSensors() throws JsonProcessingException {
		
		
		return sensors.keySet();
//		return mapper.writeValueAsString(sensors);
	}

	@GET
	@Path("/sensors/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public ISensor getSensorValueById(@PathParam("id")String id) throws JsonProcessingException {
		
		ISensor sensor = sensors.get(id);
		return sensor;
//		return mapper.writeValueAsString(s1);

	}
}