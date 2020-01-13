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

package mcprol.log4j.helpers;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import org.apache.log4j.helpers.FormattingInfo;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.spi.LoggingEvent;

import mcprol.java.monitor.JavaMonitor;

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
	    case 'J':
	        pc = new JMXPatternConverter(formattingInfo,  extractOption());
	        //LogLog.debug("DEFINE converter.");
	        //formattingInfo.dump();
	        currentLiteral.setLength(0);
	        break;
	    case 'S':
	        pc = new SystemPatternConverter(formattingInfo,  extractOption());
	        //LogLog.debug("SYSTEM converter.");
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

		if ('X' == c || 'D' == c || 'S' == c || 'J' == c) {
			key = currentOption;
		} else {
			switch (c) {
			case 'c':
				key = "category";
				break;
			case 'C':
				key = "className";
				break;
			case 'd':
				key = "date";
				break;
			case 'F':
				key = "fileLocation";
				break;
			case 'l':
				key = "fullLocation";
				break;
			case 'L':
				key = "lineLocation";
				break;
			case 'm':
				key = "message";
				break;
			case 'M':
				key = "methodLocation";
				break;
			case 'p':
				key = "level";
				break;
			case 'r':
				key = "relativeTime";
				break;
			case 't':
				key = "thread";
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
		
}


