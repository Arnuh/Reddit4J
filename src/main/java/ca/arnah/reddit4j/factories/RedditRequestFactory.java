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

package ca.arnah.reddit4j.factories;

import java.lang.reflect.Type;
import ca.arnah.reddit4j.requests.RateLimiter;
import ca.arnah.reddit4j.requests.RedditRequest;
import ca.arnah.reddit4j.requests.RequestPreprocessor;

public class RedditRequestFactory{
	
	private final String baseUrl;
	private final RequestPreprocessor requestPreprocessor;
	private final RateLimiter rateLimiter;
	
	public RedditRequestFactory(String baseUrl, RequestPreprocessor requestPreprocessor){
		this.baseUrl = baseUrl;
		this.requestPreprocessor = requestPreprocessor;
		this.rateLimiter = new RateLimiter();
	}
	
	public <R> RedditRequest.Builder<R> request(Class<R> responseClass){
		return new RedditRequest.Builder<>(this, baseUrl, requestPreprocessor, responseClass, null);
	}
	
	public <R> RedditRequest.ListingBuilder<R> listing(Class<R> responseClass){
		return new RedditRequest.ListingBuilder<>(this, baseUrl, requestPreprocessor, responseClass, null);
	}
	
	public <R> RedditRequest.ListingBuilder<R> listing(Type type){
		return new RedditRequest.ListingBuilder<>(this, baseUrl, requestPreprocessor, null, type);
	}
	
	public RateLimiter getRateLimiter(){
		return rateLimiter;
	}
}
