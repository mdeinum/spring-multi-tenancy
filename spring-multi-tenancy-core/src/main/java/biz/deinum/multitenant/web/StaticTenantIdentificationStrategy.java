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

package biz.deinum.multitenant.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.Assert;

/**
 * {@code TenantIdentificationStrategy} which always returns the same tenant identifier. Useful if an tenant
 * identifier is needed in case there isn't one, for instance as a default.
 *
 * @author Marten Deinum
 * @since 1.3
 */
public class StaticTenantIdentificationStrategy implements TenantIdentificationStrategy {

	private final String tenant;

	public StaticTenantIdentificationStrategy(String tenant) {
		Assert.hasText(tenant, "Tenant identifier cannot be empty.");
		this.tenant = tenant;
	}

	@Override
	public String getTenant(HttpServletRequest request) {
		return this.tenant;
	}
}
