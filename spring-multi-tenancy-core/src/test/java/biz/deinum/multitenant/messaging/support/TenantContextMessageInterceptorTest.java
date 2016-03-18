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

package biz.deinum.multitenant.messaging.support;

import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import biz.deinum.multitenant.context.TenantContextHolder;
import biz.deinum.multitenant.context.TenantContextTestUtil;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Created with IntelliJ IDEA.
 * User: in329dei
 * Date: 4-6-13
 * Time: 11:06
 * To change this template use File | Settings | File Templates.
 */
public class TenantContextMessageInterceptorTest {

	private Message message;

	private TenantContextMessageInterceptor interceptor = new TenantContextMessageInterceptor();

	@Before
	public void before() {
		TenantContextHolder.clearContext();
		this.message = new GenericMessage("dummy-test-payload");
	}

	@After
	public void after() {
		TenantContextHolder.clearContext();
	}

	@Test
	public void whenNoContextIsSetThenNoContextHeaderShouldBeSet() {

		Message msg = this.interceptor.preSend(message, null);
		String context = msg.getHeaders().get(this.interceptor.getHeaderName(), String.class);
		assertThat(context, is(nullValue()));
	}

	@Test
	public void whenContextIsSetThenTheContextHeaderShouldBeSet() {

		TenantContextTestUtil.setCurrentTenant("test");
		Message msg = this.interceptor.preSend(message, null);
		String context = msg.getHeaders().get(this.interceptor.getHeaderName(), String.class);
		assertThat(context, is("test"));
	}

	@Test
	public void whenContextHeaderIsSetThenContextShouldBeSet() {

		this.message = new GenericMessage("dummy-test-payload", Collections.singletonMap(this.interceptor.getHeaderName(), "test"));
		this.interceptor.postReceive(this.message, null);
		assertThat(TenantContextHolder.getContext().tenantIdentifier(), is("test"));
	}

	@Test
	public void whenNoContextHeaderIsSetThenContextShouldNotBeSet() {

		this.interceptor.postReceive(this.message, null);
		assertThat(TenantContextHolder.getContext().tenantIdentifier(), is(nullValue()));
	}
}
