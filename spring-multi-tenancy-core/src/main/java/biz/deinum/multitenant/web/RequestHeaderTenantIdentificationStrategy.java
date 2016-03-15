package biz.deinum.multitenant.web;

import biz.deinum.multitenant.context.TenantContextHolder;

import javax.servlet.http.HttpServletRequest;

/**
 * Use a request header to determine the context.
 *
 * @author Marten Deinum
 * @since 1.3
 */
public class RequestHeaderTenantIdentificationStrategy implements TenantIdentificationStrategy {

	private static final String DEFAULT_HEADER_NAME = TenantContextHolder.class.getName() + ".CONTEXT";

	private String headerName = DEFAULT_HEADER_NAME;

	@Override
	public String getTenant(HttpServletRequest request) {

		return request.getHeader(headerName);
	}

	public void setHeaderName(String headerName) {
		this.headerName = headerName;
	}
}
