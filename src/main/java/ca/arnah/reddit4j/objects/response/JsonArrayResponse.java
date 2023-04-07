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

package ca.arnah.reddit4j.objects.response;

import java.util.List;
import ca.arnah.reddit4j.gson.adapters.ThingTypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class JsonArrayResponse<T>{
	
	private JsonResArrayObject<T> json;
	
	@Getter
	@ToString
	public static class JsonResArrayObject<T>{
		
		/**
		 * Example:<br>
		 * <code>
		 * "ALREADY_SUB"<br>
		 * "This community doesn't allow links to be posted more than once, and this link has already been shared"<br>
		 * "url"<br>
		 * </code>
		 */
		private List<List<String>> errors;
		
		private JsonResArrayData<T> data;
	}
	
	@Getter
	@ToString
	public static class JsonResArrayData<T>{
		
		@JsonAdapter(ThingTypeAdapterFactory.Annotation.class)
		private List<T> things;
	}
}
