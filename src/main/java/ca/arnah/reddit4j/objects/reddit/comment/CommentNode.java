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

package ca.arnah.reddit4j.objects.reddit.comment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import ca.arnah.reddit4j.RedditClient;
import ca.arnah.reddit4j.objects.reddit.NestedIdentifiable;
import ca.arnah.reddit4j.objects.response.comments.MoreChildrenResponse;
import lombok.Getter;

public class CommentNode implements Iterable<ReplyCommentNode>{
	
	private final CommentSettings settings;
	@Getter
	private final List<ReplyCommentNode> replies;
	private MoreChildren moreChildren;
	
	public CommentNode(CommentSettings settings){
		this.settings = settings;
		this.replies = new ArrayList<>();
	}
	
	protected void init(Iterable<NestedIdentifiable> replies){
		if(replies == null) return;
		for(NestedIdentifiable ni : replies){
			if(ni instanceof MoreChildren more && !more.getChildren().isEmpty()){ // Is ignoring empty MoreChildren proper?
				this.moreChildren = more;
			}else if(ni instanceof Comment comment){
				this.replies.add(new ReplyCommentNode(settings, comment));
			}
		}
	}
	
	/**
	 * Loads
	 * @throws IllegalStateException If {@link #hasMoreChildren()} is false
	 */
	public void loadMore(RedditClient client) throws IOException, IllegalStateException{
		if(!hasMoreChildren()){
			throw new IllegalStateException("No more children");
		}
		if(moreChildren.getChildren().isEmpty()){
			moreChildren = null;
			return;
		}
		// TODO: Properly handle loading more "subreddit comments"
		if(settings instanceof LinkCommentSettings linkSettings){
			MoreChildrenResponse res = moreChildren.request(client, linkSettings).execute();
			// TODO: Errors
			// TODO: Multiple MoreChildren can exist from this response.
			if(moreChildren.getChildren().isEmpty()){
				moreChildren = null; // Means this is useless now?
			}
			try{
				init(res.getData());
			}catch(Exception ex){
				ex.printStackTrace();
			}
		}
	}
	
	public boolean hasMoreChildren(){
		return moreChildren != null;
	}
	
	@Override
	public Iterator<ReplyCommentNode> iterator(){
		return replies.iterator();
	}
}
