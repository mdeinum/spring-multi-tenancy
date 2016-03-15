package biz.deinum.multitenant.context;

/**
 * @author marten
 */
public abstract class TenantContextTestUtil {

	public static void setCurrentTenant(String tenant) {

		TenantContext mockContext = new MockTenantContext();
		mockContext.setTenant(tenant);

		TenantContextHolder.setContext(mockContext);

	}

}
