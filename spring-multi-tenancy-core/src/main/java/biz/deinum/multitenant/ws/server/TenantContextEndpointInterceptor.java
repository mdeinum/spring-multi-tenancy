package biz.deinum.multitenant.ws.server;

import biz.deinum.multitenant.context.TenantContext;
import biz.deinum.multitenant.context.TenantContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.endpoint.interceptor.EndpointInterceptorAdapter;
import org.springframework.ws.transport.HeadersAwareReceiverWebServiceConnection;
import org.springframework.ws.transport.HeadersAwareSenderWebServiceConnection;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;

import java.io.IOException;
import java.util.Iterator;

/**
 * {@code EndpointInterceptor} which retrieves  the current tenant as a header if aa
 * {@code HeadersAwareReceiverWebServiceConnection} is detected. It will also add the current tenant as a header
 * if aa {@code HeadersAwareSenderWebServiceConnection} is detected. This is useful in case of async messaging and
 * the receiving side needs to be able to get the tenant again.
 *
 * @author Marten Deinum
 * @since 1.3
 */
public class TenantContextEndpointInterceptor extends EndpointInterceptorAdapter {

	private static final String DEFAULT_HEADER_NAME = TenantContextHolder.class.getName() + ".CONTEXT";

	private final Logger logger = LoggerFactory.getLogger(TenantContextEndpointInterceptor.class);

	private String headerName = DEFAULT_HEADER_NAME;

	private boolean throwExceptionOnMissingTenant = true;

	@Override
	public boolean handleResponse(MessageContext messageContext, Object endpoint) throws Exception {
		setTenantContextAsHeader();
		return true;
	}

	@Override
	public boolean handleFault(MessageContext messageContext, Object endpoint) throws Exception {
		setTenantContextAsHeader();
		return true;
	}

	private void setTenantContextAsHeader() throws IOException {
		TransportContext transportContext = TransportContextHolder.getTransportContext();
		WebServiceConnection connection = transportContext.getConnection();
		String tenant = TenantContextHolder.getContext().getTenant();
		if (tenant != null && connection instanceof HeadersAwareSenderWebServiceConnection) {
			((HeadersAwareSenderWebServiceConnection) connection).addRequestHeader(headerName, tenant);
		}
	}

	@Override
	public boolean handleRequest(MessageContext messageContext, Object endpoint) throws Exception {

		TransportContext transportContext = TransportContextHolder.getTransportContext();
		WebServiceConnection connection = transportContext.getConnection();
		TenantContext context = getTenantContext(connection);
		logger.debug("Using context: {}", context);
		TenantContextHolder.setContext(context);
		return true;
	}

	private TenantContext getTenantContext(WebServiceConnection connection) throws Exception {

		final TenantContext context = TenantContextHolder.createEmptyContext();
		if (connection instanceof HeadersAwareReceiverWebServiceConnection) {
			Iterator<String> headers = ((HeadersAwareReceiverWebServiceConnection) connection).getRequestHeaders(getHeaderName());
			if (headers.hasNext()) {
				context.setTenant(headers.next());
				return context;
			}
		}

		if (isThrowExceptionOnMissingTenant()) {
			throw new IllegalStateException("Could not determine tenant for current request!");
		}

		return context;
	}

	@Override
	public void afterCompletion(MessageContext messageContext, Object endpoint, Exception ex) throws Exception {
		TenantContextHolder.clearContext();
	}

	public String getHeaderName() {
		return this.headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}

	public boolean isThrowExceptionOnMissingTenant() {
		return this.throwExceptionOnMissingTenant;
	}

	public void setThrowExceptionOnMissingTenant(boolean throwExceptionOnMissingTenant) {
		this.throwExceptionOnMissingTenant = throwExceptionOnMissingTenant;
	}
}
