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

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.reflect.Method;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.log4j.helpers.FormattingInfo;
import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.spi.LoggingEvent;

class JMXPatternConverter extends PatternConverter {
	private final String ATTRIBUTE = "attribute=";
	private final String METHOD = "method=";
	private final String TYPE = "type=";
	
	private final String TYPE_MEMORY = "Memory";
	
	private String fqan;
	private String domain;
	private String method;
	private String type;
	private String attribute;

	JMXPatternConverter(FormattingInfo formattingInfo, String fqan) {
		super(formattingInfo);
		this.fqan = fqan;
	}

	public String convert(LoggingEvent event) {
		String val = null;
		if (fqan == null) {
			val = null;
		} else {
			try {
				parseFQAN();
				Object value = getJMXBeanValue();
				if (null != value) {
					val = value.toString();
				}
			} catch (Exception e) {
				val = e.toString();
			}
		}
		return val;
	}
	
	private Object getJMXBeanValue() throws Exception {
		if (attribute != null) {
			return getJMXBeanAttributeValue(domain, attribute);
		}
		
		Object o = null;
		if (TYPE_MEMORY.equalsIgnoreCase(type)) {
			MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
			o = memoryBean;				
		}
		
		if (o != null) {
			String[] methods = method.split("\\.");
			for (int i=0; i<methods.length; i++) {
				String methodName = "get" + methods[i];
				Method m = o.getClass().getMethod(methodName);
				m.setAccessible(true);
				o = m.invoke(o);
			}
			
			return o;
		}		
		
		return null;
	}

	private void parseFQAN() throws Exception {
		// pattern: java.lang:type=Runtime,attribute=VmVersion
		int separatorIndex = fqan.lastIndexOf(ATTRIBUTE);
		if (separatorIndex != -1) {
			domain = fqan.substring(0, separatorIndex-1);
			attribute = fqan.substring(separatorIndex+ATTRIBUTE.length());
			return;
		}
		
		// pattern: java.lang:type=Memory,method=MemoryUsage
		separatorIndex = fqan.lastIndexOf(METHOD);
		if (separatorIndex != -1) {
			domain = fqan.substring(0, separatorIndex-1);
			method = fqan.substring(separatorIndex+METHOD.length());
			
			separatorIndex = domain.lastIndexOf(TYPE);
			type = domain.substring(separatorIndex+TYPE.length());

			return;
		}	
		
		throw new Exception("Cannot parse key '" + fqan + "'");
	}
	
	public Object getJMXBeanAttributeValue(String domain, String attribute) throws Exception {
		MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
		ObjectName objectName = ObjectName.getInstance(domain);			
		Object val = mbeanServer.getAttribute(objectName, attribute);
		
		return val;
	}	
	
}