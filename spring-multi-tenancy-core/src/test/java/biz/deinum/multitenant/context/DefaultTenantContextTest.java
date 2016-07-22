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

import org.hamcrest.Matchers;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests for the {@link DefaultTenantContext}.
 *
 * @author Marten Deinum
 */
public class DefaultTenantContextTest {

	@Test
	public void setTenantShouldBeReturnedAsIdentifier() throws Exception {
		DefaultTenantContext ctx = new DefaultTenantContext();
		ctx.setTenant("test");

		assertThat(ctx.getTenant(), is("test"));
	}

	@Test
	public void setTenantShouldBeReturnedWhenGet() throws Exception {
		DefaultTenantContext ctx = new DefaultTenantContext();
		ctx.setTenant("test");

		assertThat(ctx.getTenant(), Matchers.<Object>is("test"));

	}

	@Test
	public void sameTenantShouldMakeTenantContextEqual() throws Exception {
		DefaultTenantContext ctx = new DefaultTenantContext();
		ctx.setTenant("test");

		DefaultTenantContext ctx2 = new DefaultTenantContext();
		ctx2.setTenant("test");

		assertThat(ctx.equals(null), is(false));
		assertThat(ctx.equals(ctx), is(true));
		assertThat(ctx.equals(ctx2), is(true));
		assertThat(ctx2.equals(ctx), is(true));
	}

	@Test
	public void sameTenantShouldLeadToSameHashCode() throws Exception {
		DefaultTenantContext ctx = new DefaultTenantContext();
		ctx.setTenant("test");

		DefaultTenantContext ctx2 = new DefaultTenantContext();
		ctx2.setTenant("test");

		assertThat(ctx.hashCode(), is(ctx2.hashCode()));
	}

	@Test
	public void messageShouldMatch() throws Exception {

		DefaultTenantContext ctx = new DefaultTenantContext();
		ctx.setTenant("test");

		assertThat(ctx.toString(), is("DefaultTenantContext [tenant='test']"));
	}
}