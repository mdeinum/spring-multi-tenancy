package biz.deinum.multitenant.web.servlet;

import biz.deinum.multitenant.context.TenantContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.theme.AbstractThemeResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * {@code ThemeResolver} implementation which returns the current Context as
 * the name of the theme to use.
 *
 * Optionally a {@code prefix} and {@code suffix} can be applied.
 *
 * @author Marten Deinum
 * @since 1.3
 */
public class TenantContextThemeResolver extends AbstractThemeResolver {

	private String prefix = "";

	private String suffix = "";


	@Override
	public String resolveThemeName(HttpServletRequest request) {
		String tenant = TenantContextHolder.getContext().getTenant();
		if (StringUtils.hasText(tenant)) {
			return this.prefix + tenant + this.suffix;
		}
		return getDefaultThemeName();
	}

	@Override
	public void setThemeName(HttpServletRequest request, HttpServletResponse response, String themeName) {
		throw new UnsupportedOperationException("Cannot change theme - use a different theme resolution strategy");
	}

	public void setPrefix(String prefix) {
		this.prefix = (prefix != null ? prefix : "");
	}

	public void setSuffix(String suffix) {
		this.suffix = (suffix != null ? suffix : "");
	}
}
