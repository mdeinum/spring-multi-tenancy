package biz.deinum.multitenant.messaging.support;

import biz.deinum.multitenant.context.TenantContextHolder;
import biz.deinum.multitenant.context.TenantContextTestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import java.util.Collections;

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
		message = new GenericMessage("dummy-test-payload");
	}

	@After
	public void after() {
		TenantContextHolder.clearContext();
	}

	@Test
	public void whenNoContextIsSetThenNoContextHeaderShouldBeSet() {

		Message msg = interceptor.preSend(message, null);
		String context = msg.getHeaders().get(interceptor.getHeaderName(), String.class);
		assertThat(context, is(nullValue()));
	}

	@Test
	public void whenContextIsSetThenTheContextHeaderShouldBeSet() {

		TenantContextTestUtil.setCurrentTenant("test");
		Message msg = interceptor.preSend(message, null);
		String context = msg.getHeaders().get(interceptor.getHeaderName(), String.class);
		assertThat(context, is("test"));
	}

	@Test
	public void whenContextHeaderIsSetThenContextShouldBeSet() {

		message = new GenericMessage("dummy-test-payload", Collections.singletonMap(interceptor.getHeaderName(), "test"));
		interceptor.postReceive(message, null);
		assertThat(TenantContextHolder.getContext().getTenant(), is("test"));
	}

	@Test
	public void whenNoContextHeaderIsSetThenContextShouldNotBeSet() {

		interceptor.postReceive(message, null);
		assertThat(TenantContextHolder.getContext().getTenant(), is(nullValue()));
	}


}
