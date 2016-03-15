package biz.deinum.multitenant.web;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Marten Deinum
 * @since 1.3
 */
public class StaticTenantIdentificationStrategy implements TenantIdentificationStrategy {

	private final String tenant;

	public StaticTenantIdentificationStrategy(String tenant) {
		this.tenant = tenant;
	}

	@Override
	public String getTenant(HttpServletRequest request) {
		return this.tenant;
	}
}
