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

package ca.arnah.reddit4j.requests;


import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class URIBuilder{
	
	private String scheme = null;
	private String user = null;
	private String host = null;
	private int port = -1;
	private final List<String> paths = new ArrayList<>();
	private final Map<String, String> parameters = new HashMap<>();
	private String fragment = null;
	
	public URIBuilder(){}
	
	public URIBuilder(URI uri){
		this.scheme = uri.getScheme();
		this.user = uri.getUserInfo();
		this.host = uri.getHost();
		this.port = uri.getPort();
		this.paths.addAll(List.of(uri.getRawPath().split("/")));
	}
	
	public URIBuilder addParameter(String name, String value){
		parameters.put(name, value);
		return this;
	}
	
	public URIBuilder addParameter(String name, long value){
		addParameter(name, String.valueOf(value));
		return this;
	}
	
	public URIBuilder addParameter(String name, File file){
		addParameter(name, file.toURI().getPath());
		return this;
	}
	
	public URIBuilder addPath(String path){
		paths.add(path);
		return this;
	}
	
	public URIBuilder setHost(String host){
		this.host = host;
		return this;
	}
	
	public URIBuilder setScheme(String scheme){
		this.scheme = scheme;
		return this;
	}
	
	public URIBuilder setUser(String user){
		this.user = user;
		return this;
	}
	
	public URIBuilder setPort(int port){
		this.port = port;
		return this;
	}
	
	public URIBuilder setFragment(String fragment){
		this.fragment = fragment;
		return this;
	}
	
	public URI build(){
		StringBuilder path = new StringBuilder();
		for(String p : paths){
			if(p.startsWith("/")) path.append(p);
			else path.append("/").append(p);
		}
		
		StringBuilder query = new StringBuilder();
		for(var entry : parameters.entrySet()){
			if(!query.isEmpty()) query.append("&");
			query.append(entry.getKey()).append("=").append(entry.getValue());
		}
		try{
			return new URI(scheme, user, host, port, path.length() > 0 ? path.toString() : null, query.length() > 0 ? query.toString() : null, fragment);
		}catch(URISyntaxException e){
			throw new RuntimeException(e);
		}
	}
}