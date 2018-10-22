package org.learning.ws.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AsyncResponse<V> implements Future<V>{

	private V value;
	private Exception executionException;
	private boolean isCompletedExceptionally = false;
	private boolean isCancelled = false;
	private boolean isDone = false;
	private long checkCompleteInterval = 100;
	
	public AsyncResponse() {
		super();
	}
	
	public AsyncResponse(Throwable ex) {
		this.executionException = new ExecutionException(ex);
		this.isCompletedExceptionally = true;
		this.isDone=true;
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		this.isCancelled=true;
		this.isDone = true;
		return false;
	}

	@Override
	public boolean isCancelled() {
		return this.isCancelled;
	}
	
	public boolean isCompletedExceptionally() {
		return this.isCompletedExceptionally;
	}

	@Override
	public boolean isDone() {
		return this.isDone;
	}

	@Override
	public V get() throws InterruptedException, ExecutionException {
		return null;
	}

	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		// TODO Auto-generated method stub
		return null;
	}

}
