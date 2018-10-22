package org.learning.ws.service;

import java.util.Collection;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.learning.model.Greeting;
import org.learning.ws.AbstractTest;
import org.learning.ws.web.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class GreetingServiceTest extends AbstractTest {

	@Autowired
	private GreetingService greetingService;
	
	@Before
	public void setup() {
		greetingService.evictCache();
	}
	
	@After
	public void tearDown() {
		//clean up after each test
	}
	
	@Test
	public void testFindAll() {
		Collection<Greeting> list = greetingService.findAll();
		Assert.assertNotNull("failure-expected not null", list);
		Assert.assertNotNull("failure-expected size", list.size());
	}
	
	@Test
	public void testFindOne() {
		Long id = new Long(1);
		Greeting entity = greetingService.findOne(id);
		Assert.assertNotNull("failure-expected not null",entity);
		Assert.assertEquals("failure-expected id attribute",id,entity.getId());
	}
	
	@Test
	public void testFindOneNotFound() {
		Long id = Long.MAX_VALUE;
		Greeting entity = greetingService.findOne(id);
		Assert.assertNull(entity);
	}
	
	@Test
	public void testCreate() {
		Greeting greeting = new Greeting();
		greeting.setText("test");
		Greeting createdEntity =greetingService.create(greeting);
		
		Assert.assertNotNull("failure-expected not null",createdEntity);
		Assert.assertNotNull("failure-expected id attribute not null",createdEntity.getId());
		Assert.assertEquals("failure-expected text attribute match", "test",createdEntity.getText());
		
		Collection<Greeting> list = greetingService.findAll();
		Assert.assertEquals("failure-expected size", 3, list.size());
	}
	
	@Test
	public void testCreateWithId() {
		Exception e = null;
		
		Greeting entity = new Greeting();
		entity.setId(Long.MAX_VALUE);
		entity.setText("test");
		
		try {
			greetingService.create(entity);
		}catch(EntityExistsException eee) {
			e=eee;
		}
		
		Assert.assertNotNull("failure-expected exception",e);
		Assert.assertTrue("failure-expected EntityExistException", e instanceof EntityExistsException);
	}
	
	
	@Test
	public void testUpdate() {
		Long id = new Long(1);
		
		Greeting entity = greetingService.findOne(id);
		
		Assert.assertNotNull("failure-expected not null",entity);
		String updatedText =entity.getText()+"-test"; 
		entity.setText(updatedText);
		Greeting updatedEntity = greetingService.update(entity);
		
		Assert.assertNotNull("failure - expected updated entity not null",updatedEntity);
		Assert.assertEquals("failure-expected updated entity id attribute unchanged", id,updatedEntity.getId());
		Assert.assertEquals("failure-expected udpated entity text attribute match",updatedText,updatedEntity.getText());
	}
	
	@Test
	public void testUpdateNotFound() {
		Exception e = null;
		
		Greeting entity  = new Greeting();
		entity.setId(Long.MAX_VALUE);
		entity.setText("test");
		
		try {
			greetingService.update(entity);
		}catch(NoResultException nre) {
			e = nre;
		}
		Assert.assertNotNull("failure-expected exception",e);
		Assert.assertTrue("failure-expected NoResultException",e instanceof NoResultException);
	}
	
	@Test
	public void testDelete() {
		Long id = new Long(1);
		
		Greeting entity = greetingService.findOne(id);
		
		Assert.assertNotNull("failure-expected not null",entity);
		greetingService.delete(id);
		
		Collection<Greeting> list = greetingService.findAll();
		Assert.assertEquals("failure-expected size",1,list.size());
		
		Greeting deletedEntity = greetingService.findOne(id);
		Assert.assertNull("failure-expected entity to be deleted",deletedEntity);
	}
}
