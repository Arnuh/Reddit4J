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

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class Features{
	
	@SerializedName("mod_service_mute_writes")
	private boolean modServiceMuteWrites;
	
	@SerializedName("promoted_trend_blanks")
	private boolean promotedTrendBlanks;
	
	@SerializedName("show_amp_link")
	private boolean showAmpLink;
	
	@SerializedName("top_content_email_digest_v2")
	private Experiment topContentEmailDigestV2;
	
	@SerializedName("chat")
	private boolean chat;
	
	@SerializedName("is_email_permission_required")
	private boolean emailPermissionRequired;
	
	@SerializedName("mod_awards")
	private boolean modAwards;
	
	@SerializedName("expensive_coins_package")
	private boolean expensive_coins_package;
	
	@SerializedName("mweb_xpromo_revamp_v2")
	private Experiment mwebXpromoRevampV2;
	
	@SerializedName("awards_on_streams")
	private boolean awardsOnStreams;
	
	@SerializedName("chat_subreddit")
	private boolean chatSubreddit;
	
	@SerializedName("cookie_consent_banner")
	private boolean cookieConsentBanner;
	
	@SerializedName("modlog_copyright_removal")
	private boolean modlogCopyrightRemoval;
	
	@SerializedName("show_nps_survey")
	private boolean showNpsSurvey;
	
	@SerializedName("do_not_track")
	private boolean doNotTrack;
	
	@SerializedName("mod_service_mute_reads")
	private boolean modServiceMuteReads;
	
	@SerializedName("chat_user_settings")
	private boolean chatUserSetings;
	
	@SerializedName("use_pref_account_deployment")
	private boolean usePrefAccountDeployment;
	
	@SerializedName("noreferrer_to_noopener")
	private boolean noreferrerToNoopener;
	
	@SerializedName("premium_subscriptions_table")
	private boolean premiumSubscriptionsTable;
	
	@SerializedName("crowd_control_for_post")
	private boolean crowdControlForPost;
	
	@SerializedName("mweb_nsfw_xpromo")
	private Experiment mwebNsfwXpromo;
	
	@SerializedName("chat_group_rollout")
	private boolean chatGroupRollout;
	
	@SerializedName("resized_styles_images")
	private boolean resizedStylesImages;
	
	@SerializedName("spez_modal")
	private boolean spezModal;
	
	@SerializedName("images_in_comments")
	private boolean imagesInComments;
	
	@SerializedName("mweb_sharing_web_share_api")
	private JsonObject mwebSharingWebShareApi; // TODO: owner, variant, experiment_id
	
	@SerializedName("mweb_xpromo_modal_listing_click_daily_dismissible_android")
	private boolean mwebXpromoModalListingClickDailyDismissibleAndroid;
	
	@SerializedName("mweb_xpromo_modal_listing_click_daily_dismissible_ios")
	private boolean mwebXpromoModalListingClickDailyDismissibleIos;
	
	@SerializedName("mweb_xpromo_interstitial_comments_android")
	private boolean mwebXpromoInterstitialCommentsAndroid;
	
	@SerializedName("mweb_xpromo_interstitial_comments_ios")
	private boolean mwebXpromoInterstitialCommentsIos;
}
