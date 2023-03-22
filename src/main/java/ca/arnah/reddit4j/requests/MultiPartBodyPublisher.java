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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

public class MultiPartBodyPublisher{
	
	private final List<PartsSpecification> partsSpecificationList = new ArrayList<>();
	
	public HttpRequest.BodyPublisher build(){
		if(partsSpecificationList.size() == 0){
			throw new IllegalStateException("Must have at least one part to build multipart message.");
		}
		return HttpRequest.BodyPublishers.ofByteArrays(PartsIterator::new);
	}
	
	public int calculateLength(){
		int length = 0;
		for(var part : partsSpecificationList){
			length += part.name.length() + part.value.length();
			++length;
		}
		length += partsSpecificationList.size() - 1;
		return length;
	}
	
	public MultiPartBodyPublisher addPart(Map.Entry<String, String> entry){
		return addPart(entry.getKey(), entry.getValue());
	}
	
	public MultiPartBodyPublisher addPart(String name, String value){
		PartsSpecification newPart = new PartsSpecification();
		newPart.type = PartsSpecification.TYPE.STRING;
		newPart.name = name;
		newPart.value = URLEncoder.encode(value, StandardCharsets.UTF_8);
		partsSpecificationList.add(newPart);
		return this;
	}
	
	static class PartsSpecification{
		
		public enum TYPE{
			STRING,
		}
		
		PartsSpecification.TYPE type;
		String name;
		String value;
		
	}
	
	class PartsIterator implements Iterator<byte[]>{
		
		private final Iterator<PartsSpecification> iter;
		
		private boolean done;
		private byte[] next;
		private boolean first = true;
		
		PartsIterator(){
			iter = partsSpecificationList.iterator();
		}
		
		@Override
		public boolean hasNext(){
			if(done) return false;
			if(next != null) return true;
			try{
				next = computeNext();
			}catch(IOException e){
				throw new UncheckedIOException(e);
			}
			if(next == null){
				done = true;
				return false;
			}
			return true;
		}
		
		@Override
		public byte[] next(){
			if(!hasNext()) throw new NoSuchElementException();
			byte[] res = next;
			next = null;
			return res;
		}
		
		private byte[] computeNext() throws IOException{
			if(!iter.hasNext()) return null;
			PartsSpecification nextPart = iter.next();
			if(PartsSpecification.TYPE.STRING.equals(nextPart.type)){
				String part = (first ? "" : "&") + nextPart.name + "=" + nextPart.value;
				first = false;
				return part.getBytes(StandardCharsets.UTF_8);
			}
			throw new UnsupportedEncodingException();
		}
	}
}