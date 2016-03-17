/*
 * Copyright 2007-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package biz.deinum.multitenant.web.servlet;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import biz.deinum.multitenant.context.TenantContext;
import biz.deinum.multitenant.context.TenantContextHolder;
import biz.deinum.multitenant.web.TenantIdentificationStrategy;

/**
 * {@code HandlerInterceptor} which sets the context from the current request.
 * Delegates the actual lookup to a {@link TenantIdentificationStrategy}.
 *
 * <p>When no context is found an IllegalStateException is thrown, this can be
 * switched of by setting the {@code throwExceptionOnMissingTenant} property.
 *
 * @author Marten Deinum
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
		this.logger.debug("Using context: {}", context);
		TenantContextHolder.setContext(context);
		return true;
	}


	private TenantContext determineTenantContext(HttpServletRequest request) {

		TenantContext context = TenantContextHolder.createEmptyContext();
		context.setTenant(determineTenant(request));
		return context;

	}

	protected String determineTenant(HttpServletRequest request) {

		for (TenantIdentificationStrategy strategy : this.strategies) {
			String tenant = strategy.getTenant(request);
			if (tenant != null) {
				return tenant;
			}
		}

		if (isThrowExceptionOnMissingTenant()) {
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

	public boolean isThrowExceptionOnMissingTenant() {
		return this.throwExceptionOnMissingTenant;
	}
}
