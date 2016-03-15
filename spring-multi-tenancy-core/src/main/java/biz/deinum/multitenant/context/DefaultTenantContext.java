package biz.deinum.multitenant.context;

/**
 * @author Marten Deinum
 * @since 1.3
 */
public class DefaultTenantContext implements TenantContext {

	private String tenant;

	@Override
	public String getTenant() {
		return this.tenant;
	}

	@Override
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) { return true; }
		if (o == null || getClass() != o.getClass()) { return false; }

		DefaultTenantContext that = (DefaultTenantContext) o;

		return tenant != null ? tenant.equals(that.tenant) : that.tenant == null;

	}

	@Override
	public int hashCode() {
		return tenant != null ? tenant.hashCode() : 0;
	}

	@Override
	public String toString() {
		return "DefaultTenantContext [tenant='" + this.tenant + "']";
	}
}
