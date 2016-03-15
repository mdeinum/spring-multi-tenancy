package biz.deinum.multitenant.concurrent;

import biz.deinum.multitenant.context.TenantContext;
import biz.deinum.multitenant.context.TenantContextHolder;

/**
 * @author marten
 */
public abstract class TenantContextRunnable implements Runnable {

	private final TenantContext context;

	protected TenantContextRunnable(TenantContext context) {
		this.context = context;
	}

	@Override
	public final void run() {
		final TenantContext previousContext = TenantContextHolder.getContext();

		try {
			TenantContextHolder.setContext(this.context);
			runWithContext();
		} finally {
			TenantContextHolder.setContext(previousContext);
		}


	}

	protected abstract void runWithContext();
}
