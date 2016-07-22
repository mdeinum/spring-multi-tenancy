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

import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * @author marten
 */
public class TenantContextHolderTests {

	@Test
	public void shouldNotHaveTenantAfterClearing() {

		TenantContextHolder.clearContext();
		assertThat(TenantContextHolder.getContext(), is(notNullValue()));
		assertThat(TenantContextHolder.getContext().getTenant(), is(nullValue()));
	}

	@Test
	public void shouldHaveValueAfterSetting() {

		TenantContext context = mock(TenantContext.class);

		TenantContextHolder.setContext(context);
		assertThat(TenantContextHolder.getContext(), is(context));
	}

}