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

package ca.arnah.reddit4j.gson.adapters;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import ca.arnah.reddit4j.objects.reddit.Link;
import ca.arnah.reddit4j.objects.reddit.Listing;
import ca.arnah.reddit4j.objects.reddit.NestedIdentifiable;
import ca.arnah.reddit4j.objects.reddit.Thing;
import ca.arnah.reddit4j.objects.response.listings.LinkCommentResponse;
import ca.arnah.reddit4j.requests.Endpoint;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

/**
 * Certain endpoints return an array of Listings. Known examples are {@link Endpoint#GET_DUPLICATES} and {@link Endpoint#GET_COMMENTS_ARTICLE}<br>
 * Since {@link Endpoint#GET_DUPLICATES} is just the original Link you're looking for duplicates of, and the duplicate links in separate listings, no custom
 * handling actually needs to exist and can be parsed by a normal array type if that option is good enough.<br>
 * But, {@link Endpoint#GET_COMMENTS_ARTICLE} is 2 separate types of listings being Link and Comment. This Adapter Factory handles such cause and returns a
 * {@link LinkCommentResponse} with both listings, and its proper types.
 */
public class ArrayListingAdapterFactory implements TypeAdapterFactory{
	
	@Override
	@SuppressWarnings("unchecked")
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type){
		if(type.getRawType().equals(LinkCommentResponse.class)){
			var linkType = new TypeToken<Thing<Listing<Link>>>(){};
			var commentType = new TypeToken<Thing<Listing<NestedIdentifiable>>>(){};
			return (TypeAdapter<T>) new LinkCommentDataAdapter(gson.getAdapter(linkType), gson.getAdapter(commentType));
		}
		return null;
	}
	
	private static class LinkCommentDataAdapter extends TypeAdapter<LinkCommentResponse>{
		
		private final TypeAdapter<?> linkAdapter;
		private final TypeAdapter<?> commentAdapter;
		
		public LinkCommentDataAdapter(TypeAdapter<Thing<Listing<Link>>> linkAdapter, TypeAdapter<Thing<Listing<NestedIdentifiable>>> commentAdapter){
			this.linkAdapter = linkAdapter;
			this.commentAdapter = commentAdapter;
		}
		
		@Override
		public void write(JsonWriter out, LinkCommentResponse value) throws IOException{
			throw new UnsupportedEncodingException();
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public LinkCommentResponse read(JsonReader in) throws IOException{
			in.beginArray();
			Listing<Link> links = (Listing<Link>) linkAdapter.read(in);
			Listing<NestedIdentifiable> comments = (Listing<NestedIdentifiable>) commentAdapter.read(in);
			in.endArray();
			return new LinkCommentResponse(links, comments);
		}
	}
}
