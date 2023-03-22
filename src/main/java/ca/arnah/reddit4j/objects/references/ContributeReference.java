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

package ca.arnah.reddit4j.objects.references;

import java.util.Map;
import ca.arnah.reddit4j.RedditClient;
import ca.arnah.reddit4j.objects.reddit.Kind;
import ca.arnah.reddit4j.objects.reddit.Vote;
import ca.arnah.reddit4j.objects.response.EmptyResponse;
import ca.arnah.reddit4j.requests.Endpoint;
import ca.arnah.reddit4j.requests.RedditRequest;
import lombok.Getter;

@Getter
public abstract class ContributeReference{
	
	protected final RedditClient client;
	protected final String id;
	protected final Kind kind;
	
	public ContributeReference(RedditClient client, String id, Kind kind){
		this.client = client;
		this.id = id;
		this.kind = kind;
	}
	
	public String getFullName(){
		return kind.toFullName(id);
	}
	
	/**
	 * Upvote the referenced link or comment.
	 * <br>
	 * Note: votes must be cast by humans. That is, API clients proxying a human's action one-for-one are OK, but bots deciding how to vote on content or
	 * amplifying a human's vote are not. See the <a href="https://www.reddit.com/rules">reddit rules</a> for more details on what constitutes vote cheating.
	 */
	public RedditRequest<EmptyResponse> upvote(){
		return vote(Vote.UP);
	}
	
	/**
	 * Upvote the referenced link or comment.
	 * <br>
	 * Note: votes must be cast by humans. That is, API clients proxying a human's action one-for-one are OK, but bots deciding how to vote on content or
	 * amplifying a human's vote are not. See the <a href="https://www.reddit.com/rules">reddit rules</a> for more details on what constitutes vote cheating.
	 */
	public RedditRequest<EmptyResponse> downvote(){
		return vote(Vote.DOWN);
	}
	
	/**
	 * Clear a vote for the reference link or comment.
	 * <br>
	 * Note: votes must be cast by humans. That is, API clients proxying a human's action one-for-one are OK, but bots deciding how to vote on content or
	 * amplifying a human's vote are not. See the <a href="https://www.reddit.com/rules">reddit rules</a> for more details on what constitutes vote cheating.
	 */
	public RedditRequest<EmptyResponse> unvote(){
		return vote(Vote.CLEAR);
	}
	
	/**
	 * Cast a vote on a thing.
	 * <br>
	 * Note: votes must be cast by humans. That is, API clients proxying a human's action one-for-one are OK, but bots deciding how to vote on content or
	 * amplifying a human's vote are not. See the <a href="https://www.reddit.com/rules">reddit rules</a> for more details on what constitutes vote cheating.
	 */
	public RedditRequest<EmptyResponse> vote(Vote vote){
		return client.getRequestFactory()
			.request(EmptyResponse.class)
			.endpoint(Endpoint.POST_VOTE)
			.post(Map.of("id", getFullName(), "dir", String.valueOf(vote.getValue())))
			.build();
	}
	
	public RedditRequest<EmptyResponse> delete(){
		return client.getRequestFactory().request(EmptyResponse.class).endpoint(Endpoint.POST_DELETE).post(Map.of("id", getFullName())).build();
	}
}
