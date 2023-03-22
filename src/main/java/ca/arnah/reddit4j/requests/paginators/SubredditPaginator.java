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
import ca.arnah.reddit4j.objects.reddit.Link;
import ca.arnah.reddit4j.objects.reddit.Listing;
import ca.arnah.reddit4j.objects.reddit.SubredditSort;
import ca.arnah.reddit4j.objects.reddit.Thing;
import ca.arnah.reddit4j.objects.reddit.TimePeriod;
import ca.arnah.reddit4j.requests.RedditRequest;
import com.google.gson.reflect.TypeToken;

public class SubredditPaginator extends Paginator<Link>{
	
	private static final Type type = new TypeToken<Thing<Listing<Link>>>(){}.getType();
	private final String subreddit;
	private final SubredditSort sorting;
	private final TimePeriod timePeriod;
	
	protected SubredditPaginator(RedditClient redditClient, String subreddit, SubredditSort sorting, TimePeriod timePeriod, int limit, Class<Link> clazz){
		super(redditClient, limit, clazz);
		this.subreddit = subreddit;
		this.sorting = sorting;
		this.timePeriod = timePeriod;
	}
	
	@Override
	public RedditRequest.ListingBuilder<Listing<Link>> createRequest(){
		return createRequest(type);
	}
	
	@Override
	public RedditRequest.ListingBuilder<Listing<Link>> createRequest(Type type){
		String endpoint = "";
		if(subreddit != null && !subreddit.isEmpty()){
			endpoint += "/r/" + subreddit;
		}
		if(sorting != null){
			endpoint += "/" + sorting;
		}
		RedditRequest.ListingBuilder<Listing<Link>> request = redditClient.getRequestFactory().listing(type);
		request.path(endpoint);
		if(timePeriod != null){ // only Top and Controversial
			request.parameter("t", timePeriod.toString());
		}
		return request;
	}
	
	public static class Builder extends Paginator.Builder<Link>{
		
		private final String subreddit;
		private SubredditSort sorting;
		private TimePeriod timePeriod;
		
		public Builder(RedditClient redditClient, String subreddit){
			super(redditClient, Link.class);
			this.subreddit = subreddit;
		}
		
		@Override
		public Builder limit(int limit){
			this.limit = limit;
			return this;
		}
		
		public Builder sorting(SubredditSort sorting){
			this.sorting = sorting;
			return this;
		}
		
		public Builder timePeriod(TimePeriod timePeriod){
			this.timePeriod = timePeriod;
			return this;
		}
		
		@Override
		public SubredditPaginator build(){
			return new SubredditPaginator(redditClient, subreddit, sorting, timePeriod, limit, clazz);
		}
	}
}
