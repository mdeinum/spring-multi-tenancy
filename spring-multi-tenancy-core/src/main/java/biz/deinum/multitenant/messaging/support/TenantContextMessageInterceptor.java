/*
 * Copyright 2007-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package biz.deinum.multitenant.messaging.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.StringUtils;

import biz.deinum.multitenant.context.TenantContext;
import biz.deinum.multitenant.context.TenantContextHolder;

/**
 * {@code ChannelInterceptor} which reads and writes the context from/to a header in the message.
 *
 * @author Marten Deinum
 * @since 1.3.0
 */
public class TenantContextMessageInterceptor extends ChannelInterceptorAdapter {

	private static final String DEFAULT_HEADER_NAME = TenantContextHolder.class.getName() + ".CONTEXT";

	private final Logger logger = LoggerFactory.getLogger(TenantContextMessageInterceptor.class);

	private String headerName = DEFAULT_HEADER_NAME;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {

		String tenant = TenantContextHolder.getContext().getTenant();
		if (StringUtils.hasText(tenant)) {
			return MessageBuilder
					.fromMessage(message)
					.setHeader(this.headerName, tenant)
					.build();
		}
		return message;
	}

	@Override
	public Message<?> postReceive(Message<?> message, MessageChannel messageChannel) {

		String tenant = message.getHeaders().get(this.headerName, String.class);
		this.logger.debug("Using tenant: {}", tenant);
		if (StringUtils.hasText(tenant)) {
			TenantContext context = TenantContextHolder.createEmptyContext();
			context.setTenant(tenant);
			TenantContextHolder.setContext(context);
		}
		return message;
	}

	@Override
	public void afterReceiveCompletion(Message<?> message, MessageChannel channel, Exception ex) {
		TenantContextHolder.clearContext();
	}

	protected String getHeaderName() {
		return this.headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
}
