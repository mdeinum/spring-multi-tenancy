package biz.deinum.multitenant.web;

import javax.servlet.http.HttpServletRequest;

/**
 * Repository for loading the context based on the current request.
 *
 * @author Marten Deinum
 * @since 1.2
 */
public interface TenantIdentificationStrategy {

	/**
	 * Determine the tenant based on the current request. If the strategy
	 * cannot determine a tenant, return {@code null}.
	 *
	 * @param request the current request
	 * @return the tenant or {@code null}.
	 */
	String getTenant(HttpServletRequest request);
}
