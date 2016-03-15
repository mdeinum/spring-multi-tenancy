package biz.deinum.multitenant.web;

import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * Created with IntelliJ IDEA.
 * User: in329dei
 * Date: 4-6-13
 * Time: 10:17
 * To change this template use File | Settings | File Templates.
 */

public class RequestParameterTenantIdentificationStrategyTest {

	private RequestParameterTenantIdentificationStrategy strategy = new RequestParameterTenantIdentificationStrategy();

	private MockHttpServletRequest request;

	@Before
	public void before() {
		request = new MockHttpServletRequest();
	}

	@Test
	public void whenRequestParameterFoundThenValueShouldBeReturned() {

		request.setParameter("tenant", "test");
		String context = strategy.getTenant(request);
		assertThat(context, is("test"));
	}

	@Test
	public void whenRequestParameterNotFoundThenNullShouldBeReturned() {

		String context = strategy.getTenant(request);
		assertThat(context, is(nullValue()));
	}

}
