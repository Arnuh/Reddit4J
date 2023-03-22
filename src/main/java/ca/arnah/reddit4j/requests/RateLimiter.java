/*
 * Copyright (c) 2023, Arnah <github@arnah.ca>
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package ca.arnah.reddit4j.requests;

import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class RateLimiter{
	
	private final LinkedBlockingQueue<RedditRequest<?>> queue;
	private final ScheduledExecutorService executor;
	private long remaining = 600, resetDelay = 0, used = 0;
	private boolean running = true;
	
	public RateLimiter(){
		this.queue = new LinkedBlockingQueue<>();
		this.executor = Executors.newSingleThreadScheduledExecutor();
		executor.execute(()->{
			// Need to do better ratelimit handling
			while(running){
				try{
					RedditRequest<?> currentRequest = queue.poll(10, TimeUnit.SECONDS);
					if(currentRequest == null) continue;
					HttpResponse<String> result = null;
					try{
						if(remaining <= 0){
							try{
								log.debug("Delaying requests for {} seconds due to ratelimit", resetDelay + 1);
								Thread.sleep(TimeUnit.SECONDS.toMillis(resetDelay + 1));
							}catch(InterruptedException e){
								log.warn("Got interrupted while waiting for a rate limit", e);
							}
						}
						result = currentRequest.executeRequest();
					}catch(Exception ex){
						currentRequest.getResult().completeExceptionally(ex);
					}finally{
						try{
							if(result != null){
								handleResponse(currentRequest, result);
							}
						}catch(Throwable ex){
							log.catching(ex);
						}
					}
					// Check if the request is done, if not, readd it to the queue
					if(currentRequest.getResult().isDone()){
						continue;
					}
					log.trace("Readding request to queue after failed attempt");
					queue.add(currentRequest);
				}catch(Throwable ex){
					log.catching(ex);
				}
			}
		});
	}
	
	public void shutdown(){
		running = false;
		executor.shutdownNow();
	}
	
	public void queue(RedditRequest<?> request){
		log.trace("Adding request to queue, {} requests already in queue", queue.size());
		queue.add(request);
	}
	
	private void handleResponse(RedditRequest<?> request, HttpResponse<String> result){
		var headers = result.headers();
		remaining = (long) Double.parseDouble(headers.firstValue("x-ratelimit-remaining").orElse("600"));
		resetDelay = headers.firstValueAsLong("x-ratelimit-reset").orElse(600);
		used = headers.firstValueAsLong("x-ratelimit-used").orElse(0);
		log.trace("RateLimit Remaining: {}, Reset: {}, Used: {}", remaining, resetDelay, used);
		
		CompletableFuture<HttpResponse<String>> requestResult = request.getResult();
		if(!requestResult.isDone()){
			requestResult.complete(result);
		}
	}
}
