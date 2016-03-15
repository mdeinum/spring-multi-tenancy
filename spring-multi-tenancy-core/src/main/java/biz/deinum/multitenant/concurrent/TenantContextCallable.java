package biz.deinum.multitenant.concurrent;

import biz.deinum.multitenant.context.TenantContext;
import biz.deinum.multitenant.context.TenantContextHolder;

import java.util.concurrent.Callable;

/**
 * @author Marten Deinum
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
