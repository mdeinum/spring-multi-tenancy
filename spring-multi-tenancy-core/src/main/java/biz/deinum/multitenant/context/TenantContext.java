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

import java.io.Serializable;

/**
 * The {@code TenantContext} wraps a tenant and returns an identifier as a String.
 *
 * @param <T> the type which needs to be {@code Serializable}.
 *
 * @author Marten Deinum
 * @since 1.3
 */
public interface TenantContext<T extends Serializable> extends Serializable {

	/**
	 *
	 * @return the tenant identifier as a {@code String}
	 */
	String tenantIdentifier();

	/**
	 * Get the tenant
	 * @return the tenant
	 */
	T getTenant();

	/**
	 * Set the tenant.
	 *
	 * @param tenant the tenant
	 */
	void setTenant(T tenant);
}
