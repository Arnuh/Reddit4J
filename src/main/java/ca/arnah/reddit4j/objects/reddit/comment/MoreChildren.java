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

import java.util.List;
import ca.arnah.reddit4j.RedditClient;
import ca.arnah.reddit4j.objects.reddit.NestedIdentifiable;
import ca.arnah.reddit4j.objects.response.comments.MoreChildrenResponse;
import ca.arnah.reddit4j.requests.Endpoint;
import ca.arnah.reddit4j.requests.RedditRequest;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MoreChildren extends NestedIdentifiable{
	
	public static final int MORE_CHILDREN_LIMIT = 100;
	
	private int depth;
	
	private int count;
	
	private List<String> children;
	
	public RedditRequest<MoreChildrenResponse> request(RedditClient client, LinkCommentSettings settings){
		List<String> result = children.subList(0, Math.min(MORE_CHILDREN_LIMIT, children.size()));
		String childrenReq = String.join(",", result);
		result.clear();
		return client.getRequestFactory()
			.request(MoreChildrenResponse.class)
			.endpoint(Endpoint.GET_MORE_CHILDREN)
			.parameter("children", childrenReq)
			.parameter("api_type", "json")
			.parameter("link_id", settings.getLinkFullName())
			.parameter("sort", settings.getSort().getName())
			.build();
	}
}
