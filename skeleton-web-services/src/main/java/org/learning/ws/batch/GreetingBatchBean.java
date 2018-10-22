package org.learning.ws.batch;

import java.util.Collection;

import org.learning.model.Greeting;
import org.learning.ws.web.service.GreetingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Profile("batch")
public class GreetingBatchBean {

	private Logger logger = LoggerFactory.getLogger(GreetingBatchBean.class);
	
	@Autowired
	private GreetingService greetingService;
	
	@Scheduled(cron="${batch.greeting.cron}")
	public void cronJob() {
		logger.info(">cronJob");
		
		//All scheduling logic here
		Collection<Greeting> greetings = greetingService.findAll();
		logger.info("There are {} greetings in the data store",greetings.size());
		logger.info("<cronJob");
	}
	
	@Scheduled(initialDelayString="${batch.greeting.initialdelay}", fixedRateString="${batch.greeting.fixedrate}")
	public void fixedRateJobWithInitialDelay() {
		logger.info(">fixedRateJobWithInitialDelay");
		
		//All scheduling logic here
		long pause = 5000;
		long start = System.currentTimeMillis();
		
		do {
			if(start + pause < System.currentTimeMillis()) {
				break;
			}
		}while(true);
		logger.info("Processing time was {} seconds",pause/1000);
		logger.info("<fixedRateJobWithInitialDelay");
	}
	
	@Scheduled(initialDelayString="${batch.greeting.initialdelay}", fixedDelayString="${batch.greeting.fixeddelay}")
	public void fixedDelayJobWithInitialDelay() {
		logger.info(">fixedDelayJobWithInitialDelay");
		
		//All scheduling logic here
		long pause = 5000;
		long start = System.currentTimeMillis();
		
		do {
			if(start + pause < System.currentTimeMillis()) {
				break;
			}
		}while(true);
		logger.info("Processing time was {} seconds",pause/1000);
		logger.info("<fixedDelayJobWithInitialDelay");
	}
}
