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

import java.security.InvalidParameterException;
import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public enum Kind{
	@SerializedName("KarmaList") KARMA_LIST("KarmaList"),
	@SerializedName("TrophyList") TROPHY_LIST("TrophyList"),
	@SerializedName("t1") COMMENT("t1"),
	@SerializedName("t3") LINK("t3"),
	@SerializedName("t5") SUBREDDIT("t5"),
	@SerializedName("t6") TROPHY("t6"),
	@SerializedName("Listing") LISTING("Listing"),
	@SerializedName("more") MORE_CHILDREN("more"),
	@SerializedName("UserList") USER_LIST("UserList"),
	;
	
	private static final Kind[] values = values();
	
	private final String prefix;
	
	Kind(String name){
		this.prefix = name;
	}
	
	public String toFullName(String id){
		return "%s_%s".formatted(prefix, id);
	}
	
	public static Kind get(String name){
		for(Kind kind : values){
			if(kind.prefix.equals(name)) return kind;
		}
		throw new InvalidParameterException("Unknown type " + name);
	}
}
