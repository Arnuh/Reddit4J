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

import lombok.Getter;

@Getter
public enum Endpoint{
	// Account
	GET_ME("/api/v1/me"),
	// GET_ME_BLOCKED("/api/v1/me/blocked"),
	GET_ME_FRIENDS("/api/v1/me/friends"),
	GET_ME_KARMA("/api/v1/me/karma"),
	GET_ME_PREFS("/api/v1/me/prefs"),
	PATCH_ME_PREFS("/api/v1/me/prefs"),
	GET_ME_TROPHIES("/api/v1/me/trophies"),
	GET_PREFS_WHERE("/prefs/{where}"),
	// Captcha
	// Collections
	// Emoji
	// Flair
	// Reddit Gold
	// Links & Comments
	/**
	 * Represents an endpoint described <a href="https://www.reddit.com/dev/api#POST_api_comment">here</a>.
	 */
	POST_COMMENT("api/comment"),
	/**
	 * Represents an endpoint described <a href="https://www.reddit.com/dev/api#POST_api_del">here</a>.
	 */
	POST_DELETE("api/del"),
	/**
	 * Represents an endpoint described <a href="https://www.reddit.com/dev/api#POST_api_editusertext">here</a>.
	 */
	POST_EDITUSERTEXT("api/editusertext"),
	GET_MORE_CHILDREN("/api/morechildren"),
	/**
	 * Represents an endpoint described <a href="https://www.reddit.com/dev/api#POST_api_vote">here</a>.
	 */
	POST_VOTE("api/vote"),
	// Listings
	GET_BY_ID("/by_id/{names}"),
	GET_COMMENTS_SUBREDDIT("r/{subreddit}/comments"),
	GET_COMMENTS_ARTICLE("/comments/{article}"),
	GET_DUPLICATES("/duplicates/{article}"),
	// Live Threads
	// Private Messages
	// Misc
	// Moderation
	/**
	 * Represents an endpoint described <a href="https://www.reddit.com/dev/api#GET_about_%7Blocation%7D">here</a>.
	 */
	GET_SUBREDDIT_ABOUT_LOCATION("r/{subreddit}/about/{location}"),
	// New Modmail
	// Modnote
	// Multis
	// Search
	// Subreddits
	/**
	 * Represents an endpoint described <a href="https://www.reddit.com/dev/api#POST_api_subscribe">here</a>.
	 */
	POST_SUBSCRIBE("api/subscribe")
	// Users
	// Widgets
	// Wiki
	;
	
	private final String path;
	
	Endpoint(String path){
		this.path = path;
	}
}
