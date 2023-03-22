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
import ca.arnah.reddit4j.objects.reddit.Kind;
import ca.arnah.reddit4j.objects.reddit.comment.Comment;
import ca.arnah.reddit4j.objects.reddit.comment.CommentNode;
import ca.arnah.reddit4j.objects.reddit.comment.CommentSettings;
import ca.arnah.reddit4j.objects.reddit.comment.CommentSort;
import ca.arnah.reddit4j.objects.reddit.comment.LinkCommentSettings;
import ca.arnah.reddit4j.objects.reddit.comment.RootCommentNode;
import ca.arnah.reddit4j.objects.response.listings.LinkCommentResponse;
import ca.arnah.reddit4j.requests.Endpoint;
import ca.arnah.reddit4j.requests.RedditRequest;

public class LinkReference extends ContributeReference{
	
	public LinkReference(RedditClient client, String linkId){
		super(client, linkId, Kind.LINK);
	}
	
	/**
	 * Submit a new comment.
	 * @param text The raw markdown body of the comment
	 */
	public RedditRequest<Comment> comment(String text){
		return client.getRequestFactory()
			.request(Comment.class)
			.endpoint(Endpoint.POST_COMMENT)
			.post(Map.of("thing_id", getFullName(), "text", text, "return_rtjson", String.valueOf(true)))
			.build();
	}
	
	/**
	 * Same as {@link #comments(CommentSort)} but uses {@link CommentSort#Confidence} as the default sort.<br>
	 * @see #commentsAsync()
	 */
	public CommentNode comments(){
		return comments(CommentSort.Confidence);
	}
	
	/**
	 * @see #commentsAsync(CommentSort)
	 */
	public CommentNode comments(CommentSort commentSort){
		var settings = new LinkCommentSettings(CommentSettings.DEFAULT_LIMIT, getFullName(), commentSort);
		LinkCommentResponse response = client.getRequestFactory()
			.request(LinkCommentResponse.class)
			.endpoint(Endpoint.GET_COMMENTS_ARTICLE, List.of(getId()))
			.parameter("limit", settings.getLimit())
			.build()
			.execute();
		return new RootCommentNode(settings, response.getComments());
	}
	
	/**
	 * Same as {@link #commentsAsync(CommentSort)} but uses {@link CommentSort#Confidence} as the default sort.
	 */
	public CompletableFuture<CommentNode> commentsAsync(){
		return commentsAsync(CommentSort.Confidence);
	}
	
	public CompletableFuture<CommentNode> commentsAsync(CommentSort commentSort){
		var settings = new LinkCommentSettings(CommentSettings.DEFAULT_LIMIT, getFullName(), commentSort);
		return client.getRequestFactory()
			.request(LinkCommentResponse.class)
			.endpoint(Endpoint.GET_COMMENTS_ARTICLE, List.of(getId()))
			.parameter("limit", settings.getLimit())
			.build()
			.executeAsync()
			.thenApply(res->new RootCommentNode(settings, res.getComments()));
	}
}
