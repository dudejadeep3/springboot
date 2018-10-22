package org.learning.ws.actuators.health;

import java.util.Collection;

import org.learning.model.Greeting;
import org.learning.ws.web.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class GreetingHealthIndicator implements HealthIndicator{

	@Autowired
	private GreetingService greetingService;
	
	@Override
	public Health health() {
		Collection<Greeting> greetings = greetingService.findAll();
		if(greetings == null || greetings.isEmpty()) {
			return Health.down().withDetail("count", 0).build();
		}
		return Health.up().withDetail("count", greetings.size()).build();
	}

}
