package biz.deinum.multitenant.context;

/**
 * @author marten
 */
public class MockTenantContext implements TenantContext {

	private String tenant;

	@Override
	public String getTenant() {
		return this.tenant;
	}

	@Override
	public void setTenant(String tenant) {
		this.tenant = tenant;
	}
}
