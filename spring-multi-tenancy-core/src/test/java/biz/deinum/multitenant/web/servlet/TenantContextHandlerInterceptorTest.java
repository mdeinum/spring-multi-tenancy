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

package biz.deinum.multitenant.web.servlet;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import biz.deinum.multitenant.context.TenantContextHolder;
import biz.deinum.multitenant.web.TenantIdentificationStrategy;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link TenantContextHandlerInterceptor}.
 *
 * @author Marten Deinum
 */
@RunWith(MockitoJUnitRunner.class)
public class TenantContextHandlerInterceptorTest {

	@Mock
	private TenantIdentificationStrategy strategy;

	private HttpServletRequest request = new MockHttpServletRequest();
	private HttpServletResponse response = new MockHttpServletResponse();

	private TenantContextHandlerInterceptor interceptor;

	@Before
	public void before() {

		TenantContextHolder.clearContext();
		this.interceptor = new TenantContextHandlerInterceptor(Arrays.asList(this.strategy));
	}

	@Test
	public void whenContextFoundThenTheContextShouldBeSet() throws Exception {

		when(this.strategy.getTenant(this.request)).thenReturn("test");
		this.interceptor.preHandle(this.request, this.response, null);
		assertThat(TenantContextHolder.getContext().getTenant(), is("test"));
	}

	@Test(expected = IllegalStateException.class)
	public void whenNoContextFoundThenAnExceptionShouldBeThrown() throws Exception {

		when(this.strategy.getTenant(this.request)).thenReturn(null);
		this.interceptor.preHandle(this.request, this.response, null);
	}

	@Test
	public void whenNoContextFoundThenContextShouldBeNull() throws Exception {

		this.interceptor.setThrowExceptionOnMissingTenant(false);
		when(this.strategy.getTenant(this.request)).thenReturn(null);
		this.interceptor.preHandle(this.request, this.response, null);
		assertNull(TenantContextHolder.getContext().getTenant());
	}

	@Test
	public void whenAfterCompletionIsCalledThenTheContextShouldBeNull() throws Exception {

		when(this.strategy.getTenant(this.request)).thenReturn("test");
		this.interceptor.preHandle(this.request, this.response, null);
		assertThat(TenantContextHolder.getContext().getTenant(), is("test"));
		this.interceptor.afterCompletion(this.request, this.response, null, null);
		assertThat(TenantContextHolder.getContext().getTenant(), is(nullValue()));
	}


}
