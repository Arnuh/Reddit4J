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

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import ca.arnah.reddit4j.RedditClient;
import ca.arnah.reddit4j.objects.reddit.Subreddit;
import ca.arnah.reddit4j.objects.reddit.comment.CommentNode;
import ca.arnah.reddit4j.objects.reddit.comment.CommentSettings;
import ca.arnah.reddit4j.objects.reddit.comment.RootCommentNode;
import ca.arnah.reddit4j.objects.reddit.link.Submission;
import ca.arnah.reddit4j.objects.response.EmptyResponse;
import ca.arnah.reddit4j.objects.response.link.SubmissionResponse;
import ca.arnah.reddit4j.objects.response.listings.SubredditCommentResponse;
import ca.arnah.reddit4j.requests.Endpoint;
import ca.arnah.reddit4j.requests.RedditRequest;
import ca.arnah.reddit4j.requests.paginators.SubredditAboutPaginator;
import ca.arnah.reddit4j.requests.paginators.SubredditPaginator;

public class SubredditReference{
	
	private final RedditClient client;
	private final String subreddit;
	
	public SubredditReference(RedditClient client, String subreddit){
		this.client = client;
		this.subreddit = subreddit;
	}
	
	public String getSubreddit(){
		return subreddit;
	}
	
	public RedditRequest<SubmissionResponse> submit(Submission submission){
		return client.getRequestFactory().request(SubmissionResponse.class).endpoint(Endpoint.POST_SUBMIT).post(submission.toMap()).build();
	}
	
	public SubredditPaginator.Builder posts(){
		return new SubredditPaginator.Builder(client, getSubreddit());
	}
	
	/**
	 * @see #commentsAsync()
	 */
	public CommentNode comments(){
		return comments(new CommentSettings.Builder().build());
	}
	
	/**
	 * Grabs the newest comments from the {@link Subreddit}.
	 * @see #commentsAsync(CommentSettings)
	 */
	public CommentNode comments(CommentSettings settings){
		SubredditCommentResponse response = client.getRequestFactory()
			.request(SubredditCommentResponse.class)
			.endpoint(Endpoint.GET_COMMENTS_SUBREDDIT, List.of(subreddit))
			.parameter("limit", settings.getLimit())
			.build()
			.execute();
		return new RootCommentNode(settings, response.getComments());
	}
	
	public CompletableFuture<CommentNode> commentsAsync(){
		return commentsAsync(new CommentSettings.Builder().build());
	}
	
	/**
	 * Grabs the newest comments from the {@link Subreddit}.
	 */
	public CompletableFuture<CommentNode> commentsAsync(CommentSettings settings){
		return client.getRequestFactory()
			.request(SubredditCommentResponse.class)
			.endpoint(Endpoint.GET_COMMENTS_SUBREDDIT, List.of(subreddit))
			.parameter("limit", settings.getLimit())
			.build()
			.executeAsync()
			.thenApply(res->new RootCommentNode(settings, res.getComments()));
	}
	
	/**
	 * Subscribe to a subreddit.
	 * <br>
	 * The user must have access to the subreddit to be able to subscribe to it.
	 */
	public RedditRequest<EmptyResponse> subscribe(){
		return subscribe(true);
	}
	
	/**
	 * Unsubscribe from a subreddit.
	 * <br>
	 * The user must have access to the subreddit to be able to subscribe to it.
	 */
	public RedditRequest<EmptyResponse> unsubscribe(){
		return subscribe(false);
	}
	
	protected RedditRequest<EmptyResponse> subscribe(boolean subscribe){
		// Supports "sr" as a "fullname"
		// "sr_name" for "subreddit name"
		// "sr" and "sr_name" can be comma a separated list
		// The "skip_initial_defaults" param can be set to True to prevent automatically subscribing the user to the current set of defaults when
		// they take their first subscription action. Attempting to set it for an unsubscribe action will result in an error.
		return client.getRequestFactory()
			.request(EmptyResponse.class)
			.endpoint(Endpoint.POST_SUBSCRIBE)
			.post(Map.of("sr_name", getSubreddit(), "action", subscribe ? "sub" : "unsub"))
			.build();
	}
	
	// Separate "moderation tools" to separate class?
	
	/**
	 * Things that have been reported.
	 * <br>
	 * Paginator can return {@link ca.arnah.reddit4j.objects.reddit.Link} and {@link ca.arnah.reddit4j.objects.reddit.comment.Comment}.
	 * <br>
	 * Requires the "posts" moderator permission for the subreddit.
	 */
	public SubredditAboutPaginator.Builder reports(){
		return new SubredditAboutPaginator.Builder(client, Endpoint.GET_SUBREDDIT_ABOUT_LOCATION, getSubreddit(), "reports");
	}
	
	/**
	 * Things that have been marked as spam or otherwise removed.
	 * <br>
	 * Paginator can return {@link ca.arnah.reddit4j.objects.reddit.Link} and {@link ca.arnah.reddit4j.objects.reddit.comment.Comment}.
	 * <br>
	 * Requires the "posts" moderator permission for the subreddit.
	 */
	public SubredditAboutPaginator.Builder spam(){
		return new SubredditAboutPaginator.Builder(client, Endpoint.GET_SUBREDDIT_ABOUT_LOCATION, getSubreddit(), "spam");
	}
	
	/**
	 * Things requiring moderator review, such as reported things and items caught by the spam filter.
	 * <br>
	 * Paginator can return {@link ca.arnah.reddit4j.objects.reddit.Link} and {@link ca.arnah.reddit4j.objects.reddit.comment.Comment}.
	 * <br>
	 * Requires the "posts" moderator permission for the subreddit.
	 */
	public SubredditAboutPaginator.Builder modqueue(){
		return new SubredditAboutPaginator.Builder(client, Endpoint.GET_SUBREDDIT_ABOUT_LOCATION, getSubreddit(), "modqueue");
	}
	
	/**
	 * Things that have yet to be approved/removed by a mod.
	 * <br>
	 * Paginator can return {@link ca.arnah.reddit4j.objects.reddit.Link}.
	 * <br>
	 * Requires the "posts" moderator permission for the subreddit.
	 */
	public SubredditAboutPaginator.Builder unmoderated(){
		return new SubredditAboutPaginator.Builder(client, Endpoint.GET_SUBREDDIT_ABOUT_LOCATION, getSubreddit(), "unmoderated");
	}
	
	/**
	 * Things that have been edited recently.
	 * <br>
	 * Paginator can return {@link ca.arnah.reddit4j.objects.reddit.Link} and {@link ca.arnah.reddit4j.objects.reddit.comment.Comment}.
	 * <br>
	 * Requires the "posts" moderator permission for the subreddit.
	 */
	public SubredditAboutPaginator.Builder edited(){
		return new SubredditAboutPaginator.Builder(client, Endpoint.GET_SUBREDDIT_ABOUT_LOCATION, getSubreddit(), "edited");
	}
}
