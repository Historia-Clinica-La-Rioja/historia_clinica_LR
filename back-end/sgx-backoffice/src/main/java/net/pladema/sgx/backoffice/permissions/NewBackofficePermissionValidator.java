package net.pladema.sgx.backoffice.permissions;

public interface NewBackofficePermissionValidator<E, I> {

	void assertGetList(E entity);

	void assertGetOne(I id);

	void assertCreate(E entity);

	void assertUpdate(I id, E entity);

	void assertDelete(I id);
}
