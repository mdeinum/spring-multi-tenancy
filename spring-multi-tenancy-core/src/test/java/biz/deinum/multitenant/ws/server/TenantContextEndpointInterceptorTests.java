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

package biz.deinum.multitenant.ws.server;

import java.util.Arrays;
import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ws.transport.context.DefaultTransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;

import biz.deinum.multitenant.context.TenantContextHolder;
import biz.deinum.multitenant.context.TenantContextHolderStrategy;
import biz.deinum.multitenant.context.TenantContextTestUtil;
import biz.deinum.multitenant.ws.MockWebServiceConnection;

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
		this.connection = mock(MockWebServiceConnection.class);

		DefaultTransportContext context = new DefaultTransportContext(connection);
		TransportContextHolder.setTransportContext(context);

	}

	@After
	public void cleanUp() {

		TransportContextHolder.setTransportContext(null);
		TenantContextHolder.clearContext();
	}

	@Test
	public void retrieveContextFromHeader() throws Exception {

		when(this.connection.getRequestHeaders(DEFAULT_HEADER)).thenReturn(Arrays.asList(HEADER_VAL).iterator());
		this.interceptor.handleRequest(null, null);
		assertThat(TenantContextHolder.getContext().getTenant(), is(HEADER_VAL));
	}

	@Test
	public void setContextFromHeaderWhenSendingResponse() throws Exception {

		TenantContextTestUtil.setCurrentTenant(HEADER_VAL);
		this.interceptor.handleResponse(null, null);
		verify(this.connection, times(1)).addRequestHeader(DEFAULT_HEADER, HEADER_VAL);
	}

	@Test
	public void setContextFromHeaderWhenSendingFault() throws Exception {

		TenantContextTestUtil.setCurrentTenant(HEADER_VAL);
		this.interceptor.handleFault(null, null);
		verify(this.connection, times(1)).addRequestHeader(DEFAULT_HEADER, HEADER_VAL);
	}

	@Test
	public void shouldNotSetHeaderWhenNoCurrentTenant() throws Exception {

		TenantContextHolder.clearContext();
		this.interceptor.handleResponse(null, null);
		verify(this.connection, never()).addRequestHeader(any(String.class), any(String.class));
	}

	@Test
	public void checkDefaultSettings() throws Exception {

		assertThat(this.interceptor.getHeaderName(), is(DEFAULT_HEADER));
		assertThat(this.interceptor.isThrowExceptionOnMissingTenant(), is(true));
	}

	@Test
	public void whenChangingHeaderNameItShouldBeSet() {

		this.interceptor.setHeaderName(HEADER_NAME);
		assertThat(this.interceptor.getHeaderName(), is(HEADER_NAME));
	}

	@Test(expected = IllegalStateException.class)
	public void whenNoTenantFoundIllegalStateExceptionShouldBeThrown() throws Exception {

		this.interceptor.setThrowExceptionOnMissingTenant(true);
		when(this.connection.getRequestHeaders(DEFAULT_HEADER)).thenReturn(Collections.<String>emptyIterator());
		this.interceptor.handleRequest(null, null);
	}

	@Test
	public void whenNoTenantFoundAndThrowExceptionDisabledThenReturnNull() throws Exception {

		this.interceptor.setThrowExceptionOnMissingTenant(false);
		when(this.connection.getRequestHeaders(DEFAULT_HEADER)).thenReturn(Collections.<String>emptyIterator());
		this.interceptor.handleRequest(null, null);
		assertThat(TenantContextHolder.getContext().getTenant(), is(nullValue()));
	}

	@Test
	public void shouldClearContextInAfterCompletion() throws Exception {
		TenantContextHolderStrategy mockStrategy = mock(TenantContextHolderStrategy.class);
		TenantContextHolder.setStrategy(mockStrategy);
		this.interceptor.afterCompletion(null, null, new Exception());

		verify(mockStrategy, times(1)).clearContext();

	}
}
