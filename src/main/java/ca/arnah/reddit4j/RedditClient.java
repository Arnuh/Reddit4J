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

package ca.arnah.reddit4j;

import java.util.List;
import ca.arnah.reddit4j.config.RedditClientConfig;
import ca.arnah.reddit4j.factories.RedditRequestFactory;
import ca.arnah.reddit4j.objects.reddit.Kind;
import ca.arnah.reddit4j.objects.references.SubredditReference;
import ca.arnah.reddit4j.objects.response.listings.DuplicatesResponse;
import ca.arnah.reddit4j.objects.response.listings.GetByIdResponse;
import ca.arnah.reddit4j.requests.Endpoint;
import ca.arnah.reddit4j.requests.RedditRequest;
import ca.arnah.reddit4j.requests.paginators.SubredditPaginator;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class RedditClient{
	
	private final RedditRequestFactory requestFactory;
	
	public RedditClient(RedditClientConfig redditClientConfig){
		this.requestFactory = redditClientConfig.getRedditRequestFactory();
		if(redditClientConfig.hasCredentials() && !(this instanceof RedditUserClient)){
			log.warn("Not using a RedditUserClient when credentials were provided. Less functionality will be available.");
		}
	}
	
	public void shutdown(){
		requestFactory.getRateLimiter().shutdown();
	}
	
	public RedditRequestFactory getRequestFactory(){
		return requestFactory;
	}
	
	public SubredditReference subreddit(String subreddit){
		return new SubredditReference(this, subreddit);
	}
	
	public SubredditPaginator.Builder frontPage(){
		return new SubredditPaginator.Builder(this, null);
	}
	
	public SubredditPaginator.Builder all(){
		return new SubredditPaginator.Builder(this, "all");
	}
	
	public SubredditPaginator.Builder popular(){
		return new SubredditPaginator.Builder(this, "popular");
	}
	
	public SubredditPaginator.Builder random(){
		return new SubredditPaginator.Builder(this, "random");
	}
	
	public SubredditPaginator.Builder friends(){
		return new SubredditPaginator.Builder(this, "friends");
	}
	
	public SubredditPaginator.Builder mod(){
		return new SubredditPaginator.Builder(this, "mod");
	}
	
	/**
	 * Get a listing of links by fullname.<br>
	 * <br>
	 * Can convert a <code>link id</code> to <code>fullname</code> by using {@link Kind#toFullName(String)}
	 *
	 * @param linkFullNames Links by there fullname <code>t3_</code>
	 */
	public RedditRequest<GetByIdResponse> getById(String... linkFullNames){
		return getRequestFactory().request(GetByIdResponse.class).endpoint(Endpoint.GET_BY_ID, List.of(String.join(",", linkFullNames))).build();
	}
	
	/**
	 *
	 * @param link Link by the id <code>jptqj9</code>
	 * @return Duplicate links grouped by original post and "other discussions"
	 */
	public RedditRequest<DuplicatesResponse[]> duplicates(String link){
		return getRequestFactory().request(DuplicatesResponse[].class).endpoint(Endpoint.GET_DUPLICATES, List.of(link)).build();
	}
}
