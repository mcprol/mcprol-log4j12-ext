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

package mcprol.log4j;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.helpers.FormattingInfo;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.spi.LoggingEvent;

public class PatternParser extends org.apache.log4j.helpers.PatternParser {
	
	private Map<PatternConverter, String> patternConvertersMap = new LinkedHashMap<>();
	private PatternConverter currentPatternConverter = null;
	private String currentOption = null;
	
	public PatternParser(String pattern) {
		super(pattern);
	}
	
	
	public PatternConverter parse() {
		PatternConverter pc = super.parse();

		return pc;
	}

	
	protected void addConverter(PatternConverter pc) {
		super.addConverter(pc);
		currentPatternConverter = pc;
	    LogLog.debug("Add Converter currentKey: \""+pc.getClass().getName()+"\".");		
	}

	
	protected void finalizeConverter(char c) {
	    PatternConverter pc = null;
		switch (c) {
	    case 'D':
	        pc = new DefinePatternConverter(formattingInfo,  extractOption());
	        //LogLog.debug("DEFINE converter.");
	        //formattingInfo.dump();
	        currentLiteral.setLength(0);
	        break;
		default:
			super.finalizeConverter(c);
		}
		
		if (null != pc) {
		    addConverter(pc);
		}
		
	    LogLog.debug("Parsed Converter currentKey: \""+c+"\".");

	    if (null != currentPatternConverter) {
	    	String key = getPatternConverterKey(c);
			patternConvertersMap.put(currentPatternConverter, key);
			currentPatternConverter = null;
			currentOption = null;
		}
	}
	
	
	protected String extractOption() {
		currentOption = super.extractOption();
	    LogLog.debug("Parsed Converter option: \""+currentOption+"\".");
		return currentOption;
	}


	public Set<PatternConverter> getPatternConverters() {
		return patternConvertersMap.keySet();
	}
	
	
	private String getPatternConverterKey(char c) {
		String key = String.valueOf(c);

		if ('X' == c || 'D' == c) {
			key = currentOption;
		} else {
			switch (c) {
			case 'c':
				key = "Category";
				break;
			case 'C':
				key = "ClassName";
				break;
			case 'd':
				key = "Date";
				break;
			case 'F':
				key = "FileLocation";
				break;
			case 'l':
				key = "FullLocation";
				break;
			case 'L':
				key = "LineLocation";
				break;
			case 'm':
				key = "Message";
				break;
			case 'M':
				key = "MethodLocation";
				break;
			case 'p':
				key = "Level";
				break;
			case 'r':
				key = "RelativeTime";
				break;
			case 't':
				key = "Thread";
				break;
			case 'x':
				key = "NDC";
				break;
			}
		}

		return key;
	}
	
	
	public String getPatternConverterKey(PatternConverter pc) {
		return patternConvertersMap.get(pc);
	}
		
	
	private class DefinePatternConverter extends PatternConverter {
		private String key;
	
		DefinePatternConverter(FormattingInfo formattingInfo, String key) {
			super(formattingInfo);
			this.key = key;
		}
	
		public String convert(LoggingEvent event) {
			String val = null;
			if (key == null) {
				val = null;
			} else {
				val = System.getProperty(key);
			}
			return val;
		}
	}

}


