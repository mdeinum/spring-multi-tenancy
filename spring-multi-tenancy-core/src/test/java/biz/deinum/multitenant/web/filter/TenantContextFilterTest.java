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

package biz.deinum.multitenant.web.filter;

import java.io.IOException;
import java.util.Arrays;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import biz.deinum.multitenant.context.TenantContextHolder;
import biz.deinum.multitenant.web.TenantIdentificationStrategy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

/**
 * Tests for the {@link TenantContextFilter}.
 *
 * @author Marten Deinum
 */
@RunWith(MockitoJUnitRunner.class)
public class TenantContextFilterTest {

	@Mock
	private TenantIdentificationStrategy strategy;

	private HttpServletRequest request = new MockHttpServletRequest();
	private HttpServletResponse response = new MockHttpServletResponse();
	private FilterChain mockChain = new MockFilterChain();

	private TenantContextFilter filter;

	@Before
	public void before() {

		TenantContextHolder.clearContext();
		this.filter = new TenantContextFilter(Arrays.asList(strategy));
	}

	@Test
	public void whenContextFoundThenTheContextShouldBeSet() throws Exception {

		when(this.strategy.getTenant(this.request)).thenReturn("test");
		this.filter.doFilter(this.request, this.response, new VerifyFilterChain("test"));
	}

	@Test(expected = IllegalStateException.class)
	public void whenNoContextFoundThenAnExceptionShouldBeThrown() throws Exception {
		when(this.strategy.getTenant(this.request)).thenReturn(null);
		filter.doFilter(this.request, this.response, this.mockChain);
	}

	@Test
	public void whenNoContextFoundThenContextShouldBeNull() throws Exception {

		this.filter.setThrowExceptionOnMissingTenant(false);
		when(strategy.getTenant(this.request)).thenReturn(null);
		this.filter.doFilter(this.request, this.response, new VerifyFilterChain(null));
	}

	@Test
	public void whenAfterCompletionIsCalledThenTheContextShouldBeNull() throws Exception {

		when(this.strategy.getTenant(this.request)).thenReturn("test");
		this.filter.doFilter(this.request, this.response, new VerifyFilterChain("test"));

		assertThat(TenantContextHolder.getContext().getTenant(), is(nullValue()));
	}

	private static class VerifyFilterChain implements FilterChain {

		private final String value;

		public VerifyFilterChain(String value) {
			super();
			this.value = value;
		}

		@Override
		public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {

			assertThat(TenantContextHolder.getContext().getTenant(), is(this.value));

		}
	}
}
