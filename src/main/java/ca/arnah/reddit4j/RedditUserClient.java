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

package ca.arnah.reddit4j;

import java.io.IOException;
import ca.arnah.reddit4j.config.RedditClientConfig;
import ca.arnah.reddit4j.objects.reddit.PreferenceSettings;
import ca.arnah.reddit4j.objects.reddit.TrophyList;
import ca.arnah.reddit4j.objects.reddit.User;
import ca.arnah.reddit4j.objects.reddit.UserList;
import ca.arnah.reddit4j.objects.response.account.GetMyKarmaResponse;
import ca.arnah.reddit4j.requests.Endpoint;
import ca.arnah.reddit4j.requests.RedditRequest;

public class RedditUserClient extends RedditClient{
	
	public RedditUserClient(RedditClientConfig redditClientConfig){
		super(redditClientConfig);
	}
	
	public RedditRequest<User> getMe(){
		return getRequestFactory().request(User.class).endpoint(Endpoint.GET_ME).build();
	}
	
	public RedditRequest<UserList> getFriendList() throws IOException{
		// docs say it's an actual listing, unable to confirm that.
		return getRequestFactory().request(UserList.class).endpoint(Endpoint.GET_ME_FRIENDS).build();
	}
	
	public RedditRequest<GetMyKarmaResponse> getMyKarma(){
		return getRequestFactory().request(GetMyKarmaResponse.class).endpoint(Endpoint.GET_ME_KARMA).build();
	}
	
	public RedditRequest<PreferenceSettings> getMyPreferences(){
		return getRequestFactory().request(PreferenceSettings.class).endpoint(Endpoint.GET_ME_PREFS).build();
	}
	
	public RedditRequest<TrophyList> getMyTrophies(){
		return getRequestFactory().request(TrophyList.class).endpoint(Endpoint.GET_ME_TROPHIES).build();
	}
	
	public RedditRequest<PreferenceSettings> updateMyPreferences(PreferenceSettings settings){
		return getRequestFactory().request(PreferenceSettings.class).endpoint(Endpoint.PATCH_ME_PREFS).patch(settings).build();
	}
}
