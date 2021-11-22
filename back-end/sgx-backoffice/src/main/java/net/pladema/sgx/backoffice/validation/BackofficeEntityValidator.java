package net.pladema.sgx.backoffice.validation;

public interface BackofficeEntityValidator<E, I> {

	void assertCreate(E entity);

	void assertUpdate(I id, E entity);

	void assertDelete(I id);

}
