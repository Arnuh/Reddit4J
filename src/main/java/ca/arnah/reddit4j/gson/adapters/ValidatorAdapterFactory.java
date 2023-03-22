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

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import lombok.extern.log4j.Log4j2;

/**
 * <a href="https://github.com/google/gson/issues/188#issuecomment-282746095">Original Source</a>
 */
@Log4j2
public class ValidatorAdapterFactory implements TypeAdapterFactory{
	
	@Override
	@SuppressWarnings({"rawtypes", "unchecked"})
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type){
		// If the type adapter is a reflective type adapter, we want to modify the implementation using reflection. The
		// trick is to replace the Map object used to lookup the property name. Instead of returning null if the
		// property is not found, we throw a Json exception to terminate the deserialization.
		TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
		
		// Check if the type adapter is a reflective, cause this solution only work for reflection.
		if(delegate instanceof ReflectiveTypeAdapterFactory.Adapter){
			try{
				// Get reference to the existing boundFields.
				Field f = findField(delegate.getClass(), "boundFields");
				f.setAccessible(true);
				Map boundFields = (Map) f.get(delegate);
				
				// Then replace it with our implementation throwing exception if the value is null.
				boundFields = new LinkedHashMap(boundFields){
					
					@Override
					public Object get(Object key){
						Object value = super.get(key);
						if(value == null){
							log.debug("Invalid property name: {} for type {}", key, type.getRawType().getSimpleName());
						}
						return value;
					}
				};
				// Finally, push our custom map back using reflection.
				f.set(delegate, boundFields);
			}catch(Exception e){
				// Should never happen if the implementation doesn't change.
				throw new IllegalStateException(e);
			}
		}
		return delegate;
	}
	
	private static Field findField(Class<?> startingClass, String fieldName) throws NoSuchFieldException{
		for(Class<?> c = startingClass; c != null; c = c.getSuperclass()){
			try{
				return c.getDeclaredField(fieldName);
			}catch(NoSuchFieldException e){
				// OK: continue with superclasses
			}
		}
		throw new NoSuchFieldException(fieldName + " starting from " + startingClass.getName());
	}
}