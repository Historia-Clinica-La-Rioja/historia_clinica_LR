package net.pladema.sgx.backoffice.permissions;

import net.pladema.sgx.backoffice.rest.ItemsAllowed;

import java.util.List;

public interface BackofficePermissionValidator<E, I> {

	void assertGetList(E entity);

	List<I> filterIdsByPermission(List<I> ids);

	void assertGetOne(I id);

	void assertCreate(E entity);

	void assertUpdate(I id, E entity);

	void assertDelete(I id);

	ItemsAllowed<I> itemsAllowedToList(E entity);

	ItemsAllowed<I> itemsAllowedToList();
}
