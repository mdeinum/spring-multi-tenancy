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

		strategy.setHeaderName("tenant");
		request.addHeader("tenant", "test");
		String context = strategy.getTenant(request);
		assertThat(context, is("test"));
	}

	@Test
	public void whenRequestParameterNotFoundThenDefaultValueShouldBeReturned() {
		String context = strategy.getTenant(request);
		assertThat(context, is(nullValue()));
	}

}