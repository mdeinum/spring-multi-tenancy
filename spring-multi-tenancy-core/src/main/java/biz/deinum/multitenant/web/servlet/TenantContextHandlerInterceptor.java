package biz.deinum.multitenant.web.servlet;

import biz.deinum.multitenant.context.TenantContext;
import biz.deinum.multitenant.context.TenantContextHolder;
import biz.deinum.multitenant.web.TenantIdentificationStrategy;
import biz.deinum.multitenant.web.filter.TenantContextFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * {@code HandlerInterceptor} which sets the context from the current request.
 * Delegates the actual lookup to a {@link TenantIdentificationStrategy}.
 *
 * <p>When no context is found an IllegalStateException is thrown, this can be
 * switched of by setting the {@code throwExceptionOnMissingTenant} property.
 *
 * @author Marten Deinum
 * @see TenantContextFilter
 * @since 1.3
 */
public class TenantContextHandlerInterceptor extends HandlerInterceptorAdapter {

	private final Logger logger = LoggerFactory.getLogger(TenantContextHandlerInterceptor.class);
	private final List<TenantIdentificationStrategy> strategies;

	private boolean throwExceptionOnMissingTenant = true;

	public TenantContextHandlerInterceptor(List<TenantIdentificationStrategy> strategies) {
		super();
		this.strategies = strategies;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		TenantContext context = determineTenantContext(request);
		logger.debug("Using context: {}", context);
		TenantContextHolder.setContext(context);
		return true;
	}


	private TenantContext determineTenantContext(HttpServletRequest request) {

		TenantContext context = TenantContextHolder.createEmptyContext();
		context.setTenant(determineTenant(request));
		return context;

	}

	protected String determineTenant(HttpServletRequest request) {

		for (TenantIdentificationStrategy strategy : strategies) {
			String tenant = strategy.getTenant(request);
			if (tenant != null) {
				return tenant;
			}
		}

		if (throwExceptionOnMissingTenant) {
			throw new IllegalStateException("Could not determine tenant for current request!");
		}
		return null;
	}


	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		TenantContextHolder.clearContext();
	}

	public void setThrowExceptionOnMissingTenant(boolean throwExceptionOnMissingTenant) {
		this.throwExceptionOnMissingTenant = throwExceptionOnMissingTenant;
	}
}
