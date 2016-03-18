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

package biz.deinum.multitenant.http.client;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import biz.deinum.multitenant.context.TenantContextTestUtil;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;

/**
 * @author marten
 */
public class TenantContextHeaderInterceptorTest {

	private TenantContextHeaderInterceptor interceptor = new TenantContextHeaderInterceptor();

	@Test
	public void shouldSetHeaderOnRequest() {
		this.interceptor.setHeaderName("context");

		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(this.interceptor);

		RestTemplate template = new RestTemplate();
		template.setInterceptors(interceptors);

		MockRestServiceServer mockServer = MockRestServiceServer.createServer(template);

		mockServer.expect(requestTo("/test"))
				.andExpect(method(GET))
				.andExpect(header("context", "test-context"))
				.andRespond(MockRestResponseCreators.withSuccess());

		TenantContextTestUtil.setCurrentTenant("test-context");

		HttpEntity entity = new HttpEntity(null, null);

		template.exchange("/test", GET, entity, String.class);

		mockServer.verify();

	}


}