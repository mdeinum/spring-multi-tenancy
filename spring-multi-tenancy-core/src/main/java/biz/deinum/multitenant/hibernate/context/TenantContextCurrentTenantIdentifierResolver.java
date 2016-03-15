package biz.deinum.multitenant.hibernate.context;

import biz.deinum.multitenant.context.TenantContextHolder;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;

/**
 * {@code CurrentTenantIdentifierResolver} which simply delegates to the {@code ContextHolder} to
 * determine the current tenant identifier.
 *
 * @author Marten Deinum
 * @since 1.3.0
 */
public class TenantContextCurrentTenantIdentifierResolver implements CurrentTenantIdentifierResolver {

	private boolean validateExistingCurrentSessions = true;

	@Override
	public String resolveCurrentTenantIdentifier() {
		return TenantContextHolder.getContext().getTenant();
	}

	@Override
	public boolean validateExistingCurrentSessions() {
		return validateExistingCurrentSessions;
	}

	public void setValidateExistingCurrentSessions(boolean validateExistingCurrentSessions) {
		this.validateExistingCurrentSessions = validateExistingCurrentSessions;
	}
}
