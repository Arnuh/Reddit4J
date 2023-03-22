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

package ca.arnah.reddit4j.objects.reddit;

import java.util.List;
import ca.arnah.reddit4j.RedditClient;
import ca.arnah.reddit4j.objects.references.SubredditReference;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Subreddit{
	
	public static final int MIN_SUBREDDIT_NAME_CHARS = 3;
	public static final int MAX_SUBREDDIT_NAME_CHARS = 21;
	
	@SerializedName("default_set")
	private boolean defaultSet;
	
	@SerializedName("user_is_contributor")
	private boolean userContributor;
	
	@SerializedName("banner_img")
	private String bannerImg;
	
	@SerializedName("restrict_posting")
	private boolean restrictPosting;
	
	@SerializedName("user_is_banned")
	private boolean userBanned;
	
	@SerializedName("free_form_reports")
	private boolean freeFromReports;
	
	@SerializedName("community_icon")
	private String communityIcon;
	
	@SerializedName("show_media")
	private boolean showMedia;
	
	@SerializedName("icon_color")
	private String iconColor;
	
	@SerializedName("user_is_muted")
	private String userIsMuted; // TODO: 17.07.2022 What are the possible values?
	
	@SerializedName("display_name")
	private String displayName;
	
	@SerializedName("header_img")
	private String headerImg;
	
	private String title;
	
	private int coins;
	
	@SerializedName("previous_names")
	private List<String> previousNames;
	
	@SerializedName("over_18")
	private boolean over18;
	
	@SerializedName("icon_size")
	private List<Integer> iconSize;
	
	@SerializedName("primary_color")
	private String primaryColor;
	
	@SerializedName("icon_img")
	private String iconImg;
	
	@SerializedName("description")
	private String description;
	
	@SerializedName("allowed_media_in_comments")
	private List<JsonElement> allowedMediaInComments; // TODO: 17.07.2022 What are the possible values?
	
	@SerializedName("submit_link_label")
	private String submitLinkLabel;
	
	@SerializedName("header_size")
	private String headerSize; // TODO: 17.07.2022 What are the possible values?
	
	@SerializedName("restrict_commenting")
	private boolean restrictCommenting;
	
	@SerializedName("subscribers")
	private int subscribers;
	
	@SerializedName("submit_text_label")
	private String submitTextLabel;
	
	@SerializedName("is_default_icon")
	private boolean defaultIcon;
	
	@SerializedName("link_flair_position")
	private String linkFlairPosition;
	
	@SerializedName("display_name_prefixed")
	private String displayNamePrefixed;
	
	@SerializedName("key_color")
	private String keyColor;
	
	private String name;
	
	@SerializedName("is_default_banner")
	private boolean defaultBanner;
	
	private String url;
	
	private boolean quarantine;
	
	@SerializedName("banner_size")
	private String bannerSize; // TODO: 17.07.2022 What are the possible values?
	
	@SerializedName("user_is_moderator")
	private boolean userModerator;
	
	@SerializedName("accept_followers")
	private boolean acceptFollowers;
	
	@SerializedName("public_description")
	private String publicDescription;
	
	@SerializedName("link_flair_enabled")
	private boolean linkFlairEnabled;
	
	@SerializedName("disable_contributor_requests")
	private boolean disableContributorRequests;
	
	@SerializedName("subreddit_type")
	private String subredditType; // TODO: 17.07.2022 What are the possible values?
	
	@SerializedName("user_is_subscriber")
	private boolean userSubscriber;
	
	public SubredditReference getReference(RedditClient client){
		return new SubredditReference(client, getName());
	}
	
	public static boolean isValidSubredditName(String name){
		if(name.length() < MIN_SUBREDDIT_NAME_CHARS || name.length() > MAX_SUBREDDIT_NAME_CHARS){
			return false;
		}
		if(name.startsWith("_")){
			return false;
		}
		// Regex?
		for(int i = 0; i < name.length(); i++){
			char c = name.charAt(i);
			if(!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || (c == '_' && i != 0))){
				return false;
			}
		}
		return true;
	}
}
