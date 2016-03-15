package biz.deinum.multitenant.ws.client;

import biz.deinum.multitenant.context.TenantContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.ws.client.WebServiceClientException;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.client.support.interceptor.ClientInterceptor;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.transport.HeadersAwareSenderWebServiceConnection;
import org.springframework.ws.transport.WebServiceConnection;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;

import java.io.IOException;

/**
 * {@code ClientInterceptor} for adding a header to the underlying {@code WebServiceConnection}. Will only add a header if
 * there actually is a context set and the {@code WebServiceConnection} is an instance of the
 * {@code HeadersAwareSenderWebServiceConnection}.
 *
 * @author Marten Deinum
 * @since 1.3.0
 */
public class TentantContextClientInterceptor implements ClientInterceptor {

	private static final String DEFAULT_HEADER_NAME = TenantContextHolder.class.getName() + ".CONTEXT";

	private String headerName = DEFAULT_HEADER_NAME;


	@Override
	public boolean handleRequest(MessageContext messageContext) throws WebServiceClientException {

		TransportContext transportContext = TransportContextHolder.getTransportContext();
		WebServiceConnection webServiceConnection = transportContext.getConnection();
		final String tenant = TenantContextHolder.getContext().getTenant();
		if (webServiceConnection instanceof HeadersAwareSenderWebServiceConnection) {
			if (StringUtils.hasText(tenant)) {
				try {
					((HeadersAwareSenderWebServiceConnection) webServiceConnection).addRequestHeader(getHeaderName(), tenant);
				} catch (IOException e) {
					throw new WebServiceIOException("Error setting header '" + getHeaderName() + "' on connection.", e);
				}
			}
		}
		return true;
	}

	@Override
	public boolean handleResponse(MessageContext messageContext) throws WebServiceClientException {
		return true;
	}

	@Override
	public boolean handleFault(MessageContext messageContext) throws WebServiceClientException {
		return true;
	}

	@Override
	public void afterCompletion(MessageContext messageContext, Exception ex) throws WebServiceClientException {

	}

	public String getHeaderName() {
		return headerName;
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
}
