package net.pladema.sgx.acl.service;

public interface AclCrudPermissionValidator<E, I> {

	void assertGetList(E entity);

	void assertGetOne(I id);

	void assertCreate(E entity);

	void assertUpdate(I id);

	void assertDelete(I id);
}
