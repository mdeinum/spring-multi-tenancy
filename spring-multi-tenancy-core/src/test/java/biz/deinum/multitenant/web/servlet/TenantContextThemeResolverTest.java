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

import org.junit.Before;
import org.junit.Test;

import biz.deinum.multitenant.context.TenantContextHolder;
import biz.deinum.multitenant.context.TenantContextTestUtil;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author marten
 */
public class TenantContextThemeResolverTest {

	private final TenantContextThemeResolver resolver = new TenantContextThemeResolver();

	@Before
	public void setup() {
		TenantContextHolder.clearContext();
	}

	@Test
	public void whenNoContextSetReturnTheDefaultTheme() {

		String theme = this.resolver.resolveThemeName(null);
		assertThat(theme, is("theme"));
	}

	@Test
	public void whenContextSetReturnThatContextAsTheme() {

		TenantContextTestUtil.setCurrentTenant("test-theme");

		String theme = this.resolver.resolveThemeName(null);
		assertThat(theme, is("test-theme"));
	}

	@Test
	public void whenContextSetReturnThatContextAsThemeWithPrefixAndSUffixApplied() {

		TenantContextTestUtil.setCurrentTenant("test-theme");
		this.resolver.setSuffix("-after");
		this.resolver.setPrefix("before-");
		String theme = this.resolver.resolveThemeName(null);
		assertThat(theme, is("before-test-theme-after"));
	}


}