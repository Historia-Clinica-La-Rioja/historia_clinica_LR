package net.pladema.sgx.backoffice.permissions;

import java.util.List;

public interface BackofficePermissionValidator<E, I> {

	void assertGetList(E entity);

	List<I> filterByPermission(List<I> ids);

	void assertGetOne(I id);

	void assertCreate(E entity);

	void assertUpdate(I id, E entity);

	void assertDelete(I id);
}
