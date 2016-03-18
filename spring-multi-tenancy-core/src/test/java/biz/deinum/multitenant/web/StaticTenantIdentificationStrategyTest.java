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

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests for the {@link StaticTenantIdentificationStrategy}.
 *
 * @author Marten Deinum
 */
public class StaticTenantIdentificationStrategyTest {

	@Test(expected = IllegalArgumentException.class)
	public void emptyTenantIdentifierThrowsExceptionOnConstruction() throws Exception {
		new StaticTenantIdentificationStrategy("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void nullTenantIdentifierThrowsExceptionOnConstruction() throws Exception {
		new StaticTenantIdentificationStrategy(null);
	}

	@Test
	public void shouldAlwaysReturnTheGivenTenant() {
		StaticTenantIdentificationStrategy strategy = new StaticTenantIdentificationStrategy("test");
		assertThat(strategy.getTenant(null), is("test"));
	}

}