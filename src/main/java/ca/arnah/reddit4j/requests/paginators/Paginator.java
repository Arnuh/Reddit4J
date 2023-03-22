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

package ca.arnah.reddit4j.requests.paginators;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import ca.arnah.reddit4j.RedditClient;
import ca.arnah.reddit4j.objects.reddit.Listing;
import ca.arnah.reddit4j.requests.RedditRequest;
import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class Paginator<T> implements RedditIterable<T>{
	
	public static final int DEFAULT_LIMIT = 25;
	public static final int MAX_LIMIT = 100;
	
	protected final RedditClient redditClient;
	protected final int limit;
	protected final Class<T> clazz;
	protected int pageNumber;
	protected Listing<T> current;
	
	protected Paginator(RedditClient redditClient, int limit, Class<T> clazz){
		this.redditClient = redditClient;
		this.limit = limit;
		this.clazz = clazz;
	}
	
	@Override
	public Iterator<Listing<T>> iterator(){
		return new Iterator<>(){
			@Override
			public boolean hasNext(){
				return (current == null && pageNumber == 0) || (current != null && current.getAfter() != null);
			}
			
			@Override
			public Listing<T> next(){
				return Paginator.this.next();
			}
		};
	}
	
	@Override
	public Listing<T> next(){
		try{
			var request = createRequest().limit(limit);
			if(current != null){
				request.after(current.getAfter());
			}
			++pageNumber;
			current = request.build().execute();
		}catch(Exception ex){
			log.catching(ex);
			current = null;
		}
		return current;
	}
	
	@Override
	public List<Listing<T>> accumulate(int pages){
		if(pages < 0){
			throw new IllegalArgumentException("Can not accumlate less than 0 pages");
		}
		List<Listing<T>> result = new ArrayList<>();
		int count = 0;
		var iterator = iterator();
		while(++count <= pages && iterator().hasNext()){
			result.add(iterator.next());
		}
		return result;
	}
	
	@Override
	public List<T> accumulateFlatten(int pages){
		return accumulate(pages).stream().flatMap(l->l.getData().stream()).collect(Collectors.toList());
	}
	
	public abstract RedditRequest.ListingBuilder<Listing<T>> createRequest();
	
	public abstract RedditRequest.ListingBuilder<Listing<T>> createRequest(Type type);
	
	public abstract static class Builder<T>{
		
		protected final RedditClient redditClient;
		
		protected final Class<T> clazz;
		
		protected int limit = DEFAULT_LIMIT;
		
		// sorting, time period
		public Builder(RedditClient redditClient, Class<T> clazz){
			this.redditClient = redditClient;
			this.clazz = clazz;
		}
		
		public Builder<T> limit(int limit){
			this.limit = limit;
			return this;
		}
		
		public abstract Paginator<T> build();
	}
}
