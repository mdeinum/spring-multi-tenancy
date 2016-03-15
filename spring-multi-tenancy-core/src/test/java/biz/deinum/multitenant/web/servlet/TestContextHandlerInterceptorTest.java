package biz.deinum.multitenant.web.servlet;

import biz.deinum.multitenant.context.TenantContextHolder;
import biz.deinum.multitenant.web.TenantIdentificationStrategy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

/**
 * Tests for the ContextInterceptor.
 *
 * @author Marten Deinum
 */
@RunWith(MockitoJUnitRunner.class)
public class TestContextHandlerInterceptorTest {

	@Mock
	private TenantIdentificationStrategy strategy;

	private HttpServletRequest request = new MockHttpServletRequest();
	private HttpServletResponse response = new MockHttpServletResponse();

	private TenantContextHandlerInterceptor interceptor;

	@Before
	public void before() {

		TenantContextHolder.clearContext();
		interceptor = new TenantContextHandlerInterceptor(Arrays.asList(strategy));
	}

	@Test
	public void whenContextFoundThenTheContextShouldBeSet() throws Exception {

		when(strategy.getTenant(request)).thenReturn("test");
		interceptor.preHandle(request, response, null);
		assertThat(TenantContextHolder.getContext().getTenant(), is("test"));
	}

	@Test(expected = IllegalStateException.class)
	public void whenNoContextFoundThenAnExceptionShouldBeThrown() throws Exception {

		when(strategy.getTenant(request)).thenReturn(null);
		interceptor.preHandle(request, response, null);
	}

	@Test
	public void whenNoContextFoundThenContextShouldBeNull() throws Exception {

		interceptor.setThrowExceptionOnMissingTenant(false);
		when(strategy.getTenant(request)).thenReturn(null);
		interceptor.preHandle(request, response, null);
		assertNull(TenantContextHolder.getContext().getTenant());
	}

	@Test
	public void whenAfterCompletionIsCalledThenTheContextShouldBeNull() throws Exception {

		when(strategy.getTenant(request)).thenReturn("test");
		interceptor.preHandle(request, response, null);
		assertThat(TenantContextHolder.getContext().getTenant(), is("test"));
		interceptor.afterCompletion(request, response, null, null);
		assertThat(TenantContextHolder.getContext().getTenant(), is(nullValue()));
	}


}
