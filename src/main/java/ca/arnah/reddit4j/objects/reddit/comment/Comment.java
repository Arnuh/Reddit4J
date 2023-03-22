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

import java.util.Map;
import java.util.UUID;
import ca.arnah.reddit4j.RedditClient;
import ca.arnah.reddit4j.gson.adapters.ThingTypeAdapterFactory;
import ca.arnah.reddit4j.objects.reddit.Link;
import ca.arnah.reddit4j.objects.reddit.Listing;
import ca.arnah.reddit4j.objects.reddit.Metadata;
import ca.arnah.reddit4j.objects.reddit.NestedIdentifiable;
import ca.arnah.reddit4j.objects.reddit.flair.FlairRichText;
import ca.arnah.reddit4j.objects.references.CommentReference;
import ca.arnah.reddit4j.objects.references.SubredditReference;
import com.google.gson.JsonArray;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Comment extends NestedIdentifiable{
	
	/**
	 * Fullname of the {@link Link} this comment is associated with.
	 */
	@SerializedName("link_id")
	private String linkId;
	
	/**
	 * Subreddit fullname
	 */
	@SerializedName("subreddit_id")
	private String subredditId;
	
	private String subreddit;
	
	/**
	 * Subreddit Name with <code>r/</code> prefix.
	 */
	@SerializedName("subreddit_name_prefixed")
	private String subredditNamePrefixed;
	
	private String author;
	
	@SerializedName("author_fullname")
	private String authorFullname;
	
	/**
	 * Time since epoch in seconds.<br>
	 */
	@SerializedName("created_utc")
	private long createdUtc;
	
	/**
	 * Same as {@link #getCreatedUtc()}
	 */
	private long created;
	
	private int score;
	
	@SerializedName("score_hidden")
	private boolean scoreHidden;
	
	private String body;
	
	@SerializedName("body_html")
	private String bodyHTML;
	
	private String permalink;
	
	/**
	 * Time since epoch in seconds.<br>
	 * <code>null</code> if not approved.
	 */
	@SerializedName("approved_at_utc")
	private Long approvedAtUtc;
	
	private double edited;
	
	/**
	 * For emotes the key can be something like <code>emote|free_emotes_pack|joy</code>
	 */
	@SerializedName("media_metadata")
	private Map<String, Metadata> mediaMetadata;
	
	@SerializedName("subreddit_type")
	private String subredditType; // TODO: Enum?
	
	@SerializedName("author_is_blocked")
	private boolean authorIsBlocked;
	
	@SerializedName("comment_type")
	private String commentType; // TODO: Values?
	
	private String[] awarders; // TODO: Values?
	
	@SerializedName("total_awards_received")
	private int totalAwardsReceived;
	
	/**
	 * Determines your upvote status of this comment.
	 * <code>null</code> being nothing, <code>true</code> being upvoted, <code>false</code> being downvoted.
	 */
	private Boolean likes;
	
	private int ups;
	
	private int downs;
	
	@JsonAdapter(ThingTypeAdapterFactory.Annotation.class)
	private Listing<NestedIdentifiable> replies;
	
	private boolean saved;
	
	private int gilded;
	
	private int controversiality;
	
	private int depth;
	
	@SerializedName("can_gild")
	private boolean canGild;
	
	private Map<String, Integer> gildings;
	
	@SerializedName("all_awardings")
	private JsonArray allAwardings; // TODO: Object
	
	@SerializedName("top_awarded_type")
	private Integer topAwardedType; // TODO: Values?
	@SerializedName("associated_award")
	private Boolean associatedAward; // TODO: Values?
	
	@SerializedName("send_replies")
	private boolean sendReplies;
	
	@SerializedName("is_submitter")
	private boolean isSubmitter;
	
	@SerializedName("author_premium")
	private boolean authorPremium;
	
	@SerializedName("author_cakeday")
	private boolean authorCakeDay;
	
	@SerializedName("treatment_tags")
	private String[] treatmentTags;
	
	private boolean archived;
	
	private boolean stickied;
	
	private boolean distinguished;
	
	private boolean collapsed;
	
	@SerializedName("collapsed_because_crowd_control")
	private Boolean collapsedBecauseCrowdControl;
	
	private boolean locked;
	
	@SerializedName("collapsed_reason_code")
	private String collapsedReasonCode; // TODO: Values? "DELETED"
	
	@SerializedName("collapsed_reason")
	private String collapsedReason;
	
	@SerializedName("unrepliable_reason")
	private String unrepliableReason;
	
	@SerializedName("no_follow")
	private boolean noFollow;
	
	@SerializedName("author_flair_text")
	private String authorFlairText;
	
	@SerializedName("author_flair_type")
	private String authorFlairType;
	
	@SerializedName("author_flair_template_id")
	private UUID authorFlairTemplateId;
	
	@SerializedName("author_flair_css_class")
	private String authorFlairCSSClass;
	
	@SerializedName("author_flair_richtext")
	private FlairRichText[] authorFlairRichText;
	
	@SerializedName("author_patreon_flair")
	private boolean authorPatreonFlair;
	
	@SerializedName("author_flair_text_color")
	private String authorFlairTextColor;
	
	@SerializedName("author_flair_background_color")
	private String authorFlairBackgroundColor;
	
	@SerializedName("can_mod_post")
	private boolean canModPost;
	
	@SerializedName("mod_note")
	private String modNote;
	
	@SerializedName("mod_reason_by")
	private String modReasonBy;
	
	@SerializedName("mod_reason_title")
	private String modReasonTitle;
	
	@SerializedName("banned_by")
	private String bannedBy;
	
	/**
	 * Time since epoch in seconds.<br>
	 * <code>null</code> if not banned.
	 */
	@SerializedName("banned_at_utc")
	private Long bannedAtUtc;
	
	@SerializedName("user_reports")
	private JsonArray userReports; // TODO:
	
	/**
	 * Who approved it.<br>
	 * <code>null</code> if not approved, or you can't see who approved.
	 */
	@SerializedName("approved_by")
	private String approvedBy;
	
	private boolean approved;
	
	@SerializedName("removal_reason")
	private String removalReason;
	
	@SerializedName("report_reasons")
	private Integer[] reportReasons; // TODO: Values
	
	@SerializedName("mod_reports")
	private JsonArray modReports; // TODO: Values
	
	@SerializedName("num_reports")
	private Integer numReports;
	
	@SerializedName("ignore_reports")
	private boolean ignoreReports;
	
	private boolean spam;
	
	private boolean removed;
	
	/**
	 * The message displayed next to the approval button.<br>
	 * <br>
	 * "spam", "confirm spam", "remove not spam", and "reinforce spam" are possible values.
	 */
	@SerializedName("ban_note")
	private String banNote;
	
	/**
	 * Type, Count, Unk Bool, Unk Bool
	 */
	@SerializedName("user_reports_dismissed")
	private Object[][] userReportsDismissed;
	
	/**
	 * Type, mod name<br>
	 * Type can be <code>This is spam</code>
	 */
	@SerializedName("mod_reports_dismissed")
	private Object[][] modReportsDismissed;
	
	// TODO: Below appear to be specific to GET_COMMENTS_SUBREDDIT endpoint, add a "SubredditComment" class?
	
	/**
	 * Full https url of the {@link Link} this comment belongs to.<br>
	 * Only available when retrieving comments from a {@link SubredditReference} rather than a specific {@link Link}
	 */
	@SerializedName("link_url")
	private String linkUrl;
	
	/**
	 * Full https url of the {@link Link} this comment belongs to.<br>
	 * Only available when retrieving comments from a {@link SubredditReference} rather than a specific {@link Link}
	 */
	@SerializedName("link_permalink")
	private String linkPermalink;
	
	/**
	 * Only available when retrieving comments from a {@link SubredditReference} rather than a specific {@link Link}
	 */
	@SerializedName("link_title")
	private String linkTitle;
	
	/**
	 * Only available when retrieving comments from a {@link SubredditReference} rather than a specific {@link Link}
	 */
	@SerializedName("link_author")
	private String linkAuthor;
	
	private boolean quarantine;
	
	@SerializedName("over_18")
	private boolean over18;
	
	@SerializedName("num_comments")
	private int numComments;
	
	public CommentReference getReference(RedditClient client){
		return new CommentReference(client, getId());
	}
}
