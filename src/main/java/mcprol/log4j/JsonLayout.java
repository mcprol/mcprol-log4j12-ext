/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 * some ideas from:
 *   https://github.com/thmshmm/log4j-json-layout
 *   https://github.com/michaeltandy/log4j-json 
 */

package mcprol.log4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.spi.LoggingEvent;


public class JsonLayout extends mcprol.log4j.PatternLayout {
	private PatternParser parser;
	private StringBuffer buffer = new StringBuffer(BUF_SIZE);
	
	public JsonLayout() {
		this(DEFAULT_CONVERSION_PATTERN);
	}

	public JsonLayout(String pattern) {
		this.parser = createPatternParser((pattern == null) ? DEFAULT_CONVERSION_PATTERN : pattern);
		this.parser.parse();
	}

	@Override
	public void setConversionPattern(String conversionPattern) {
		this.parser = createPatternParser(conversionPattern);
		this.parser.parse();
	}

	@Override
	public String format(LoggingEvent event) {
		ObjectMapper objectMapper = new ObjectMapper();
		ObjectNode jsonObject = objectMapper.createObjectNode();
		
		Set<PatternConverter> patternConverters = parser.getPatternConverters();
		Iterator<PatternConverter> iterator = patternConverters.iterator();
		while (iterator.hasNext()) {
			PatternConverter pc = iterator.next();
			String key = parser.getPatternConverterKey(pc);
			if (null != key) {
				// Reset working stringbuffer
				if (buffer.capacity() > MAX_CAPACITY) {
					buffer = new StringBuffer(BUF_SIZE);
				} else {
					buffer.setLength(0);
				}
				
				pc.format(buffer, event);
				String val = buffer.toString().trim();
				if (val!=null && val.length()>0) {
					jsonObject.put(key, val);
				}
			}		
		}
		
        if (event.getThrowableStrRep() != null) {
            String stacktrace = String.join("", event.getThrowableStrRep());
			jsonObject.put("Stacktrace", stacktrace);
        }

		return jsonObject.toString();
	}

	
	public boolean ignoresThrowable() {
		return false;
	}
}
