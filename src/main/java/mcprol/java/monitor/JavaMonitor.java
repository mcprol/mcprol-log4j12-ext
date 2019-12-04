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

package mcprol.java.monitor;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

/*
 * https://docs.oracle.com/javase/8/docs/api/java/lang/management/package-summary.html
 */
public class JavaMonitor {
	public static long getFreeMemory() {
		return Runtime.getRuntime().freeMemory();
	}
	
	public static double getSystemLoadAverage() {
		OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
		return operatingSystemMXBean.getSystemLoadAverage();
	}
}
