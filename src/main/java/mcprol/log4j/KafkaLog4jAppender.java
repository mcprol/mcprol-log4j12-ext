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

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.spi.LoggingEvent;

public class KafkaLog4jAppender extends org.apache.kafka.log4jappender.KafkaLog4jAppender {

	private boolean appenderIsDisabled = false;
	private Integer requestTimeoutMs = null;
	private Integer maxBlockMs = null;

	public void setRequestTimeoutMs(int requestTimeoutMs) {
		this.requestTimeoutMs = requestTimeoutMs;
	}

	public void setMaxBlockMs(int maxBlockMs) {
		this.maxBlockMs = maxBlockMs;
	}

	@Override
	public void activateOptions() {
		try {
			super.activateOptions();
		} catch (Exception ex) {
			LogLog.error("[KafkaLog4jAppender initialization exception]: ", ex);
			appenderIsDisabled = true;
		}
	}

	@Override
	protected void append(LoggingEvent event) {
		if (!appenderIsDisabled) {
			super.append(event);
		}
	}

	@Override
	protected Producer<byte[], byte[]> getKafkaProducer(Properties props) {
		if (null != requestTimeoutMs) {
			props.setProperty(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, String.valueOf(requestTimeoutMs));
		}

		if (null != maxBlockMs) {
			props.setProperty(ProducerConfig.MAX_BLOCK_MS_CONFIG, String.valueOf(maxBlockMs));
		}

		return new KafkaProducer<>(props);
	}
}
