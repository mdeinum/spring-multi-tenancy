package biz.deinum.multitenant.web.servlet;

import biz.deinum.multitenant.context.TenantContextHolder;
import biz.deinum.multitenant.context.TenantContextTestUtil;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author marten
 */
public class TenantContextThemeResolverTest {

	private final TenantContextThemeResolver resolver = new TenantContextThemeResolver();

	@Before
	public void setup() {
		TenantContextHolder.clearContext();
	}

	@Test
	public void whenNoContextSetReturnTheDefaultTheme() {

		String theme = resolver.resolveThemeName(null);
		assertThat(theme, is("theme"));
	}

	@Test
	public void whenContextSetReturnThatContextAsTheme() {

		TenantContextTestUtil.setCurrentTenant("test-theme");

		String theme = resolver.resolveThemeName(null);
		assertThat(theme, is("test-theme"));
	}

	@Test
	public void whenContextSetReturnThatContextAsThemeWithPrefixAndSUffixApplied() {

		TenantContextTestUtil.setCurrentTenant("test-theme");
		resolver.setSuffix("-after");
		resolver.setPrefix("before-");
		String theme = resolver.resolveThemeName(null);
		assertThat(theme, is("before-test-theme-after"));
	}


}