package org.learning.ws.web.api;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.learning.model.Greeting;
import org.learning.ws.AbstractControllerTest;
import org.learning.ws.web.service.EmailService;
import org.learning.ws.web.service.GreetingService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class GreetingControllerMocksTest extends AbstractControllerTest {

	@Mock
	private EmailService emailService;

	@Mock
	private GreetingService greetingService;

	@InjectMocks
	private GreetingController greetingController;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		super.setup(greetingController);
	}

	private Collection<Greeting> getEntityListSetupData() {
		Collection<Greeting> list = new ArrayList<>();
		list.add(getEntityStubData());
		return list;
	}

	private Greeting getEntityStubData() {
		Greeting entity = new Greeting();
		entity.setId(1L);
		entity.setText("hello");
		return entity;
	}

	@Test
	public void testGetGreetings() throws Exception {
		// create some test data
		Collection<Greeting> list = getEntityListSetupData();

		Mockito.when(greetingService.findAll()).thenReturn(list);

		String uri = "/api/greetings";

		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		// verify the GreetingService.findAll method was invoked only once
		Mockito.verify(greetingService, Mockito.times(1)).findAll();

		Assert.assertEquals("failure-expected Http status 200", 200, status);
		Assert.assertTrue("failure-expected Http response body to have a value", content.trim().length() > 0);
	}

	@Test
	public void testGetGreeting() throws Exception {
		Long id = new Long(1);
		Greeting greeting = getEntityStubData();

		Mockito.when(greetingService.findOne(id)).thenReturn(greeting);

		String uri = "/api/greetings/{id}";

		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();

		Mockito.verify(greetingService, Mockito.times(1)).findOne(id);

		Assert.assertEquals("failure-expected Http Status 200", 200, status);
		Assert.assertTrue("failure-expected HTTP Response body to have a values", content.trim().length() > 0);
	}

	@Test
	public void testGetGreetingsNotFound() throws Exception {
		Long id = Long.MAX_VALUE;

		Mockito.when(greetingService.findOne(id)).thenReturn(null);

		String uri = "/api/greetings/{id}";

		MvcResult result = mvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.APPLICATION_JSON_VALUE))
				.andReturn();

		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();

		Mockito.verify(greetingService, Mockito.times(1)).findOne(id);

		Assert.assertEquals("failure-expected HTTP Status code 404", 404, status);
		Assert.assertTrue("failure-expected HTTP response body to be empty", content.trim().length() == 0);
	}

	@Test
	public void testCreateGreeting() throws Exception {
		Greeting entity = getEntityStubData();

		Mockito.when(greetingService.create(Mockito.any(Greeting.class))).thenReturn(entity);

		String uri = "/api/greetings";
		String jsonString = super.mapToJSON(entity);
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(uri).accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonString)).andReturn();
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();

		Mockito.verify(greetingService, Mockito.times(1)).create(Mockito.any(Greeting.class));

		Assert.assertTrue("failure-expected response body to have a value", content.trim().length() > 0);
		Assert.assertEquals("failure-expected Http status to 201", 201, status);

		Greeting createdEntity = super.mapFromJSON(content, Greeting.class);

		Assert.assertNotNull("failure-expected entity to be not null", createdEntity);
		Assert.assertNotNull("failure-expected entity id to be not null", createdEntity.getId());
		Assert.assertEquals("failure-expected text attribute match", entity.getText(), createdEntity.getText());
	}

	@Test
	public void testUpdateGreeting() throws Exception {
		Greeting entity = getEntityStubData();
		entity.setText(entity.getText() + " test");

		Mockito.when(greetingService.update(Mockito.any(Greeting.class))).thenReturn(entity);

		String uri = "/api/greetings";
		String inputJSON = super.mapToJSON(entity);

		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(uri).accept(MediaType.APPLICATION_JSON_VALUE)
				.content(inputJSON).contentType(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		Mockito.verify(greetingService,Mockito.times(1)).update(Mockito.any(Greeting.class));
		
		Assert.assertEquals("failure-expected Http Status 200", 200,status);
		Assert.assertNotNull("failure-expected response body to have value",content.trim().length()>0);
		
		Greeting updatedEntity = super.mapFromJSON(content, Greeting.class);
		
		Assert.assertNotNull("failure-expected entity not null",updatedEntity);
		Assert.assertNotNull("faiure-expected entity id not null",updatedEntity.getId());
		Assert.assertEquals("failure-expected entity text to be match", entity.getText(),updatedEntity.getText());
	}
	
	@Test
	public void testDeleteGreeting() throws Exception {
		Long id = new Long(1);
		Greeting entity = getEntityStubData();
		String jsonString = super.mapToJSON(entity);
		Mockito.doAnswer(new Answer<Void>() {

			@Override
			public Void answer(InvocationOnMock invocation) throws Throwable {
				return null;
			}
			
		}).when(greetingService).delete(id);
		
		String uri = "/api/greetings/{id}";
		MvcResult result = mvc.perform(MockMvcRequestBuilders.delete(uri,id).contentType(MediaType.APPLICATION_JSON_VALUE).content(jsonString)).andReturn();
			
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();
		
		Mockito.verify(greetingService,Mockito.times(1)).delete(id);
		
		Assert.assertEquals("failure-expected http status code 204", 204,status);
		Assert.assertTrue("failiure-expected Http response body to be empty",content.trim().length()==0);
	}
}
