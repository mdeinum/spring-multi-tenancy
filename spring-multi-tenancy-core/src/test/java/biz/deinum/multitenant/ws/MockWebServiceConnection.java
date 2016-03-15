package biz.deinum.multitenant.ws;

import org.springframework.ws.transport.HeadersAwareReceiverWebServiceConnection;
import org.springframework.ws.transport.HeadersAwareSenderWebServiceConnection;
import org.springframework.ws.transport.WebServiceConnection;

/**
 * @author marten
 */
public abstract class MockWebServiceConnection implements WebServiceConnection,
		HeadersAwareReceiverWebServiceConnection,
		HeadersAwareSenderWebServiceConnection {
}
