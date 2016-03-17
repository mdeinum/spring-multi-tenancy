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

package biz.deinum.multitenant.concurrent;

import java.util.concurrent.Callable;

import biz.deinum.multitenant.context.TenantContext;
import biz.deinum.multitenant.context.TenantContextHolder;

/**
 * {@link Callable} extension which sets the {@link TenantContext} before actually calling the method.
 *
 * @param <T> the type
 *
 * @author Marten Deinum
 * @since 1.3
 */
public abstract class TenantContextCallable<T> implements Callable<T> {

	private final TenantContext context;

	protected TenantContextCallable(TenantContext context) {
		this.context = context;
	}

	@Override
	public final T call() throws Exception {
		final TenantContext previousContext = TenantContextHolder.getContext();

		try {
			TenantContextHolder.setContext(this.context);
			return callWithContext();
		} finally {
			TenantContextHolder.setContext(previousContext);
		}
	}

	protected abstract T callWithContext() throws Exception;
}
