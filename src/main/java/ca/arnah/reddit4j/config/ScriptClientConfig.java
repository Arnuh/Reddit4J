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

package ca.arnah.reddit4j.config;

import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import ca.arnah.reddit4j.factories.RedditRequestFactory;
import ca.arnah.reddit4j.objects.app.script.Credentials;
import ca.arnah.reddit4j.objects.app.script.PersonalUseScript;
import ca.arnah.reddit4j.objects.app.script.UserAgent;
import ca.arnah.reddit4j.objects.reddit.AccessToken;
import ca.arnah.reddit4j.requests.RedditRequest;
import ca.arnah.reddit4j.requests.RequestPreprocessor;

public class ScriptClientConfig implements RedditClientConfig{
	
	private final RequestPreprocessor userAgentPreprocessor;
	
	private final Supplier<RedditRequest<AccessToken>> getAccessToken;
	
	private AccessToken accessToken;
	
	private final boolean hasCredentials;
	private final ReentrantLock accessTokenLock = new ReentrantLock(false);
	private final RedditRequestFactory requestFactory;
	
	public ScriptClientConfig(PersonalUseScript personalUseScript, UserAgent userAgent){
		this.userAgentPreprocessor = request->request.header(USER_AGENT, userAgent.toString());
		this.hasCredentials = false;
		this.requestFactory = getRedditRequestFactory();
		// Access token appears to have separate ratelimit?
		// But since we have no ratelimit data from reddit it doesn't delay the initial access token request.
		this.getAccessToken = ()->{
			var accessTokenBuilder = new RedditRequest.Builder<>(requestFactory, "https://www.reddit.com/api/v1/access_token", request->this.userAgentPreprocessor.preprocess(request)
				.header(AUTHORIZATION, personalUseScript.toString()), AccessToken.class, null);
			return accessTokenBuilder.post(Map.of("grant_type", "client_credentials")).build();
		};
	}
	
	public ScriptClientConfig(PersonalUseScript personalUseScript, UserAgent userAgent, Credentials credentials){
		this.userAgentPreprocessor = request->request.header(USER_AGENT, userAgent.toString());
		this.hasCredentials = true;
		this.requestFactory = getRedditRequestFactory();
		this.getAccessToken = ()->{
			var accessTokenBuilder = new RedditRequest.Builder<>(requestFactory, "https://www.reddit.com/api/v1/access_token", request->this.userAgentPreprocessor.preprocess(request)
				.header(AUTHORIZATION, personalUseScript.toString()), AccessToken.class, null);
			return accessTokenBuilder.post(Map.of("grant_type", "password", "username", credentials.getUsername(), "password", credentials.getPassword()))
				.build();
		};
	}
	
	@Override
	public boolean validate(){
		if(this.accessToken == null || this.accessToken.isExpired()){
			this.accessToken = this.getAccessToken.get().execute();
		}
		return true;
	}
	
	@Override
	public RedditRequestFactory getRedditRequestFactory(){
		if(requestFactory != null){
			return requestFactory;
		}
		return new RedditRequestFactory("https://oauth.reddit.com", request->{
			accessTokenLock.lock();
			try{
				validate();
			}finally{
				accessTokenLock.unlock();
			}
			return this.userAgentPreprocessor.preprocess(request).header(AUTHORIZATION, "bearer " + this.accessToken.getAccessToken());
		});
	}
	
	@Override
	public boolean hasCredentials(){
		return hasCredentials;
	}
}
