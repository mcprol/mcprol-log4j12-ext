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

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;

public class Log4jTestMain {
	
	private final static Logger logger = Logger.getLogger(Log4jTestMain.class);
		
	public static void main(String[] args) throws Exception {
		MDC.put("s", "99999");
		MDC.put("t", "00000");
		
		//logger.info("Running " + Log4jTestMain.class.getName() + "... ");
		
		for (int i=0; i<10; i++) {
			logger.info("iteration " + (i+1));
			Integer[] ii = new Integer[1000000];
		}
	}
}
