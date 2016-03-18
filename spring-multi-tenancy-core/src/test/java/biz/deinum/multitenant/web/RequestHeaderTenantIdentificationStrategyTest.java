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

package biz.deinum.multitenant.web;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * @author marten
 */
public class RequestHeaderTenantIdentificationStrategyTest {

	private RequestHeaderTenantIdentificationStrategy strategy = new RequestHeaderTenantIdentificationStrategy();

	private MockHttpServletRequest request;

	@Before
	public void before() {
		request = new MockHttpServletRequest();
	}

	@Test
	public void whenRequestParameterFoundThenValueShouldBeReturned() {

		this.strategy.setHeaderName("tenant");
		this.request.addHeader("tenant", "test");
		String context = this.strategy.getTenant(this.request);
		assertThat(context, is("test"));
	}

	@Test
	public void whenRequestParameterNotFoundThenDefaultValueShouldBeReturned() {
		String context = this.strategy.getTenant(this.request);
		assertThat(context, is(nullValue()));
	}

}