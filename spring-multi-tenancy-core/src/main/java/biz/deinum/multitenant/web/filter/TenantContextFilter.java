package biz.deinum.multitenant.web.filter;

import biz.deinum.multitenant.context.TenantContext;
import biz.deinum.multitenant.context.TenantContextHolder;
import biz.deinum.multitenant.web.TenantIdentificationStrategy;
import biz.deinum.multitenant.web.servlet.TenantContextHandlerInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>{@code javax.servlet.Filter} which sets the context from the current request.
 *
 * <p>Delegates the actual lookup to a {@code TenantIdentificationStrategy}.
 *
 * <p>When no context is found an {@code IllegalStateException} is thrown, this can be
 * switched off by setting the {@code throwExceptionOnMissingTenant}property.
 *
 * @author Marten Deinum
 * @see TenantContextHandlerInterceptor
 * @since 1.3
 */
public class TenantContextFilter extends OncePerRequestFilter {

	private final Logger logger = LoggerFactory.getLogger(TenantContextFilter.class);

	private final List<TenantIdentificationStrategy> strategies;

	private boolean throwExceptionOnMissingTenant = true;

	public TenantContextFilter(List<TenantIdentificationStrategy> strategies) {
		super();
		Assert.notEmpty(strategies, "At least one TenantIdentificationStrategy is required.");
		this.strategies = strategies;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

		try {
			TenantContext context = determineTenantContext(request);
			logger.debug("Using context: {}", context);
			TenantContextHolder.setContext(context);
			filterChain.doFilter(request, response);
		} finally {
			//Always clear the thread local after request processing.
			TenantContextHolder.clearContext();
		}

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

	/**
	 * When {@code true} (the default) an exception is throw if no tenant is found for the current request.
	 *
	 * @param throwExceptionOnMissingTenant {@code boolean} specifying if exception needs to be thrown.
	 */
	public void setThrowExceptionOnMissingTenant(boolean throwExceptionOnMissingTenant) {
		this.throwExceptionOnMissingTenant = throwExceptionOnMissingTenant;
	}
}
