package biz.deinum.multitenant.hibernate.context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import biz.deinum.multitenant.context.TenantContextHolder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author marten
 */
public class TenantContextCurrentTenantIdentifierResolverTest {

	private static final String TENANT = "TEST_TENANT";

	private final TenantContextCurrentTenantIdentifierResolver resolver = new TenantContextCurrentTenantIdentifierResolver();

	@Before
	public void setup() {

		TenantContextHolder.getContext().setTenant(TENANT);
	}

	@After
	public void cleanUp() {

		TenantContextHolder.clearContext();
	}

	@Test
	public void testDefaultSettings() {

		assertThat(resolver.validateExistingCurrentSessions(), is(true));
	}

	@Test
	public void shouldReturnContextAsSet() {

		assertThat(resolver.resolveCurrentTenantIdentifier(), is(TENANT));
	}

	@Test
	public void settingPropertyShouldBeReflected() {
		resolver.setValidateExistingCurrentSessions(false);
		assertThat(resolver.validateExistingCurrentSessions(), is(false));
	}

}