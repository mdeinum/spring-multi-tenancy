package biz.deinum.multitenant.web;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author marten
 */
public class StaticTenantIdentificationStrategyTest {

	@Test(expected = IllegalArgumentException.class)
	public void emptyTenantIdentifierThrowsExceptionOnConstruction() throws Exception {
		new StaticTenantIdentificationStrategy("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullTenantIdentifierThrowsExceptionOnConstruction() throws Exception {
		new StaticTenantIdentificationStrategy(null);
	}

	@Test
	public void shouldAlwaysReturnTheGivenTenant() {
		StaticTenantIdentificationStrategy strategy = new StaticTenantIdentificationStrategy("test");
		assertThat(strategy.getTenant(null), is("test"));
	}

}