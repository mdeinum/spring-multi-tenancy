package biz.deinum.multitenant.context;

import org.springframework.util.Assert;

/**
 * @author Marten Deinum
 * @since 1.3
 */
public class ThreadLocalTenantContextHolderStrategy implements TenantContextHolderStrategy {

	private static final ThreadLocal<TenantContext> contextHolder = new ThreadLocal<>();

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
