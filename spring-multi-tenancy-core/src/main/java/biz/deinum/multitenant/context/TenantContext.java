package biz.deinum.multitenant.context;

import java.io.Serializable;

/**
 * @author Marten Deinum
 * @since 1.3
 */
public interface TenantContext extends Serializable {

	String getTenant();

	void setTenant(String tenant);
}
