package org.learning.ws.web.api;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.learning.model.Greeting;
import org.learning.ws.AbstractControllerTest;
import org.learning.ws.web.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class GreetingControllerTest extends AbstractControllerTest {

	@Autowired
	private GreetingService greetingService;

	@Before
	public void setup() {
		super.setup();
		greetingService.evictCache();
	}

	@Test
	public void testGetGreetings() throws Exception {
		String uri = "/api/greetings";
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();

		Assert.assertEquals("failure-expected HTTP status 200", 200, status);
		Assert.assertTrue("failure-expected HTTP response body to have a value", content.trim().length() > 0);
	}

	@Test
	public void testGetGreeting() throws Exception {
		String uri = "/api/greetings/{id}";
		Long id = new Long(1);

		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();

		Assert.assertEquals("failure-expected http status 200", 200, status);
		Assert.assertTrue("failure-expected Http response body to have a value", content.trim().length() > 0);
	}

	@Test
	public void testGetGreetingNotFound() throws Exception {
		String uri = "/api/greetings/{id}";
		Long id = Long.MAX_VALUE;

		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id)).andReturn();
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();

		Assert.assertEquals("failure-expected http status as 404", 404, status);
		Assert.assertTrue("failure-expected http response to be empty", content.trim().isEmpty());
	}

	@Test
	public void testCreateGreeting() throws Exception {
		String uri = "/api/greetings";
		Greeting greeting = new Greeting();
		greeting.setText("test");

		String inputJSON = super.mapToJSON(greeting);

		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(uri).accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJSON)).andReturn();

		Greeting createdGreeting = super.mapFromJSON(result.getResponse().getContentAsString(), Greeting.class);
		Assert.assertNotNull("failure-expected greeting not null", createdGreeting);
		Assert.assertNotNull("failure-expected greetting id to be not null", createdGreeting.getId());
		Assert.assertEquals("failure-expected greeting.text match", "test", createdGreeting.getText());
	}

	@Test
	public void testUpdateGreeting() throws Exception {
		String uri = "/api/greetings";
		Long id = new Long(1);
		Greeting greeting = greetingService.findOne(id);
		String updatedText = greeting.getText() + " test";
		greeting.setText(updatedText);
		String inputJson = super.mapToJSON(greeting);

		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri, id).contentType(MediaType.APPLICATION_JSON_VALUE)
				.accept(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		Assert.assertEquals("failure-expected HTTP status code to be 200",200,status);
		Assert.assertTrue("failure-expected HTTP response body to have a value",content.trim().length()>0);
		
		Greeting updatedGreeting = super.mapFromJSON(content, Greeting.class);
		
		Assert.assertNotNull("failure-expected greeting not null",updatedGreeting);
		Assert.assertEquals("failure-expected id to be same", greeting.getId(),updatedGreeting.getId());
		Assert.assertEquals("failure-expected greeting text to be updated", updatedText,updatedGreeting.getText());
	}
	
	@Test
	public void testDeleteGreeting() throws Exception {
		String uri = "/api/greetings/{id}";
		Long id = new Long(1);
		Greeting greeting = new Greeting();
		greeting.setId(id);
		String jsonString = super.mapToJSON(greeting);
		MvcResult result = mvc.perform(MockMvcRequestBuilders.delete(uri,id).contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonString)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		Assert.assertEquals("failure-expected status code to be 204", 204,status);
		Assert.assertTrue("failure-expected content string to be empty",content.trim().length()==0);
		
		Greeting deletedGreeting = greetingService.findOne(id);
		Assert.assertNull("failure-expected greeting object to be empty",deletedGreeting);
	}

}
