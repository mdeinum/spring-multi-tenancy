package biz.deinum.multitenant.http.client;

import biz.deinum.multitenant.context.TenantContextTestUtil;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;

/**
 * @author marten
 */
public class TenantContextHeaderInterceptorTest {

	private TenantContextHeaderInterceptor interceptor = new TenantContextHeaderInterceptor();

	@Test
	public void shouldSetHeaderOnRequest() {
		interceptor.setHeaderName("context");

		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(interceptor);

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