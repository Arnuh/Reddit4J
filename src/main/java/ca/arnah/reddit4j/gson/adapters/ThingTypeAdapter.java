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
import ca.arnah.reddit4j.objects.reddit.Kind;
import ca.arnah.reddit4j.objects.reddit.Listing;
import ca.arnah.reddit4j.objects.reddit.Thing;
import ca.arnah.reddit4j.objects.reddit.TrophyList;
import ca.arnah.reddit4j.objects.reddit.UserList;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * Type Adapter for {@link Thing} json data that will handle deserializing and returning the value of
 * {@link Thing#getData()}.
 * <p>
 * This class assumes that the data is encapsulated like the following:
 * <pre>
 * {
 *   "kind": "kind",
 *   "data": { ... }
 * }
 * </pre>
 */
public class ThingTypeAdapter<T> extends TypeAdapter<T>{
	
	private final Gson gson;
	private final TypeAdapter<String> kindAdapter;
	private final TypeAdapterFactory skipPast;
	private final TypeToken<T> dataTypeToken;
	
	public ThingTypeAdapter(Gson gson, TypeAdapterFactory skipPast, TypeToken<T> dataTypeToken){
		this.gson = gson;
		this.kindAdapter = gson.getAdapter(String.class);
		this.skipPast = skipPast;
		this.dataTypeToken = dataTypeToken;
	}
	
	@Override
	public void write(JsonWriter out, T value) throws IOException{
		throw new UnsupportedEncodingException();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public T read(JsonReader in) throws IOException{
		// Comment replies can be an empty string if null instead of a null keyword.
		// Handle that specific case here.
		if(in.peek() == JsonToken.STRING){
			in.nextString();
			return null;
		}
		in.beginObject();
		Thing<T> data = new Thing<>();
		while(in.hasNext()){
			String name = in.nextName();
			if(name.equals("kind")){
				data.setKind(Kind.get(kindAdapter.read(in)));
			}else if(name.equals("data")){
				TypeAdapter<?> adapter;
				if(data.getKind().equals(Kind.LISTING)){
					adapter = gson.getAdapter(TypeToken.getParameterized(Listing.class, dataTypeToken.getType()));
				}else if(data.getKind().equals(Kind.TROPHY_LIST)){
					// TrophyList is a copy of Listing but with only a Trophy type supported and "trophies" instead of "children"
					adapter = gson.getDelegateAdapter(skipPast, TypeToken.get(TrophyList.class));
				}else if(data.getKind().equals(Kind.USER_LIST)){
					// repeat
					adapter = gson.getDelegateAdapter(skipPast, TypeToken.get(UserList.class));
				}else{
					adapter = gson.getAdapter(ThingTypeAdapterFactory.REGISTRY.get(data.getKind()));
				}
				data.setData((T) adapter.read(in));
			}else{
				in.skipValue();
			}
		}
		in.endObject();
		return data.getData();
	}
}