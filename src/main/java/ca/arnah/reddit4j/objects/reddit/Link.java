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
import java.util.Map;
import java.util.UUID;
import ca.arnah.reddit4j.RedditClient;
import ca.arnah.reddit4j.objects.reddit.link.PollData;
import ca.arnah.reddit4j.objects.references.LinkReference;
import ca.arnah.reddit4j.requests.Endpoint;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Link extends NestedIdentifiable{
	
	/**
	 * Time since epoch in seconds.<br>
	 * <code>null</code> if not approved.
	 */
	@SerializedName("approved_at_utc")
	private Long approvedAtUtc;
	
	@SerializedName("subreddit")
	private String subreddit;
	
	@SerializedName("selftext")
	private String selftext;
	
	@SerializedName("author_flair_text_color")
	private String authorFlairTextColor;
	
	@SerializedName("author")
	private String author;
	
	/**
	 * {@code t3_id}
	 */
	@SerializedName("author_fullname")
	private String authorFullname;
	
	@SerializedName("author_premium")
	private String authorPremium;
	
	@SerializedName("author_is_blocked")
	private boolean authorBlocked;
	
	@SerializedName("author_patreon_flair")
	private boolean authorPatreonFlair;
	
	@SerializedName("author_cakeday")
	private boolean authorCakeDay;
	
	@SerializedName("author_flair_text")
	private String authorFlairText;
	
	@SerializedName("author_flair_type")
	private String authorFlairType;
	
	@SerializedName("author_flair_background_color")
	private String authorFlairBackgroundColor;
	
	@SerializedName("author_flair_template_id")
	private String authorFlairTemplateId;
	
	@SerializedName("author_flair_css_class")
	private String authorFlairCssClass;
	
	@SerializedName("author_flair_richtext")
	private JsonArray authorFlairRichtext; // TODO: What are the possible values?
	
	private boolean saved;
	
	@SerializedName("mod_reason_title")
	private String modReasonTitle;
	
	private int gilded;
	
	private boolean clicked;
	
	@SerializedName("is_gallery")
	private boolean gallery;
	
	private String title;
	
	@SerializedName("poll_data")
	private PollData pollData;
	
	@SerializedName("link_flair_richtext")
	private JsonArray linkFlairRichtext;
	
	@SerializedName("subreddit_name_prefixed")
	private String subredditNamePrefixed;
	
	private boolean hidden;
	
	private int pwls;
	
	@SerializedName("link_flair_css_class")
	private String linkFlairCssClass;
	
	private int downs;
	
	@SerializedName("thumbnail_height")
	private int thumbnail_height;
	
	@SerializedName("top_awarded_type")
	private String topAwardedType;
	
	@SerializedName("hide_score")
	private boolean hideScope;
	
	private boolean quarantine;
	
	@SerializedName("link_flair_text_color")
	private String linkFlairTextColor;
	
	@SerializedName("upvote_ratio")
	private double upvoteRatio;
	
	@SerializedName("subreddit_type")
	private String subredditType;
	
	private int ups;
	
	@SerializedName("total_awards_received")
	private int totalAwardsReceived;
	
	@SerializedName("media_embed")
	private MediaEmbed mediaEmbed;
	
	@SerializedName("thumbnail_width")
	private int thumbnailWidth;
	
	@SerializedName("is_original_content")
	private boolean originalContent;
	
	@SerializedName("user_reports")
	private JsonArray userReports; // TODO: What are the possible values?
	
	@SerializedName("secure_media")
	private Media secureMedia;
	
	@SerializedName("is_reddit_media_domain")
	private boolean redditMediaDomain;
	
	@SerializedName("is_meta")
	private boolean meta;
	
	private String category;
	
	@SerializedName("secure_media_embed")
	private SecureMediaEmbed secureMediaEmbed;
	
	@SerializedName("link_flair_text")
	private String linkFlairText;
	
	@SerializedName("can_mod_post")
	private boolean canModPost;
	
	private int score;
	
	@SerializedName("approved_by")
	private String approvedBy;
	
	@SerializedName("is_created_from_ads_ui")
	private boolean createdFromAdsUi;
	
	private String thumbnail;
	
	private double edited;
	
	private JsonElement gildings; // TODO: What are the possible values?
	
	@SerializedName("post_hint")
	private String postHint;
	
	@SerializedName("content_categories")
	private String[] contentCategories;
	
	@SerializedName("is_self")
	private boolean self;
	
	@SerializedName("mod_note")
	private String modNote;
	
	private long created;
	
	@SerializedName("link_flair_type")
	private String linkFlairType;
	
	private int wls;
	
	@SerializedName("removed_by_category")
	private String removedByCategory;
	
	@SerializedName("banned_by")
	private String bannedBy;
	
	private String domain;
	
	@SerializedName("allow_live_comments")
	private boolean allowLiveComments;
	
	@SerializedName("selftext_html")
	private String selftextHtml;
	
	private String likes;
	
	@SerializedName("suggested_sort")
	private String suggestedSort;
	
	@SerializedName("banned_at_utc")
	private String bannedAtUtc;
	
	@SerializedName("url_overridden_by_dest")
	private String urlOverriddenByDest;
	
	@SerializedName("view_count")
	private String viewCount;
	
	private boolean archived;
	
	@SerializedName("no_follow")
	private boolean noFollow;
	
	@SerializedName("is_crosspostable")
	private boolean crosspostable;
	
	private boolean pinned;
	
	@SerializedName("over_18")
	private boolean over18;
	
	@SerializedName("preview")
	private Preview preview;
	
	@SerializedName("all_awardings")
	private JsonArray allAwardings;
	
	private JsonArray awarders;
	
	@SerializedName("media_only")
	private boolean mediaOnly;
	
	@SerializedName("can_gild")
	private Boolean canGild;
	
	@SerializedName("spoiler")
	private boolean spoiler;
	
	@SerializedName("locked")
	private boolean locked;
	
	@SerializedName("treatment_tags")
	private JsonArray treatmentTags;
	
	private boolean visited;
	
	@SerializedName("removed_by")
	private String removedBy;
	
	@SerializedName("num_reports")
	private String numReports;
	
	@SerializedName("distinguished")
	private String distinguished;
	
	@SerializedName("subreddit_id")
	private String subredditId;
	
	@SerializedName("mod_reason_by")
	private String modReasonBy;
	
	@SerializedName("removal_reason")
	private String removalReason;
	
	@SerializedName("link_flair_background_color")
	private String linkFlairBackgroundColor;
	
	@SerializedName("is_robot_indexable")
	private boolean robotIndexable;
	
	/**
	 * This attribute is deprecated. Please use mod_reports and user_reports instead.
	 */
	@Deprecated
	@SerializedName("report_reasons")
	private List<String> reportReasons;
	
	@SerializedName("discussion_type")
	private String discussionType;
	
	@SerializedName("num_comments")
	private int numComments;
	
	@SerializedName("send_replies")
	private boolean sendReplies;
	
	@SerializedName("whitelist_status")
	private String whitelistStatus;
	
	@SerializedName("contest_mode")
	private boolean contestMode;
	
	@SerializedName("mod_reports")
	private JsonArray modReports;
	
	private String permalink;
	
	@SerializedName("parent_whitelist_status")
	private String parentWhitelistStatus;
	
	private boolean stickied;
	
	private String url;
	
	@SerializedName("subreddit_subscribers")
	private int subredditSubscribers;
	
	@SerializedName("created_utc")
	private long createdUtc;
	
	@SerializedName("num_crossposts")
	private int numCrossposts;
	
	private Media media;
	
	@SerializedName("is_video")
	private boolean video;
	
	@SerializedName("media_metadata")
	private Map<String, Metadata> mediaMetadata;
	
	@SerializedName("gallery_data")
	private GalleryData galleryData;
	
	@SerializedName("crosspost_parent_list")
	private List<Link> crosspostParentList;
	
	/**
	 * Number of "Other Discussions", only given when using {@link Endpoint#GET_COMMENTS_ARTICLE}
	 */
	@SerializedName("num_duplicates")
	private int numDuplicates;
	
	@SerializedName("link_flair_template_id")
	private UUID linkFlairTemplateId;
	
	@SerializedName("ignore_reports")
	private boolean ignoreReports;
	
	private boolean spam;
	
	private boolean removed;
	
	private boolean approved;
	
	/**
	 * The message displayed next to the approval button.<br>
	 * <br>
	 * <code>confirm spam</code> and <code>remove not spam</code> are two of the possible values.
	 */
	@SerializedName("ban_note")
	private String banNote;
	
	/**
	 * Type, Count, Unk Bool, Unk Bool
	 */
	@SerializedName("user_reports_dismissed")
	private Object[][] userReportsDismissed;
	
	@Getter
	@ToString
	public static class MediaEmbed{
		
		private String content;
		
		private int width;
		
		private boolean scrolling;
		
		private int height;
	}
	
	@Getter
	@ToString
	public static class SecureMediaEmbed extends MediaEmbed{
		
		@SerializedName("media_domain_url")
		private String mediaDomainUrl;
	}
	
	public LinkReference getReference(RedditClient client){
		return new LinkReference(client, getId());
	}
}
