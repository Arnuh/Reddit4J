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

package ca.arnah.reddit4j.objects.reddit.link;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * <a href="https://www.reddit.com/dev/api#POST_api_submit">To see missing options</a>
 **/
@Getter
public class Submission{
	
	
	public static Submission builder(SubmissionKind kind){
		return new Submission(kind);
	}
	
	private final SubmissionKind kind;
	private String subreddit;
	private String title;
	private String text;
	private String url;
	
	private boolean spoiler;
	private boolean nsfw;
	
	private boolean sendReplies = true; // Default
	
	private boolean resubmit; // I think this is defaulted to true?
	
	protected Submission(SubmissionKind kind){
		this.kind = kind;
	}
	
	public Submission subreddit(String subreddit){
		this.subreddit = subreddit;
		return this;
	}
	
	public Submission title(String title){
		if(title.length() > 300) throw new IllegalArgumentException("Title cannot be longer than 300 characters");
		this.title = title;
		return this;
	}
	
	public Submission text(String text){
		this.text = text;
		return this;
	}
	
	public Submission url(String url){
		this.url = url;
		return this;
	}
	
	public Submission spoiler(boolean spoiler){
		this.spoiler = spoiler;
		return this;
	}
	
	public Submission nsfw(boolean nsfw){
		this.nsfw = nsfw;
		return this;
	}
	
	public Submission sendReplies(boolean sendReplies){
		this.sendReplies = sendReplies;
		return this;
	}
	
	/**
	 * If a link with the same URL has already been submitted to the specified subreddit an error will be returned when <code>resubmit</code> is false
	 */
	public Submission resubmit(boolean resubmit){
		this.resubmit = resubmit;
		return this;
	}
	
	public Map<String, String> toMap(){
		if(subreddit == null){
			throw new IllegalArgumentException("Subreddit cannot be null");
		}
		if(title == null){
			throw new IllegalArgumentException("Title cannot be null");
		}
		if(kind == SubmissionKind.Link && url == null){
			throw new IllegalArgumentException("URL cannot be null for link submissions");
		}
		if(kind == SubmissionKind.Self && text == null){
			throw new IllegalArgumentException("Text cannot be null for self submissions");
		}
		Map<String, String> result = new HashMap<>();
		result.put("sr", subreddit);
		result.put("title", title);
		if(text != null){
			result.put("text", text);
		}
		if(url != null){
			// video_poster_url for SubmissionKind.Video ?
			result.put("url", url);
		}
		result.put("kind", kind.name().toLowerCase());
		result.put("spoiler", Boolean.toString(spoiler));
		result.put("nsfw", Boolean.toString(nsfw));
		result.put("sendreplies", Boolean.toString(sendReplies));
		result.put("resubmit", Boolean.toString(resubmit));
		// TODO:
		// extension is used for determining which view-type (e.g. json, compact etc.) to use for the redirect that is generated if the resubmit error occurs.
		// Can't tell what it does? Documentation also says "extension used for redirects"
		//
		// flair_id	 a string no longer than 36 characters
		// flair_texta string no longer than 64 characters
		//
		// event_end(beta) a datetime string e.g. 2018-09-11T12:00:00
		// event_start	(beta) a datetime string e.g. 2018-09-11T12:00:00
		// event_tz	(beta) a pytz timezone e.g. America/Los_Angeles
		//
		// collection_id	 (beta) the UUID of a collection
		//
		// post_set_default_post_id	 a string
		// post_set_id a string
		
		// TODO: Polls, undocumentated?
		// "duration" = time in days
		// "options" = array of string options
		// raw_rtjson: "{\"document\":[{\"e\":\"par\",\"c\":[{\"e\":\"text\",\"t\":\"poll\"}]}]}"
		return result;
	}
}
