package org.learning.ws;

import java.io.IOException;

import org.learning.ws.web.api.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebAppConfiguration
public abstract class AbstractControllerTest extends AbstractTest{

	protected MockMvc mvc;
	
	@Autowired
	protected WebApplicationContext webApplicationContext;
	
	protected void setup() {
		this.mvc=MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
	}
	
	protected String mapToJSON(Object obj) throws JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(obj);
	}
	
	protected void setup(BaseController controller) {
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
	}
	
	protected <T> T mapFromJSON(String json, Class<T> clazz) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json,clazz);
	}
	
	
}
