package biz.deinum.multitenant.context;

import org.springframework.util.Assert;

/**
 * A <code>static</code> field-based implementation of {@link TenantContextHolderStrategy}.
 * <p>
 * This means that all intances in the JVM share the same {@code TenantContext}. This is useful
 * in situation where only a single tenant is required, like in a rich client after selecting the
 * tenant to use.
 *
 * @author M. Deinum
 * @since 1.3.0
 */
public class GlobalTenantContextHolderStrategy implements TenantContextHolderStrategy {

	private static TenantContext contextHolder;

	@Override
	public void clearContext() {
		contextHolder=null;
	}

	@Override
	public TenantContext getContext() {
		if (contextHolder == null) {
			contextHolder = createEmptyContext();
		}
		return contextHolder;
	}

	@Override
	public void setContext(TenantContext context) {
		Assert.notNull(context, "Only non-null TenantContext instances are permitted");
		contextHolder = context;
	}

	@Override
	public TenantContext createEmptyContext() {
		return new DefaultTenantContext();
	}
}
