package biz.deinum.multitenant.web.filter;

import biz.deinum.multitenant.context.TenantContextHolder;
import biz.deinum.multitenant.web.TenantIdentificationStrategy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.Mockito.when;

/**
 * Tests for the ContextInterceptor.
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
		filter = new TenantContextFilter(Arrays.asList(strategy));
	}

	@Test
	public void whenContextFoundThenTheContextShouldBeSet() throws Exception {
		when(strategy.getTenant(request)).thenReturn("test");
		filter.doFilter(request, response, new VerifyFilterChain("test"));
	}

	@Test(expected = IllegalStateException.class)
	public void whenNoContextFoundThenAnExceptionShouldBeThrown() throws Exception {
		when(strategy.getTenant(request)).thenReturn(null);
		filter.doFilter(request, response, mockChain);
	}

	@Test
	public void whenNoContextFoundThenContextShouldBeNull() throws Exception {
		filter.setThrowExceptionOnMissingTenant(false);
		when(strategy.getTenant(request)).thenReturn(null);
		filter.doFilter(request, response, new VerifyFilterChain(null));
	}

	@Test
	public void whenAfterCompletionIsCalledThenTheContextShouldBeNull() throws Exception {
		when(strategy.getTenant(request)).thenReturn("test");
		filter.doFilter(request, response, new VerifyFilterChain("test"));

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

			assertThat(TenantContextHolder.getContext().getTenant(), is(value));

		}
	}

}
