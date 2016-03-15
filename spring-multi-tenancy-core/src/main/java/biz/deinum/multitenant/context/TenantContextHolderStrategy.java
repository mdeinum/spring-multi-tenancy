package biz.deinum.multitenant.context;

/**
 * @author marten
 */
public interface TenantContextHolderStrategy {

	void clearContext();

	TenantContext getContext();

	void setContext(TenantContext context);

	TenantContext createEmptyContext();

}
