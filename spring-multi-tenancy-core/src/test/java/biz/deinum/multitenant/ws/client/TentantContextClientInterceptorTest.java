package biz.deinum.multitenant.ws.client;

import biz.deinum.multitenant.context.TenantContextHolder;
import biz.deinum.multitenant.context.TenantContextTestUtil;
import biz.deinum.multitenant.ws.MockWebServiceConnection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.transport.context.DefaultTransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * @author marten
 */
public class TentantContextClientInterceptorTest {

	private static final String DEFAULT_HEADER = "biz.deinum.multitenant.context.TenantContextHolder.CONTEXT";
	private static final String HEADER_NAME = "tenant-header";
	private static final String HEADER_VAL = "tenant";

	private final TentantContextClientInterceptor interceptor = new TentantContextClientInterceptor();

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
	}


	@Test
	public void checkPreConditions() {

		assertThat(interceptor.getHeaderName(), is(DEFAULT_HEADER));
		assertThat(interceptor.handleFault(null), is(true));
		assertThat(interceptor.handleResponse(null), is(true));
	}

	@Test
	public void headerNameShouldChange() {

		interceptor.setHeaderName(HEADER_NAME);
		assertThat(interceptor.getHeaderName(), is(HEADER_NAME));
	}

	@Test
	public void whenNoTenantSetNoHeaderShouldBeSet() throws Exception {

		TenantContextHolder.clearContext();
		interceptor.handleRequest(null);
		verify(connection, never()).addRequestHeader(any(String.class), any(String.class));
	}

	@Test
	public void whenTenantSetHeaderShouldBeSet() throws Exception {

		TenantContextTestUtil.setCurrentTenant(HEADER_VAL);
		interceptor.handleRequest(null);
		verify(connection, times(1)).addRequestHeader(DEFAULT_HEADER, HEADER_VAL);
	}

	@Test(expected = WebServiceIOException.class)
	public void whenIOExceptionThrownShouldBeConverted() throws Exception {

		doThrow(new IOException("test")).when(connection).addRequestHeader(DEFAULT_HEADER, HEADER_VAL);
		TenantContextTestUtil.setCurrentTenant(HEADER_VAL);
		interceptor.handleRequest(null);
	}

}