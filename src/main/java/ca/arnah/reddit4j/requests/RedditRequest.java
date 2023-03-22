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

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import ca.arnah.reddit4j.exceptions.HttpStatusCodeException;
import ca.arnah.reddit4j.factories.RedditRequestFactory;
import ca.arnah.reddit4j.gson.adapters.ArrayListingAdapterFactory;
import ca.arnah.reddit4j.gson.adapters.BooleanNumberTypeAdapterFactory;
import ca.arnah.reddit4j.gson.adapters.ThingTypeAdapterFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class RedditRequest<R>{
	
	private static final Gson gson = new GsonBuilder().registerTypeAdapterFactory(new BooleanNumberTypeAdapterFactory())
		// Logs missing properties when parsing JSON.
		//.registerTypeAdapterFactory(new ValidatorAdapterFactory())
		.registerTypeAdapterFactory(new ThingTypeAdapterFactory()).registerTypeAdapterFactory(new ArrayListingAdapterFactory()).setPrettyPrinting().create();
	/**
	 * Following redirects for /random subreddit
	 */
	private static final HttpClient client = HttpClient.newBuilder()
		.followRedirects(HttpClient.Redirect.ALWAYS)
		.connectTimeout(Duration.of(30, ChronoUnit.SECONDS))
		.build();
	
	private final RedditRequestFactory requestFactory;
	private final RequestPreprocessor requestPreprocessor;
	private final HttpRequest.Builder request;
	private final Class<R> responseClass;
	private final Type type;
	@Getter
	private final CompletableFuture<HttpResponse<String>> result;
	
	protected RedditRequest(RedditRequestFactory requestFactory, HttpRequest.Builder request, RequestPreprocessor requestPreprocessor, Class<R> responseClass, Type type){
		this.requestFactory = requestFactory;
		this.request = request;
		this.requestPreprocessor = requestPreprocessor;
		this.responseClass = responseClass;
		this.type = type;
		this.result = new CompletableFuture<>();
	}
	
	HttpResponse<String> executeRequest() throws IOException{
		HttpResponse<String> response;
		try{
			var request = buildRequest();
			log.trace("Sending request {}", request.uri());
			response = client.send(request, HttpResponse.BodyHandlers.ofString());
			if(response.statusCode() != 200){
				throw new HttpStatusCodeException("Received status code %d from request with header %s, body %s".formatted(response.statusCode(), response.headers()
					.map(), response.body()), response.statusCode());
			}
			return response;
		}catch(InterruptedException e){
			throw new RuntimeException(e);
		}
		
	}
	
	public R execute(){
		try{
			return executeAsync().get();
		}catch(Exception ex){
			throw new RuntimeException(ex);
		}
	}
	
	public CompletableFuture<R> executeAsync(){
		requestFactory.getRateLimiter().queue(this);
		CompletableFuture<R> future = new CompletableFuture<>();
		result.whenComplete((result, throwable)->{
			if(throwable != null){
				future.completeExceptionally(throwable);
				return;
			}
			try{
				if(type != null){
					future.complete(gson.fromJson(result.body(), type));
				}else{
					future.complete(gson.fromJson(result.body(), responseClass));
				}
			}catch(Throwable ex){
				future.completeExceptionally(ex);
			}
		});
		return future;
	}
	
	private HttpRequest buildRequest() throws IOException{
		return requestPreprocessor.preprocess(request).build();
	}
	
	public static class Builder<R>{
		
		static{
			System.setProperty("jdk.httpclient.allowRestrictedHeaders", "Content-Length");
		}
		
		private final Pattern ENDPOINT_FORMAT = Pattern.compile("\\{.*?}");
		private final RedditRequestFactory requestFactory;
		private final URIBuilder uriBuilder;
		private final HttpRequest.Builder request;
		private final RequestPreprocessor requestPreprocessor;
		private final Class<R> responseClass;
		private final Type type;
		
		public Builder(RedditRequestFactory requestFactory, String url, RequestPreprocessor requestPreprocessor, Class<R> responseClass, Type type){
			this.requestFactory = requestFactory;
			this.uriBuilder = new URIBuilder(URI.create(url));
			this.requestPreprocessor = requestPreprocessor;
			this.responseClass = responseClass;
			this.type = type;
			this.request = HttpRequest.newBuilder();
		}
		
		public Builder<R> endpoint(Endpoint endpoint){
			uriBuilder.addPath(endpoint.getPath());
			return this;
		}
		
		public Builder<R> endpoint(Endpoint endpoint, List<String> args){
			String path = endpoint.getPath();
			
			Matcher matcher = null;
			for(String arg : args){
				if(matcher == null){
					matcher = ENDPOINT_FORMAT.matcher(path);
				}else{
					matcher.reset(path);
				}
				path = matcher.replaceFirst(arg);
			}
			uriBuilder.addPath(path);
			return this;
		}
		
		public Builder<R> path(String path){
			uriBuilder.addPath(path);
			return this;
		}
		
		public Builder<R> parameter(String param, Object value){
			uriBuilder.addParameter(param, value.toString());
			return this;
		}
		
		public Builder<R> post(HttpRequest.BodyPublisher body){
			this.request.POST(body);
			return this;
		}
		
		public Builder<R> post(Map<String, String> body){
			MultiPartBodyPublisher multipart = new MultiPartBodyPublisher();
			body.entrySet().forEach(multipart::addPart);
			this.request.POST(multipart.build())
				.header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
				.headers("Content-Length", String.valueOf(multipart.calculateLength()));
			return this;
		}
		
		public Builder<R> patch(HttpRequest.BodyPublisher body){
			this.request.method("PATCH", body).header("Content-Type", "application/json");
			return this;
		}
		
		public Builder<R> patch(Object body){
			this.request.method("PATCH", HttpRequest.BodyPublishers.ofString(gson.toJson(body))).header("Content-Type", "application/json");
			return this;
		}
		
		public RedditRequest<R> build(){
			request.uri(uriBuilder.build());
			return new RedditRequest<>(requestFactory, request, requestPreprocessor, responseClass, type);
		}
	}
	
	public static class ListingBuilder<R> extends RedditRequest.Builder<R>{
		
		public ListingBuilder(RedditRequestFactory requestFactory, String url, RequestPreprocessor requestPreprocessor, Class<R> responseClass, Type type){
			super(requestFactory, url, requestPreprocessor, responseClass, type);
		}
		
		public ListingBuilder<R> after(String after){
			parameter("after", after);
			return this;
		}
		
		public ListingBuilder<R> before(String before){
			parameter("before", before);
			return this;
		}
		
		public ListingBuilder<R> count(int count){
			parameter("count", String.valueOf(count));
			return this;
		}
		
		public ListingBuilder<R> limit(int limit){
			parameter("limit", String.valueOf(limit));
			return this;
		}
		
		public ListingBuilder<R> show(){
			parameter("show", "all");
			return this;
		}
	}
}
