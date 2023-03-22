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
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import ca.arnah.reddit4j.gson.KindObject;
import ca.arnah.reddit4j.objects.reddit.Kind;
import ca.arnah.reddit4j.objects.reddit.Link;
import ca.arnah.reddit4j.objects.reddit.Listing;
import ca.arnah.reddit4j.objects.reddit.Subreddit;
import ca.arnah.reddit4j.objects.reddit.Thing;
import ca.arnah.reddit4j.objects.reddit.Trophy;
import ca.arnah.reddit4j.objects.reddit.TrophyList;
import ca.arnah.reddit4j.objects.reddit.UserList;
import ca.arnah.reddit4j.objects.reddit.comment.Comment;
import ca.arnah.reddit4j.objects.reddit.comment.MoreChildren;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

/**
 * A Gson TypeAdapterFactory to use {@link ThingTypeAdapter} for deserializing {@link Thing} json objects.
 * <p>
 * This TypeAdapterFactory can be automatically used by adding the {@link KindObject} annotation to a class that should
 * be deserialized like a {@link Thing}.
 * <br>
 * Since a class might not always be a {@link Thing} object, field
 * specific deserializing can be handled by adding the {@link JsonAdapter} annotation and using the
 * {@link Annotation} factory.
 * <br>
 * Deserializing a class as a {@link Thing} in only certain scenarios can be handled by creating a
 * {@link TypeToken} for the class and passing it to the called fromJson {@link Gson#fromJson(String, Type)}.
 *
 * @see Thing
 * @see ThingTypeAdapter
 * @see Annotation
 * @see KindObject
 */
@SuppressWarnings("unchecked")
public class ThingTypeAdapterFactory implements TypeAdapterFactory{
	
	public static final Map<Kind, Class<?>> REGISTRY = new HashMap<>();
	public static final Map<Class<?>, Kind> REGISTRY_REVERSE;
	
	static{
		
		REGISTRY.put(Kind.COMMENT, Comment.class);
		REGISTRY.put(Kind.LINK, Link.class);
		REGISTRY.put(Kind.SUBREDDIT, Subreddit.class);
		REGISTRY.put(Kind.TROPHY, Trophy.class);
		REGISTRY.put(Kind.LISTING, Listing.class);
		REGISTRY.put(Kind.TROPHY_LIST, TrophyList.class);
		REGISTRY.put(Kind.MORE_CHILDREN, MoreChildren.class);
		REGISTRY.put(Kind.USER_LIST, UserList.class);
		
		REGISTRY_REVERSE = REGISTRY.entrySet().stream().collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
	}
	
	@Override
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type){
		if(type.getType() instanceof ParameterizedType parameterizedType && type.getRawType().equals(Thing.class)){
			TypeToken<?> paramTypeToken = TypeToken.get(parameterizedType.getActualTypeArguments()[0]);
			if(paramTypeToken.getType() instanceof ParameterizedType paramType){
				return (TypeAdapter<T>) new ThingTypeAdapter<>(gson, this, TypeToken.get(paramType.getActualTypeArguments()[0]));
			}
			ThingTypeAdapter<?> adapter = new ThingTypeAdapter<>(gson, this, paramTypeToken);
			return (TypeAdapter<T>) adapter;
		}else if(type.getRawType().isAnnotationPresent(KindObject.class)){
			ThingTypeAdapter<?> adapter = new ThingTypeAdapter<>(gson, this, type);
			return (TypeAdapter<T>) adapter;
		}
		return null;
	}
	
	/**
	 * A Gson TypeAdapterFactory to create a {@link ThingTypeAdapter} for a {@link Thing} json object.
	 * This factory is only required if the data class being encapsulated is not marked with {@link KindObject}.
	 * <p>
	 * Gson provides no way to get annotations for fields inside a class, requiring the use of a {@link JsonAdapter}
	 * annotation and this factory instead of the {@link KindObject} annotation.
	 * <p>
	 * See {@link ThingTypeAdapterFactory} for a factory that works with {@link KindObject} annotated classes.
	 *
	 * @see ThingTypeAdapter
	 * @see ListAdapter
	 */
	public static class Annotation implements TypeAdapterFactory{
		
		@Override
		public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type){
			if(type.getRawType().isAssignableFrom(List.class)){
				ParameterizedType parameterizedType = (ParameterizedType) type.getType();
				ListAdapter<?> adapter = new ListAdapter<>(gson, TypeToken.get(parameterizedType.getActualTypeArguments()[0]));
				return (TypeAdapter<T>) adapter;
			}
			if(type.getType() instanceof ParameterizedType paramType){
				ThingTypeAdapter<?> adapter = new ThingTypeAdapter<>(gson, this, TypeToken.get(paramType.getActualTypeArguments()[0]));
				return (TypeAdapter<T>) adapter;
			}
			return null;
		}
	}
	
	/**
	 * Type Adapter for a {@link List} containing {@link Thing} objects.
	 * <p>
	 * This class assumes that the data is encapsulated like the following:
	 * <pre>
	 * "field":[
	 *   {
	 *     "kind": "kind",
	 *     "data": { ... }
	 *   },
	 *   {
	 *     "kind": "kind",
	 *     "data": { ... }
	 *   }
	 * ]
	 * </pre>
	 */
	private static class ListAdapter<T> extends TypeAdapter<List<T>>{
		
		private final TypeAdapter<T> adapter;
		
		public ListAdapter(Gson gson, TypeToken<T> paramType){
			this.adapter = new ThingTypeAdapter<>(gson, null, paramType);
		}
		
		@Override
		public void write(JsonWriter out, List<T> value) throws IOException{
			out.beginArray();
			for(T item : value){
				adapter.write(out, item);
			}
			out.endArray();
		}
		
		@Override
		public List<T> read(JsonReader in) throws IOException{
			if(in.peek() == JsonToken.BEGIN_ARRAY){
				in.beginArray();
				List<Object> list = new ArrayList<>();
				while(in.hasNext()){
					list.add(adapter.read(in));
				}
				in.endArray();
				return (List<T>) list;
			}
			return null;
		}
	}
}