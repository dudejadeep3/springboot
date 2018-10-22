package org.learning.ws.web.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.learning.model.Greeting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceBean implements EmailService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public Boolean send(Greeting greeting) {
		logger.info("<send");
		
		Boolean success = Boolean.FALSE;
		
		long pause = 5000;
		try {
			Thread.sleep(pause);
		}catch(InterruptedException e) {
			//do nothing
		}
		success = Boolean.TRUE;
		logger.info("Processing time was {} seconds",pause/1000);
		logger.info("< send");
		return success;
	}

	@Async
	@Override
	public void sendAsync(Greeting greeting) {
		logger.info("<sendAsync");
		try {
			send(greeting);
		}catch(Exception e) {
			logger.error("Exception caught sending asynchronous mail",e);
		}
		
		logger.info("<sendAsync");
	}

	@Async
	@Override
	public Future<Boolean> sendAsyncWithResult(Greeting greeting) {
		logger.info(">sendAsyncWithResult");
		
		CompletableFuture<Boolean> response = new CompletableFuture<>();
		
		try {
			Boolean success = send(greeting);
			response.complete(success);
		}catch(Exception e) {
			logger.warn("Exception caught sending asynchronous mail",e);
			response.isCompletedExceptionally();
		}
		logger.info("<sendAsyncWithResult");
		return response;
	}

}
