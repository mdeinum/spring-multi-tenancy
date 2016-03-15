package biz.deinum.multitenant.web;

import org.springframework.web.bind.ServletRequestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Basic implementation of the {@code ContextRepository} uses a request parameter to determine the context to use.
 * This is not the best implementation nor the wisest it merely servers as an example!
 *
 * @author Marten Deinum
 */
public class RequestParameterTenantIdentificationStrategy implements TenantIdentificationStrategy {

	private static final String DEFAULT_REQUEST_PARAMETER = "tenant";

	private String parameter = DEFAULT_REQUEST_PARAMETER;

	@Override
	public String getTenant(HttpServletRequest request) {
		return ServletRequestUtils.getStringParameter(request, parameter, null);
	}

	public void setParameter(String parameter) {
		this.parameter = parameter;
	}
}
