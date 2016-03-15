package biz.deinum.multitenant.ws.server;

import biz.deinum.multitenant.context.TenantContextHolder;
import biz.deinum.multitenant.context.TenantContextHolderStrategy;
import biz.deinum.multitenant.context.TenantContextTestUtil;
import biz.deinum.multitenant.context.ThreadLocalTenantContextHolderStrategy;
import biz.deinum.multitenant.ws.MockWebServiceConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ws.transport.context.DefaultTransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

/**
 * Tests for the {@code ContextEndpointInterceptorTests}.
 *
 * @author Marten Deinum
 */
public class TenantContextEndpointInterceptorTests {

	private static final String DEFAULT_HEADER = "biz.deinum.multitenant.context.TenantContextHolder.CONTEXT";

	private static final String HEADER_NAME = "context-header";
	private static final String HEADER_VAL = "context";

	private final TenantContextEndpointInterceptor interceptor = new TenantContextEndpointInterceptor();

	private MockWebServiceConnection connection;

	@Before
	public void setup() {
		// Setup TransportContext
		connection = mock(MockWebServiceConnection.class);

		DefaultTransportContext context = new DefaultTransportContext(connection);
		TransportContextHolder.setTransportContext(context);

	}

	@After
	public void cleanUp() {

		TransportContextHolder.setTransportContext(null);
		TenantContextHolder.clearContext();
		TenantContextHolder.setStrategy(new ThreadLocalTenantContextHolderStrategy());
	}

	@Test
	public void retrieveContextFromHeader() throws Exception {

		when(connection.getRequestHeaders(DEFAULT_HEADER)).thenReturn(Arrays.asList(HEADER_VAL).iterator());
		interceptor.handleRequest(null, null);
		assertThat(TenantContextHolder.getContext().getTenant(), is(HEADER_VAL));
	}

	@Test
	public void setContextFromHeaderWhenSendingResponse() throws Exception {

		TenantContextTestUtil.setCurrentTenant(HEADER_VAL);
		interceptor.handleResponse(null, null);
		verify(connection, times(1)).addRequestHeader(DEFAULT_HEADER, HEADER_VAL);
	}

	@Test
	public void setContextFromHeaderWhenSendingFault() throws Exception {

		TenantContextTestUtil.setCurrentTenant(HEADER_VAL);
		interceptor.handleFault(null, null);
		verify(connection, times(1)).addRequestHeader(DEFAULT_HEADER, HEADER_VAL);
	}

	@Test
	public void shouldNotSetHeaderWhenNoCurrentTenant() throws Exception {

		TenantContextHolder.clearContext();
		interceptor.handleResponse(null, null);
		verify(connection, never()).addRequestHeader(any(String.class), any(String.class));
	}

	@Test
	public void checkDefaultSettings() throws Exception {

		assertThat(interceptor.getHeaderName(), is(DEFAULT_HEADER));
		assertThat(interceptor.isThrowExceptionOnMissingTenant(), is(true));
	}

	@Test
	public void whenChangingHeaderNameItShouldBeSet() {

		interceptor.setHeaderName(HEADER_NAME);
		assertThat(interceptor.getHeaderName(), is(HEADER_NAME));
	}

	@Test(expected = IllegalStateException.class)
	public void whenNoTenantFoundIllegalStateExceptionShouldBeThrown() throws Exception {

		interceptor.setThrowExceptionOnMissingTenant(true);
		when(connection.getRequestHeaders(DEFAULT_HEADER)).thenReturn(Collections.<String>emptyIterator());
		interceptor.handleRequest(null, null);
	}

	@Test
	public void whenNoTenantFoundAndThrowExceptionDisabledThenReturnNull() throws Exception {

		interceptor.setThrowExceptionOnMissingTenant(false);
		when(connection.getRequestHeaders(DEFAULT_HEADER)).thenReturn(Collections.<String>emptyIterator());
		interceptor.handleRequest(null, null);
		assertThat(TenantContextHolder.getContext().getTenant(), is(nullValue()));
	}

	@Test
	public void shouldClearContextInAfterCompletion() throws Exception {
		TenantContextHolderStrategy mockStrategy = mock(TenantContextHolderStrategy.class);
		TenantContextHolder.setStrategy(mockStrategy);
		interceptor.afterCompletion(null, null, new Exception());

		verify(mockStrategy, times(1)).clearContext();

	}

}