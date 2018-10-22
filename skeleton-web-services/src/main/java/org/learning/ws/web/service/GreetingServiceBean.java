package org.learning.ws.web.service;

import java.util.Collection;

import javax.persistence.EntityExistsException;
import javax.persistence.NoResultException;

import org.learning.model.Greeting;
import org.learning.web.repository.GreetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation=Propagation.SUPPORTS , readOnly=true)//supports tells it will support the curret transaction but will not create anothere
public class GreetingServiceBean implements GreetingService {
	@Autowired
	private GreetingRepository greetingRepository;
	
	@Autowired
	private CounterService counterService;
	
	@Override
	public Collection<Greeting> findAll() {
		counterService.increment("method.invoked.greetingServiceBean.findAll");
		Collection<Greeting> greeting = greetingRepository.findAll();
		return greeting;
	}

	@Override
	@Cacheable(value="greetings",key="#id")//value tells the cache in which we have store the result
	public Greeting findOne(Long id) {     // and key tells the index at which it is stored
		counterService.increment("method.invoked.greetingServiceBean.findOne");
		Greeting greeting = greetingRepository.findOne(id);
		return greeting;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)//overrides the class level annotation. required means it will create if transaction is not there
	@CachePut(value="greetings",key="#result.id")//result represent returned value
	public Greeting create(Greeting greeting) {
		counterService.increment("method.invoked.greetingServiceBean.create");
		if(greeting.getId()!=null) {
			//return null;//cannot create greeting with specified id
			throw new EntityExistsException();
		}
		Greeting savedGreeting = greetingRepository.save(greeting);
		/*if(savedGreeting.getId()==4L) {
			throw new RuntimeException("Roll me back");
		}*/
		return savedGreeting;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
	@CachePut(value="greetings", key="#greeting.id")
	public Greeting update(Greeting greeting) {
		counterService.increment("method.invoked.greetingServiceBean.update");
		Greeting greetingPersisted = greetingRepository.findOne(greeting.getId());
		if(greetingPersisted==null) {
			//return null;//cannot update greeting that hasn't been persisted
			throw new NoResultException();
		}
		Greeting updatedGreeting  = greetingRepository.save(greeting);
		return updatedGreeting;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,readOnly=false)
	@CacheEvict(value="greetings", key="#id")
	public void delete(Long id) {
		counterService.increment("method.invoked.greetingServiceBean.delete ");
		greetingRepository.delete(id);
	}
	
	@Override
	@CacheEvict(value="greetings",allEntries=true)
	public void evictCache() {
		
	}
}
