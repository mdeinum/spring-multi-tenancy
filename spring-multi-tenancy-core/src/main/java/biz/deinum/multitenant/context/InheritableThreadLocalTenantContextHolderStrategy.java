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

import org.springframework.util.Assert;

/**
 * {@link TenantContextHolderStrategy} implementation which delegates to an {@link InheritableThreadLocal}.
 *
 * @author Marten Deinum
 * @since 1.3
 */
class InheritableThreadLocalTenantContextHolderStrategy implements TenantContextHolderStrategy {

	private static final ThreadLocal<TenantContext> contextHolder = new InheritableThreadLocal<>();

	@Override
	public void clearContext() {
		contextHolder.remove();
	}

	@Override
	public TenantContext getContext() {
		TenantContext ctx = contextHolder.get();
		if (ctx == null) {
			ctx = createEmptyContext();
			contextHolder.set(ctx);
		}
		return ctx;
	}

	@Override
	public void setContext(TenantContext context) {
		Assert.notNull(context, "TenantContext cannot be null!");
		contextHolder.set(context);
	}

	@Override
	public TenantContext createEmptyContext() {
		return new DefaultTenantContext();
	}
}
