package biz.deinum.multitenant.context;

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * @author marten
 */
public class TenantContextHolderTests {

	@Test
	public void shouldNotHaveTenantAfterClearing() {

		TenantContextHolder.clearContext();
		assertThat(TenantContextHolder.getContext(), is(notNullValue()));
		assertThat(TenantContextHolder.getContext().getTenant(), is(nullValue()));
	}

	@Test
	public void shouldHaveValueAfterSetting() {

		TenantContext context = mock(TenantContext.class);

		TenantContextHolder.setContext(context);
		assertThat(TenantContextHolder.getContext(), is(context));
	}

}