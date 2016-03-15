package biz.deinum.multitenant.messaging.support;

import biz.deinum.multitenant.context.TenantContext;
import biz.deinum.multitenant.context.TenantContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.StringUtils;

/**
 * {@code ChannelInterceptor} which reads and writes the context to a header in the message.
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

		String context = TenantContextHolder.getContext().getTenant();
		if (StringUtils.hasText(context)) {
			return MessageBuilder
					.fromMessage(message)
					.setHeader(this.headerName, context)
					.build();
		}
		return message;
	}

	@Override
	public Message<?> postReceive(Message<?> message, MessageChannel messageChannel) {

		String tenant = message.getHeaders().get(this.headerName, String.class);
		logger.debug("Using tenant: {}", tenant);
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
