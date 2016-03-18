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

package biz.deinum.multitenant.ws.client;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.ws.client.WebServiceIOException;
import org.springframework.ws.transport.context.DefaultTransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;

import biz.deinum.multitenant.context.TenantContextHolder;
import biz.deinum.multitenant.context.TenantContextTestUtil;
import biz.deinum.multitenant.ws.MockWebServiceConnection;

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
		this.connection = mock(MockWebServiceConnection.class);

		DefaultTransportContext context = new DefaultTransportContext(this.connection);
		TransportContextHolder.setTransportContext(context);
	}

	@After
	public void cleanUp() {

		TransportContextHolder.setTransportContext(null);
		TenantContextHolder.clearContext();
	}


	@Test
	public void checkPreConditions() {

		assertThat(this.interceptor.getHeaderName(), is(DEFAULT_HEADER));
		assertThat(this.interceptor.handleFault(null), is(true));
		assertThat(this.interceptor.handleResponse(null), is(true));
	}

	@Test
	public void headerNameShouldChange() {

		this.interceptor.setHeaderName(HEADER_NAME);
		assertThat(this.interceptor.getHeaderName(), is(HEADER_NAME));
	}

	@Test
	public void whenNoTenantSetNoHeaderShouldBeSet() throws Exception {

		TenantContextHolder.clearContext();
		this.interceptor.handleRequest(null);
		verify(this.connection, never()).addRequestHeader(any(String.class), any(String.class));
	}

	@Test
	public void whenTenantSetHeaderShouldBeSet() throws Exception {

		TenantContextTestUtil.setCurrentTenant(HEADER_VAL);
		this.interceptor.handleRequest(null);
		verify(this.connection, times(1)).addRequestHeader(DEFAULT_HEADER, HEADER_VAL);
	}

	@Test(expected = WebServiceIOException.class)
	public void whenIOExceptionThrownShouldBeConverted() throws Exception {

		doThrow(new IOException("test")).when(this.connection).addRequestHeader(DEFAULT_HEADER, HEADER_VAL);
		TenantContextTestUtil.setCurrentTenant(HEADER_VAL);
		this.interceptor.handleRequest(null);
	}

}
