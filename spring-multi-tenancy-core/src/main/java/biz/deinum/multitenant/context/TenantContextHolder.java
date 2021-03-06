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

package biz.deinum.multitenant.context;

import java.lang.reflect.Constructor;

import org.springframework.util.ReflectionUtils;

/**
 * ContextHolder will hold a reference to a context which can be used through-out
 * the application. The actual storing and creating is delegated to a {@link TenantContextHolderStrategy}
 * implementation.
 *
 * @author Marten Deinum
 * @since 1.0
 */
public abstract class TenantContextHolder {

	public static final String MODE_THREADLOCAL = "MODE_THREADLOCAL";
	public static final String MODE_INHERITABLETHREADLOCAL = "MODE_INHERITABLETHREADLOCAL";
	public static final String MODE_GLOBAL = "MODE_GLOBAL";
	public static final String SYSTEM_PROPERTY = "multi_tenant.strategy";

	private static String strategyName = System.getProperty(SYSTEM_PROPERTY, MODE_THREADLOCAL);

	private static TenantContextHolderStrategy strategy;

	static {
		initialize();
	}

	private static void initialize() {
		if ((strategyName == null) || "".equals(strategyName)) {
			// Set default
			strategyName = MODE_THREADLOCAL;
		}

		if (strategyName.equals(MODE_THREADLOCAL)) {
			strategy = new ThreadLocalTenantContextHolderStrategy();
		}
		else if (strategyName.equals(MODE_INHERITABLETHREADLOCAL)) {
			strategy = new InheritableThreadLocalTenantContextHolderStrategy();
		}
		else if (strategyName.equals(MODE_GLOBAL)) {
			strategy = new GlobalTenantContextHolderStrategy();
		}
		else {
			// Try to load a custom strategy
			try {
				Class<?> clazz = Class.forName(strategyName);
				Constructor<?> customStrategy = clazz.getConstructor();
				strategy = (TenantContextHolderStrategy) customStrategy.newInstance();
			}
			catch (Exception ex) {
				ReflectionUtils.handleReflectionException(ex);
			}
		}
	}

	public static TenantContext getContext() {
		TenantContext context = strategy.getContext();
		return context;
	}

	public static void setContext(final TenantContext context) {
		strategy.setContext(context);
	}

	public static void clearContext() {
		strategy.clearContext();
	}

	public static TenantContext createEmptyContext() {
		return strategy.createEmptyContext();
	}

	/**
	 * Changes the preferred strategy. Do <em>NOT</em> call this method more than once for
	 * a given JVM, as it will re-initialize the strategy and adversely affect any
	 * existing threads using the old strategy.
	 *
	 * @param strategyName the fully qualified class name of the strategy that should be
	 * used.
	 */
	public static void setStrategyName(String strategyName) {
		TenantContextHolder.strategyName = strategyName;
		initialize();
	}

	@Override
	public String toString() {
		return String.format("TenantContextHolder [strategy=%s]", strategyName);
	}
}
