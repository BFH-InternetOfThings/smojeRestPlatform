package ch.bfh.smoje;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ch.bfh.smoje.entity.ISensor;
import ch.bfh.smoje.entity.CameraSensor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Path("/smoje")
public class Demo {
	ObjectMapper mapper = new ObjectMapper();
	
	@GET
	@Path("/sensors")
	@Produces(MediaType.APPLICATION_JSON)
	public String getSensors() throws JsonProcessingException {
		List<CameraSensor> sensors = new ArrayList<CameraSensor>();
		
		CameraSensor s1 = new CameraSensor();
		
		sensors.add(s1);
		
		return mapper.writeValueAsString(sensors);
	}

	@GET
	@Path("/sensors/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public String getSensorById(@PathParam("id")String id) throws JsonProcessingException {
		
		ISensor s1 = new CameraSensor();
		
		
		return mapper.writeValueAsString(s1);

	}
}