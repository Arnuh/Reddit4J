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

package ca.arnah.reddit4j.requests.paginators;

import java.lang.reflect.Type;
import ca.arnah.reddit4j.RedditClient;
import ca.arnah.reddit4j.objects.reddit.Listing;
import ca.arnah.reddit4j.requests.Endpoint;
import ca.arnah.reddit4j.requests.RedditRequest;

public abstract class DefaultPaginator<T> extends Paginator<T>{
	
	private final Endpoint endpoint;
	
	protected DefaultPaginator(RedditClient redditClient, int limit, Class<T> clazz, Endpoint endpoint){
		super(redditClient, limit, clazz);
		this.endpoint = endpoint;
	}
	
	@Override
	public RedditRequest.ListingBuilder<Listing<T>> createRequest(Type type){
		RedditRequest.ListingBuilder<Listing<T>> request = redditClient.getRequestFactory().listing(type);
		request.endpoint(endpoint);
		return request;
	}
	
	public static abstract class Builder<T> extends Paginator.Builder<T>{
		
		protected final Endpoint endpoint;
		
		public Builder(RedditClient redditClient, Class<T> clazz, Endpoint endpoint){
			super(redditClient, clazz);
			this.endpoint = endpoint;
		}
		
		@Override
		public Builder<T> limit(int limit){
			this.limit = limit;
			return this;
		}
	}
}
