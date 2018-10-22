package org.learning.ws.web.service;

import java.util.Collection;

import org.learning.model.Greeting;

public interface GreetingService {

	Collection<Greeting> findAll();
	
	Greeting findOne(Long id);
	
	Greeting create(Greeting greeting);
	
	Greeting update(Greeting greeting);
	
	void delete(Long id);
	
	void evictCache();
}
