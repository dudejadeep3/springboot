package org.learning.ws.web.service;

import java.util.concurrent.Future;

import org.learning.model.Greeting;

public interface EmailService {

	Boolean send(Greeting greeting);

	void sendAsync(Greeting greeting);

	Future<Boolean> sendAsyncWithResult(Greeting greeting);
}
